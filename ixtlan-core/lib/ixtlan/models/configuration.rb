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
      
      property :notification_sender_email, String, :format => :email, :nullable => true, :length => 64

      property :notification_recipient_emails, String, :format => Proc.new { |email| emails = email.split(','); emails.find_all { |e| e =~ DataMapper::Validate::Format::Email::EmailAddress }.size == emails.size}, :nullable => true, :length => 254 #honour mysql max varchar length
      
      timestamps :updated_at

      modified_by ::Ixtlan::Models::USER, :updated_by

      def locales
        if @locales.nil? 
          # TODO spec the empty array to make sure new relations are stored
          # in the database or the locales collection is empty before filling it
          @locales = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Object.full_const_get(::Ixtlan::Models::LOCALE)), [])
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
        begin
          get(1) || new(:id => 1, :session_idle_timeout => 5, :keep_audit_logs => 7)
        rescue
          auto_migrate!
          new(:id => 1, :session_idle_timeout => 5, :keep_audit_logs => 7)
        end
      end

      alias :to_x :to_xml_document
      def to_xml_document(opts, doc = nil)
        opts.merge!({:methods => [:updated_by, :locales], :exclude => [:updated_by_id, :id]})
        to_x(opts, doc)
      end
    end
  end
end
