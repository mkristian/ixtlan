module Ixtlan
  module Rails
    module SessionTimeout

      def self.included(base)
        base.send(:include, InstanceMethods)
      end
      
      module InstanceMethods
        private

        CONFIG = Object.full_const_get(Ixtlan::Models::CONFIGURATION)

        def session_user_logger
          @session_user_logger ||= UserLogger.new(Ixtlan::Rails::SessionTimeout)
        end
        
        def expire_session
          session.clear
          #      reset_session
          render_session_timeout
          return false
        end
        
        public
        
        def check_session_expiry
          if !session[:expires_at].nil? and session[:expires_at] < DateTime.now
            # Session has expired.
            session_user_logger.log(self, "session timeout")
            expire_session
          else
            # Assign a new expiry time
            session[:expires_at] = session_timeout.minutes.from_now
            return true
          end
        end
        
        def check_session_ip_binding
          if !session[:session_ip].nil? and session[:session_ip] != request.headers['REMOTE_ADDR']
            # client IP has changed
            session_user_logger.log(self, "IP changed from #{session[:session_ip]} to #{request.headers['REMOTE_ADDR']}")
            expire_session
          else
            # Assign client IP 
            session[:session_ip] = request.headers['REMOTE_ADDR']
            return true
          end
        end
        
        def check_session
          check_session_ip_binding and check_session_expiry
        end
        
        def check_session_browser_signature
          if !session[:session_browser_signature].nil? and session[:session_browser_signature] != retrieve_browser_signature
            # browser signature has changed
            session_user_logger.log(self, "browser signature changed from #{session[:session_browser_signature]} to #{retrieve_browser_signature}")
            expire_session
            return false
          else
            # Assign a browser signature
            session[:session_browser_signature] = retrieve_browser_signature
            return true
          end
        end
        
        def retrieve_browser_signature
          [request.headers['HTTP_USER_AGENT'],
           request.headers['HTTP_ACCEPT_LANGUAGE'],
           request.headers['HTTP_ACCEPT_ENCODING'],
           request.headers['HTTP_ACCEPT']].join('|')
        end
        
        def render_session_timeout
          respond_to do |format| 
            format.html { 
              @notice = "session timeout" unless @notice
              render :template => "sessions/login"
            }
            format.xml { head :unauthorized }
          end
        end
        
        def session_timeout
          CONFIG.instance.session_idle_timeout
        end
      end
    end
  end
end

ActionController::Base.send(:include, Ixtlan::Rails::SessionTimeout)
