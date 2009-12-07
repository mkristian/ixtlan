module Ixtlan
  module Rails
    module AuditBase
      def self.included(base)
        base.append_after_filter(Audit)
      end
    end

    class Audit

      def self.logger
        @logger ||= UserLogger.new(self)
      end

      def self.filter(controller)
        logger.log_action(controller)
      end
    end
  end
end

::ActionController::Base.send(:include, Ixtlan::Rails::AuditBase)
