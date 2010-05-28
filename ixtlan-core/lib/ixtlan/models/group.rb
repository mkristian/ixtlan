require 'dm-serializer'
require 'ixtlan/models/update_children'
module Ixtlan
  module Models
    module Group
      unless const_defined? "LOCALE"
        LOCALE = Object.full_const_get(Models::LOCALE)
        DOMAIN = Object.full_const_get(Models::DOMAIN)
      end

      def self.included(model)
        model.send(:include, DataMapper::Resource)
        model.send(:include, UpdateChildren)

        model.property :id, ::DataMapper::Types::Serial, :field => "gidnumber"

        model.property :name, String, :required => true , :format => /^[^<'&">]*$/, :length => 32, :field => "cn", :unique_index => true

        model.timestamps :created_at

        model.modified_by Ixtlan::Models::USER, :created_by

        model.class_eval <<-EOS, __FILE__, __LINE__
      if protected_instance_methods.find {|m| m == 'to_x'}.nil?

        protected

        alias :to_x :to_xml_document
        def to_xml_document(opts, doc = nil)
          opts.merge!({:methods => [:locales, :domains], :exclude => [:created_by_id]})
          to_x(opts, doc)
        end
      end

      def self.first_or_get!(id_or_name)
        first(:name => id_or_name) || get!(id_or_name)
      end

      def self.first_or_get(id_or_name)
        first(:name => id_or_name) || get(id_or_name)
      end
EOS
    end

    def locales(user = nil)
      if @locales.nil? or not user.nil?

        # TODO spec the empty array to make sure new relations are stored
        # in the database or the locales collection is empty before filling it
        @locales = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, LOCALE), [])

        return @locales if user.nil?

        GroupLocaleUser.all(:group_id => id, :user_id => user.id).each{ |glu| @locales << glu.locale }
        def @locales.group=(group)
          @group = group
        end
        @locales.group = self
        def @locales.user=(user)
          @user = user
        end
        @locales.user = user
        def @locales.<<(locale)
          unless member? locale
            GroupLocaleUser.create(:group_id => @group.id, :user_id => @user.id, :locale_id => locale.id)
            super
          end

          self
        end
        def @locales.delete(locale)
          glu = GroupLocaleUser.first(:group_id => @group.id, :user_id => @user.id, :locale_id => locale.id)
          if glu
            glu.destroy
          end
          super
        end
      end
      @locales
    end

    def domains(user = nil)
      if @domains.nil? or not user.nil?

        # TODO spec the empty array to make sure new relations are stored
        # in the database or the domains collection is empty before filling it
        @domains = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, DOMAIN), [])

        return @domains if user.nil?

        DomainGroupUser.all(:group_id => id, :user_id => user.id).each{ |glu| @domains << glu.domain }
        def @domains.group=(group)
          @group = group
        end
        @domains.group = self
        def @domains.user=(user)
          @user = user
        end
        @domains.user = user
        def @domains.<<(domain)
          unless member? domain
            DomainGroupUser.create(:group_id => @group.id, :user_id => @user.id, :domain_id => domain.id)
            super
          end

          self
        end
        def @domains.delete(domain)
          glu = DomainGroupUser.first(:group_id => @group.id, :user_id => @user.id, :domain_id => domain.id)
          if glu
            glu.destroy
          end
          super
        end
      end
      @domains
    end

    def root?
      attribute_get(:name) == 'root'
    end

    def locales_admin?
      attribute_get(:name) == 'locales'
    end

    def domains_admin?
      attribute_get(:name) == 'domains'
    end
  end
end
end
