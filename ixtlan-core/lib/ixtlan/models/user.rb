require 'digest/sha1'
require 'base64'
require 'ixtlan/modified_by'
require 'ixtlan/passwords'
require 'ixtlan/digest'
require 'dm-serializer'
require 'ixtlan/models/update_children'
require 'ixtlan/models/group_user'
module Ixtlan
  module Models
    module User

      GROUP = Object.full_const_get(Models::GROUP)
      GROUP_USER = Object.full_const_get(Models::GROUP_USER)

      def root?
        groups.detect { |g| g.root? } || false
      end

      def locales_admin?
        groups.detect { |g| g.locales_admin? } || false
      end

      def domains_admin?
        groups.detect { |g| g.domains_admin? } || false
      end

      def password
        @password
      end

      def reset_password(len = 12)
        @password = Ixtlan::Passwords.generate(len)
        attribute_set(:hashed_password, Ixtlan::Digest.ssha(@password, "--#{Time.now}--#{login}--"))
        @password
      end

      def digest
        ::Base64.decode64(@hashed_password[6,200])
      end
      def groups
        if @groups.nil?
          # TODO spec the empty array to make sure new relations are stored
          # in the database or the groups collection is empty before filling it
          @groups = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, GROUP), [])
          GROUP_USER.all(:memberuid => login).each do |gu|
            @groups << gu.group
          end
          def @groups.user=(user)
            @user = user
          end
          @groups.user = self
          def @groups.<<(group)
            group.locales(@user)
            group.domains(@user)
            unless member? group
              gu = GROUP_USER.create(:memberuid => @user.login, :gidnumber => group.id)
              super
            end

            self
          end

          def @groups.delete(group)
            gu = GROUP_USER.first(:memberuid => @user.login, :gidnumber => group.id)
            if gu
              gu.destroy
            end
            super
          end
          @groups.each do |g|
            g.locales(self)
            g.domains(self)
          end
        end
        @groups
      end

      def update_all_children(new_groups)
        if current_user.root?
          # root has no restrictions
          update_children(new_groups, :groups)
          update_locales_domains(new_groups)
        else
          update_restricted_children(new_groups, :groups, current_user.groups)
          if current_user.locales_admin? || current_user.domains_admin?
            # locales/domains admin can use all locales/domains
            update_locales_domains(new_groups)
          end
          if !current_user.locales_admin? || !current_user.domains_admin?
            update_restricted_locales_domains(new_groups, current_user.groups)
          end
        end
      end

      def update_locales_domains(new_groups)
        if(new_groups)
          # make sure we have an array
          new_groups = new_groups.is_a?(Array) ? new_groups : new_groups[:group]
          new_groups = [new_groups] unless new_groups.is_a? Array

          # create a map group_id =>  group
          user_groups_map = {}
          groups.each { |g| user_groups_map[g.id.to_s] = g }

          # for each new groups update the locales/domains respectively
          new_groups.each do |group|
            if user_groups_map.key?(group[:id])
              user_groups_map[group[:id]].update_children(group[:locales], :locales) if current_user.locales_admin? || current_user.root?
              user_groups_map[group[:id]].update_children(group[:domains], :domains) if current_user.domains_admin? || current_user.root?
            end
          end
        end
      end

      def update_restricted_locales_domains(new_groups, ref_groups)
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
          ref_group_domains = {}
          ref_groups.each { |g| ref_group_domains[g.id.to_s] = g.domains }

          # for each new groups update the locales respectively
          new_groups.each do |group|
            if user_groups_map.key?(group[:id])
              user_groups_map[group[:id]].update_restricted_children(group[:locales], :locales, ref_group_locales[group[:id]] || []) unless current_user.locales_admin?
              user_groups_map[group[:id]].update_restricted_children(group[:domains], :domains, ref_group_domains[group[:id]] || []) unless current_user.domains_admin?
            end
          end
        end
      end

      def self.included(model)
        model.send(:include, DataMapper::Resource)
        model.send(:include, UpdateChildren)

        model.property :id, ::DataMapper::Types::Serial, :field => "uidnumber"

        model.property :login, String, :required => true, :field => "uid", :length => 4..32, :unique_index => true, :format => /^[a-zA-Z0-9]*$/, :writer => :private

        model.property :name, String, :required => true, :format => /^[^<">]*$/, :length => 2..64, :field => "cn"

        model.property :email, String, :required => true, :format => :email_address, :required => true, :length => 8..64, :unique_index => true, :field => "mail"

        model.property :language, String, :required => false, :format => /[a-z][a-z]/, :length => 2, :field => "preferredlanguage"

        model.property :hashed_password, String, :required => false, :length => 128, :accessor => :private, :field => "userpassword"

        if !model.const_defined?('LDAP') || !model.const_get('LDAP')
          model.timestamps :at
          model.modified_by ::Ixtlan::Models::USER
        end

        #validates_is_unique  :login
        #validates_is_unique  :email

        model.class_eval <<-EOS, __FILE__, __LINE__
        # make sure login is immutable
        def login=(new_login)
          attribute_set(:login, new_login) if login.nil?
        end

        def self.authenticate(login, password)
          user = first(:login => login) # need to get the salt
          if user
            digest = user.digest
            salt = digest[20,147]
            if ::Digest::SHA1.digest("\#{password}" + salt) == digest[0,20]
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

        if protected_instance_methods.find {|m| m == 'to_x'}.nil?
          alias :to_x :to_xml_document

          protected

          def to_xml_document(opts={}, doc = nil)
            unless(opts[:methods])
              opts.merge!({ 
                            :skip_types => true,
                            :skip_empty_tags => true,
                            :methods => [:groups, :created_by, :updated_by], 
                            :groups => {
                              :exclude => [:created_at, :created_by_id], 
                              :methods => [:locales, :domains], 
                              :locales => {
                                :methods => [], 
                                :exclude => [:created_at]
                              }, 
                              :domains => {
                                :methods => [], 
                                :exclude => [:created_at]
                              }
                            },
                            :created_by => {
                              :methods => [], 
                              :exclude => [:created_at, :updated_at, :hashed_password, :created_by_id, :updated_by_id, :language]
                            },
                            :updated_by => {
                              :methods => [], 
                              :exclude => [:created_at, :updated_at, :hashed_password, :created_by_id, :updated_by_id, :language]
                            }                        
                          })
            end
            unless(opts[:exclude])
              opts.merge!({:exclude => [:hashed_password, :created_by_id, :updated_by_id]})
            end
            to_x(opts, doc)
          end
        end
EOS
      end
    end
  end
end
