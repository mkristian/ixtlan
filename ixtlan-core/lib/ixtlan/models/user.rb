require 'digest/sha1'
require 'base64'
require 'ixtlan/modified_by'
require 'dm-serializer'
require 'ixtlan/models/update_children'
module Ixtlan
  module Models
    class User
      include DataMapper::Resource
      include UpdateChildren

      GROUP = Object.full_const_get(Models::GROUP)

      def self.default_storage_name
        "User"
      end

      property :id, Serial, :field => "uidnumber"

      property :login, String, :required => true , :length => 4..32, :index => :unique_index, :format => /^[a-zA-z0-9]*$/, :writer => :private, :field => "uid"

      property :name, String, :required => true, :format => /^[^<">]*$/, :length => 2..64, :field => "cn"

      property :email, String, :required => true, :format => :email_address, :required => true, :length => 8..64, :index => :unique_index, :field => "mail"

      property :language, String, :required => false, :format => /[a-z][a-z]/, :length => 2, :field => "preferredlanguage"

      property :hashed_password, String, :required => false, :length => 128, :accessor => :private, :field => "userpassword"

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
            user
          else
            "wrong password"
          end
        else
          "unknown login"
        end
      end

      def self.first_or_get!(id_or_login)
        first(:login => id_or_login) || get!(id_or_login)
      end

      def self.first_or_get(id_or_login)
        first(:login => id_or_login) || get(id_or_login)
      end

      def groups
        if @groups.nil?
          # TODO spec the empty array to make sure new relations are stored
          # in the database or the groups collection is empty before filling it
          @groups = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, GROUP), [])
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
        attribute_set(:login, new_login) if login.nil?
      end

      def root?
        groups.detect { |g| g.root? } || false
      end

      def locales_admin?
        groups.detect { |g| g.locales_admin? } || false
      end

      def update_all_children(new_groups, actor)
        if actor.root?
          # root has no restrictions
          update_children(new_groups, :groups)
          update_locales(new_groups)
        else
          update_restricted_children(new_groups, :groups, actor.groups)
          if actor.locales_admin?
            # locales admin can use all locales
            update_locales(new_groups)
          else
            update_restricted_locales(new_groups, actor.groups)
          end
        end
      end

      def update_locales(new_groups)
        if(new_groups)
          # make sure we have an array
          new_groups = new_groups[:group]
          new_groups = [new_groups] unless new_groups.is_a? Array

          # create a map group_id =>  group
          user_groups_map = {}
          groups.each { |g| user_groups_map[g.id.to_s] = g }

          # for each new groups update the locales respectively
          new_groups.each do |group|
            user_groups_map[group[:id]].update_children(group[:locales], :locales) if user_groups_map.key?(group[:id])
          end
        end
      end

      def update_restricted_locales(new_groups, ref_groups)
        if(new_groups)
          # make sure we have an array
          new_groups = new_groups[:group]
          new_groups = [new_groups] unless new_groups.is_a? Array

          # create a map group_id =>  group
          user_groups_map = {}
          groups.each { |g| user_groups_map[g.id.to_s] = g }
          # create a map group_id =>  group for the reference group
          ref_group_locales = {}
          ref_groups.each { |g| ref_group_locales[g.id.to_s] = g.locales }

          # for each new groups update the locales respectively
          new_groups.each do |group|
            user_groups_map[group[:id]].update_restricted_children(group[:locales], :locales, ref_group_locales[group[:id]] || []) if user_groups_map.key?(group[:id])
          end
        end
      end

      if protected_instance_methods.find {|m| m == 'to_x'}.nil?
        alias :to_x :to_xml_document

        protected

        def to_xml_document(opts={}, doc = nil)
          unless(opts[:methods] || opts[:exclude])
            opts.merge!({:exclude => [:hashed_password], :methods => [:groups]})
          end
          to_x(opts, doc)
        end
      end
    end
  end
end
