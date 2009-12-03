require 'digest'
require 'base64'
require 'ixtlan/modified_by'
require 'dm-serializer'
module Ixtlan
  module Models
    class User
      include DataMapper::Resource

      def self.default_storage_name
        "User"
      end

      property :id, Serial, :field => "uidnumber"

      property :login, String, :nullable => false , :length => 4..32, :index => :unique_index, :format => /^[a-zA-z0-9]*$/, :writer => :private, :field => "uid"

      property :name, String, :nullable => false, :format => /^[^<">]*$/, :length => 2..64, :field => "cn"

      property :email, String, :nullable => false, :format => :email_address, :nullable => false, :length => 8..64, :index => :unique_index, :field => "mail"

      property :language, String, :nullable => false, :format => /[a-z][a-z]/, :length => 2, :field => "preferredlanguage"

      property :hashed_password, String, :nullable => true, :length => 128, :accessor => :private, :field => "userpassword"

      timestamps :at

      modified_by ::Ixtlan::Models::USER

      # Virtual attribute for the plaintext password
      attr_reader :password

      validates_is_unique  :login
      validates_is_unique  :email

      def reset_password(len = 12)
        @password = Ixtlan::Passwords.generate(len)
        attribute_set(:hashed_password, Ixtlan::Digest.ssha(@password, "--#{Time.now}--#{login}--"))
        @password
      end
      
      def digest
        ::Base64.decode64(@hashed_password[6,200])
      end

      def self.authenticate(login, password)
        user = first(:login => login) # need to get the salt
        if user
          digest = user.digest
          salt = digest[20,147]
          if ::Digest::SHA1.digest("#{password}" + salt) == digest[0,20]
            @logger ||= Ixtlan::UserLogger.new(self)
            @logger.log_user(login, "logged in")
            user
          else
            "wrong password"
          end
        else
          "unknown login"
        end
      end

      def groups
        if @groups.nil?
          # TODO spec the empty array to make sure new relations are stored
          # in the database or the groups collection is empty before filling it
          @groups = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Models::Group), [])
          GroupUser.all(:memberuid => login).each do |gu| 
            @groups << gu.group
          end
          def @groups.user=(user)
            @user = user
          end
          @groups.user = self
          def @groups.<<(group)
            group.locales(@user)
            unless member? group
              gu = GroupUser.create(:memberuid => @user.login, :gidnumber => group.id)
              super
            end
            
            self
          end
          
          def @groups.delete(group) 
            gu = GroupUser.first(:memberuid => @user.login, :gidnumber => group.id)
            if gu
              gu.destroy
            end
            super
          end
          @groups.each {|g| g.locales(self) }
        end
        @groups
      end

      # make sure login is immutable
      def login=(new_login)
        attribute_set(:login, new_login) if (login.nil? or login == "'NULL'" or login == "NULL")
      end

      if protected_instance_methods.find {|m| m == 'to_x'}.nil?
        alias :to_x :to_xml_document 

        protected

        def to_xml_document(opts={}, doc = nil)
          opts.merge!({:exclude => [:hashed_password], :methods => [:groups]})
          to_x(opts, doc)
        end
      end
    end
  end
end
