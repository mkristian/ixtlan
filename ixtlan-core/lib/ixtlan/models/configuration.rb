require 'dm-core'
require 'ixtlan/modified_by'
require 'dm-serializer'
module Ixtlan
  module Models
    class Configuration
      include DataMapper::Resource

      def self.default_storage_name
        "Configuration"
      end

      property :id, Serial

      property :session_idle_timeout, Integer, :required => true
      
      property :keep_audit_logs, Integer, :required => true 
      
      property :password_sender_email, String, :format => :email, :required => false, :length => 64

      property :notification_sender_email, String, :format => :email, :required => false, :length => 64

      property :notification_recipient_emails, String, :format => Proc.new { |email| emails = email.split(','); emails.find_all { |e| e =~ DataMapper::Validate::Format::Email::EmailAddress }.size == emails.size}, :required => false, :length => 254 #honour mysql max varchar length
      
      property :errors_dump_directory, String, :required => false, :length => 192
      property :logfiles_directory, String, :required => false, :length => 192

      timestamps :updated_at

      modified_by ::Ixtlan::Models::USER, :updated_by

      def locales
        if @locales.nil? 
          # TODO spec the empty array to make sure new relations are stored
          # in the database or the locales collection is empty before filling it
          @locales = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Object.full_const_get(::Ixtlan::Models::LOCALE)), [])
p ConfigurationLocale.all(:configuration_id => id)
          ConfigurationLocale.all(:configuration_id => id).each{ |cl| @locales << cl.locale }
          def @locales.configuration=(configuration)
            @configuration = configuration
          end
          @locales.configuration = self
          def @locales.<<(locale)
            unless member? locale
              ConfigurationLocale.create(:configuration_id => @configuration.id, :locale_id => locale.id)
              super
            end
            
            self
          end
          
          def @locales.delete(locale) 
            cl = ConfigurationLocale.first(:configuration_id => @configuration.id, :locale_id => locale.id)
            if cl
              cl.destroy
            end
            super
          end
        end
p @locales
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
        unless(opts[:methods] || opts[:exclude])
          opts.merge!({:methods => [:updated_by, :locales], :exclude => [:updated_by_id, :id]})
        end
        to_x(opts, doc)
      end
    end
  end
end
