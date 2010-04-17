require 'ixtlan/user_logger'
module Ixtlan
  module Rails
    module ErrorHandling

      def log_user_error(exception)
        Ixtlan::UserLogger.new(Ixtlan::Rails::ErrorHandling).log_action(self, " - #{exception.class} - #{exception.message}")
        log_error(exception)
      end

      def internal_server_error(exception)
        dump_error(exception, Object.full_const_get(::Ixtlan::Models::CONFIGURATION).instance)
        status = :internal_server_error
        error_page(:internal_server_error, exception) do |exception|
          "internal server error: #{exception.class.name}"
        end
      end

      def error_page(status, exception, &block)
        respond_to do |format| 
          format.html { 
            @notice = block.call(exception)
            if logged_in?
              render :template => "errors/error", :status => status
            else
              render :template => "sessions/login", :status => status
            end
          }
          format.xml { head status }
        end
      end

      def page_not_found(exception)
        log_user_error(exception)
        status = rescue_responses[exception.class.name]
        status = status == :internal_server_error ? :not_found : status
        error_page(status, exception) { "page not found" }
      end

      def stale_resource(exception)
        log_user_error(exception)
        respond_to do |format| 
          format.html { 
            render :template => "errors/stale", :status => :conflict
          }
          format.xml { head :conflict }
        end
      end

      def dump_error(exception, config)
        log_user_error(exception)
        dumper = DumpError.new(config.notification_sender_email, 
                               config.notification_recipient_emails,
                               config.errors_dump_directory)
        dumper.dump(self, exception)
      end
    end
    
    class ErrorNotifier < ActionMailer::Base
      require 'pathname'

      def error_notification(email_from, email_to, exception, error_file)
        @subject    = exception.to_s
        @body       = {:text => "#{error_file}"}
        @recipients = email_to
        @from       = email_from
        @sent_on    = Time.now
        @headers    = {}
        path = Pathname(__FILE__).parent.dirname.to_s
        view_paths << path unless view_paths.member? path
        @template   = "error_notification.rhtml"
      end

    end
    
    class DumpError
      
      def initialize(email_from, email_to, errors_dir = nil)
        errors_dir ||= "#{RAILS_ROOT}/log/errors"
        FileUtils.mkdir_p(errors_dir || "#{RAILS_ROOT}/log/errors")
        @errors_dir = Dir.new(errors_dir)
        @email_from = email_from
        @email_to = email_to
      end

      def dump(controller, exception)
        time = Time.now
        error_log_id = "#{time.tv_sec}#{time.tv_usec}"
        
        log_file = File.join(@errors_dir.path.to_s, "error-#{error_log_id}.log")
        logger = Logger.new(log_file)
        
        dump_environment(logger, exception, controller)
        ErrorNotifier.deliver_error_notification(@email_from, @email_to, exception, log_file) unless @email_to.blank?
        log_file
      end 

      private

      def dump_environment_header(logger, header)
        logger.error("\n===================================================================\n#{header}\n===================================================================\n");
      end
      
      def dump_environment(logger, exception, controller)
        dump_environment_header(logger, "REQUEST DUMP");
        dump_hashmap(logger, controller.request.env)
        
        dump_environment_header(logger, "RESPONSE DUMP");
        dump_hashmap(logger, controller.response.headers)
        
        dump_environment_header(logger, "SESSION DUMP");
        dump_hashmap(logger, controller.session)
        
        dump_environment_header(logger, "PARAMETER DUMP");
        map = {}
        dump_hashmap(logger, controller.params.each{ |k,v| map[k]=v })
        
        dump_environment_header(logger, "EXCEPTION");
        logger.error("#{exception.class}:#{exception.message}")
        logger.error("\t" + exception.backtrace.join("\n\t"))
      end

      def dump_hashmap(logger, map)
        for key,value in map
          logger.error("\t#{key} => #{value.inspect}")
        end
      end

    end
  end
end

ActionController::Base.send(:include, Ixtlan::Rails::ErrorHandling)
