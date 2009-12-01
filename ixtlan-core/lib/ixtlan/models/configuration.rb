require 'dm-core'
require 'ixtlan/modified_by'
module Ixtlan
  module Models
    class Configuration
      include DataMapper::Resource

      def self.default_storage_name
        "Configuration"
      end

      property :id, Serial

      property :session_idle_timeout, Integer, :nullable => false
      
      property :keep_audit_logs, Integer, :nullable => false 
      
      property :notification_sender_email, String, :format => :email, :nullable => true 

      property :notification_recipient_email, String, :format => :email, :nullable => true 
      
      timestamps :updated_at

      modified_by User.to_s, :updated_by

      def locales
        if @locales.nil? 
          # TODO spec the empty array to make sure new relations are stored
          # in the database or the locales collection is empty before filling it
          @locales = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Locale), [])
          ConfigurationLocale.all(:configuration_id => id).each{ |cl| @locales << cl.locale }
          def @locales.configuration=(configuration)
            @configuration = configuration
          end
          @locales.configuration = self
          def @locales.<<(locale)
            unless member? locale
              ConfigurationLocale.create(:configuration_id => @configuration.id, :locale_code => locale.code)
              super
            end
            
            self
          end
          
          def @locales.delete(locale) 
            cl = ConfigurationLocale.first(:configuration_id => @configuration.id, :user_id => @user.id, :locale_code => locale.code)
            if cl
              cl.destroy
            end
            super
          end
        end
        @locales
      end

      def self.instance
        # HACK: return a new object in case there is none in the database
        # to allow rails rake tasks to work with an empty database
        get(1) || new(:id => 1, :session_idle_timeout => 5, :keep_audit_logs => 7, :current_user => User.first)
      end

      alias :to_x :to_xml_document
      def to_xml_document(opts, doc = nil)
        opts.merge!({:methods => [:updated_by, :locales]})
        to_x(opts, doc)
      end
    end
  end
end
