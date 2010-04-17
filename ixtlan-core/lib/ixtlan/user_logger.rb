require 'slf4r'
module Ixtlan
  
  class UserLogger
    
    def initialize(arg)
      @logger = Slf4r::LoggerFacade.new(arg)
    end

    private 

    def login_from(controller)
      user = controller.respond_to?(:current_user) ? controller.send(:current_user) : nil
      user.nil? ? nil: user.login
    end

    public 

    def log(controller, message = nil, &block)
      log_user(login_from(controller), message, &block)
    end

    def log_action(controller, message = nil)
      log_user(login_from(controller)) do
        as_xml = controller.response.content_type == 'application/xml' ? " - xml" : ""
        if controller.params[:controller]
          audits = controller.instance_variable_get("@#{controller.params[:controller].to_sym}")
          if(audits)
            "#{controller.params[:controller]}##{controller.params[:action]} #{audits.model.to_s.plural}[#{audits.size}]#{as_xml}#{message}"
          else
            audit = controller.instance_variable_get("@#{controller.params[:controller].singular.to_sym}")
            if(audit)
              errors = if(audit.respond_to?(:errors) && !audit.errors.empty?)
                         " - errors: " + audit.errors.full_messages.join(", ") 
                       end
              "#{controller.params[:controller]}##{controller.params[:action]} #{audit.model}(#{audit.key})#{as_xml}#{message}#{errors}"
            else
              "#{controller.params[:controller]}##{controller.params[:action]}#{as_xml}#{message}"
            end
          end
        else
          "params=#{controller.params.inspect}"
        end
      end
    end
     
    def log_user(user, message = nil, &block)
      user = "???" unless user
      @logger.info {"[#{user}] #{message}#{block.call if block}" } 
    end
  end
end
