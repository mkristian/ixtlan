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
    
    timestamps :at

    modified_by Ixtlan::User

    def self.instance
      @instance ||= get(1) || create(:id => 1, session_idle_timeout => 5, :keep_audit_logs => 7)
    end
  end
end
