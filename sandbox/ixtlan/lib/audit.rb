# Audit
module Audit
  module Base
    def self.included(base)
      base.append_after_filter(AuditFilter)
    end
  end
  
  class AuditFilter
    
    @logger = Slf4r::LoggerFacade.new(self)

    @logger.info("audit logger initialized ...")

    def self.filter(controller)
      user = controller.send(:current_user)
      user = user ? user.login : "???"
      @logger.info {"[#{user}] #{controller.params[:controller]}##{controller.params[:action]}: #{controller.send(:audit)}" }
    end
  end
end


::ActionController::Base.send(:include, Audit::Base)
