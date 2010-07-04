require 'dm-core'
require 'ixtlan/modified_by'
require 'dm-serializer'
require 'ixtlan/models/update_children'
module Ixtlan
  module Models
    module Configuration

      unless const_defined? "LOCALE"
        LOCALE = Object.full_const_get(Models::LOCALE)
      end

      def self.included(model)
        model.send(:include, DataMapper::Resource)
        model.send(:include, UpdateChildren)

        model.property :id, ::DataMapper::Types::Serial

        model.property :session_idle_timeout, Integer, :required => true

        model.property :keep_audit_logs, Integer, :required => true

        model.property :password_sender_email, String, :format => :email_address, :required => false, :length => 64

        model.property :login_url, String, :required => false, :length => 128

        model.property :notification_sender_email, String, :format => :email_address, :required => false, :length => 64

        model.property :notification_recipient_emails, String, :format => Proc.new { |email| emails = email.split(','); emails.find_all { |e| e =~ DataMapper::Validate::Format::Email::EmailAddress }.size == emails.size}, :required => false, :length => 254 #honour mysql max varchar length

        model.property :errors_dump_directory, String, :required => false, :length => 192
        model.property :logfiles_directory, String, :required => false, :length => 192

        model.timestamps :updated_at

        model.modified_by ::Ixtlan::Models::USER, :updated_by

        model.has model.n, :locales, :model => ::Ixtlan::Models::LOCALE, :through => DataMapper::Resource

        model.class_eval <<-EOS, __FILE__, __LINE__
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
EOS
      end
    end
  end
end
