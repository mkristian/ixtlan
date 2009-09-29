require 'dm-core'
require 'ixtlan/modified_by'
module Ixtlan
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

    modified_by Ixtlan::User, :updated_by

    def self.instance
      # HACK return a new object in case there is none in the database
      # to allow rails rake tasks to work with empty database
      @instance ||= get(1) || new(:id => 1, :session_idle_timeout => 5, :keep_audit_logs => 7, :current_user => Ixtlan::User.first)
    end
  end
end
