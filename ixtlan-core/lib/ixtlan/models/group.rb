require 'dm-serializer'
require 'ixtlan/models/update_children'
module Ixtlan
  module Models
    class Group
      include DataMapper::Resource
      include UpdateChildren

      if defined? :LOCALE
        #p LOCALE
        # TODO where is LOCALE defined and remove the double defintion
      end
      LOCALE = Object.full_const_get(Models::LOCALE)

      def self.default_storage_name
        "Group"
      end

      property :id, Serial, :field => "gidnumber"

      property :name, String, :required => true , :format => /^[^<'&">]*$/, :length => 32, :field => "cn", :unique_index => true

      timestamps :created_at

      modified_by Models::USER

      def self.first_or_get!(id_or_name)
        first(:name => id_or_name) || get!(id_or_name)
      end

      def self.first_or_get(id_or_name)
        first(:name => id_or_name) || get(id_or_name)
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

      def root?
        attribute_get(:name) == 'root'
      end

      def locales_admin?
        attribute_get(:name) == 'locales'
      end

      if protected_instance_methods.find {|m| m == 'to_x'}.nil?

        protected

        alias :to_x :to_xml_document
        def to_xml_document(opts, doc = nil)
          opts.merge!({:methods => [:locales]})
          to_x(opts, doc)
        end
      end
    end
  end
end
