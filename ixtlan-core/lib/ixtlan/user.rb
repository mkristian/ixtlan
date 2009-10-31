require 'digest'
require 'ixtlan/modified_by'
module Ixtlan
  class User
    include DataMapper::Resource

    def self.name
      "User"
    end

    def self.to_sh
      "User"
    end

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
    
    modified_by "::Ixtlan::User"

    # Virtual attribute for the plaintext password
    attr_reader :password

    validates_is_unique  :login
    validates_is_unique  :email

    def reset_password(len = 12)
      @password = Ixtlan::Passwords.generate(len)
      attribute_set(:hashed_password, Ixtlan::Digest.ssha(@password, "--#{Time.now}--#{login}--"))
      @password
    end

    def self.authenticate(login, password)
      user = first(:login => login) # need to get the salt
      if user
        digest = Base64.decode64(user.inspect.sub(/.*hashed_password=..SSHA./, "").sub(/\".*/,''))
        salt = digest[20,147]
        if ::Digest::SHA1.digest("#{password}" + salt) == digest[0,20]
          @logger ||= UserLogger.new(self)
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
        @groups = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Ixtlan::Group), [])
        Ixtlan::GroupUser.all(:memberuid => login).each do |gu| 
          @groups << gu.group
        end
        def @groups.user=(user)
          @user = user
        end
        @groups.user = self
        def @groups.<<(group)
          group.locales(@user)
          unless member? group
            gu = Ixtlan::GroupUser.create(:memberuid => @user.login, :gidnumber => group.id)
            super
          end
          
          self
        end
      
        def @groups.delete(group) 
          gu = Ixtlan::GroupUser.first(:memberuid => @user.login, :gidnumber => group.id)
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
    
    alias :to_x :to_xml_document
    def to_xml_document(opts={}, doc = nil)
      opts.merge!({:exclude => [:hashed_password], :methods => [:groups]})
      to_x(opts, doc)
    end
  end
end
