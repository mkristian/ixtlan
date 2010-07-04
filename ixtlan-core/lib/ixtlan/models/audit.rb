require 'dm-serializer'
module Ixtlan
  module Models
    module Audit
      def self.included(model)
        model.send(:include, DataMapper::Resource)

        model.property :id, ::DataMapper::Types::Serial

        model.property :date, DateTime, :required => true

        model.property :login, String, :required => true, :format => /^[a-zA-Z0-9]+$/, :length => 32, :auto_validation => false #required => true does not allow empty string :-( so skip validation

        model.property :message, String, :required => true, :length => 255

        model.class_eval <<-EOS, __FILE__, __LINE__
      unless const_defined? "CONFIGURATION"
        CONFIGURATION = Object.full_const_get(Models::CONFIGURATION)
      end
      def self.pop_all
        result = Thread.current[:audit] || []
        Thread.current[:audit] = nil
        if(!@last_cleanup.nil? && @last_cleanup < 1.days.ago)
          @last_cleanup = Date.today
          begin
            self.class.all(:date.lt => CONFIGURATION.keep_audit_log.days.ago).destroy!
          rescue Error
            # TODO log this !!
          end
        end
        result
      end
EOS
      end

      def push
        list = (Thread.current[:audit] ||= [])
        list << self
      end
    end
  end
end
  
