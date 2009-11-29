module Ixtlan
  module Models
    class Group

      include DataMapper::Resource
      
      def self.default_storage_name
        "Group"
      end

      property :id, Serial, :field => "gidnumber"
      
      property :name, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 32, :field => "cn", :unique_index => true
      
      timestamps :created_at
      
      modified_by User.to_s

      def locales(user = nil)
#        return ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Locale), [])
        if @locales.nil? or not user.nil?
          
          # TODO spec the empty array to make sure new relations are stored
          # in the database or the locales collection is empty before filling it
          @locales = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Locale), [])

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
              GroupLocaleUser.create(:group_id => @group.id, :user_id => @user.id, :locale_code => locale.code)
              super
            end
            
            self
          end
          def @locales.delete(locale) 
            glu = GroupLocaleUser.first(:group_id => @group.id, :user_id => @user.id, :locale_code => locale.code)
            if glu
              glu.destroy
            end
            super
          end
        end
        @locales
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
