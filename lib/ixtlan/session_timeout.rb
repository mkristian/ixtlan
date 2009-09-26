module Ixtlan
  module SessionTimeout
    
    private

    def expire_session
      @session_user_logger ||= UserLogger.new(Ixtlan::SessionTimeout)
      @session_user_logger.log(self, "session timeout")
      session.clear
#      reset_session
      render_session_timeout
      return false
    end

    public

    def check_session_expiry
      if !session[:expires_at].nil? and session[:expires_at] < DateTime.now
        # Session has expired.
        expire_session
      else
        # Assign a new expiry time
        session[:expires_at] = new_session_timeout
        return true
      end
    end

    def check_session_ip_binding
      if !session[:session_ip].nil? and session[:session_ip] != request.headers['REMOTE_ADDR']
        # client IP has changed
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

    # def check_session_browser_signature_binding
    #     if !session[:session_browser_signature].nil? and session[:session_browser_signature] != "something"
    #       # browser signature has changed
    #       reset_session
    #       session_expired
    #       return false
    #     else
    #       # Assign a browser signature
    #       session[:session_browser_signature] = ""
    #       return true
    #     end
    #   end

    
    def render_session_timeout
      respond_to do |format| 
        format.html { 
          @notice = "session timeout" unless @notice
          render :template => "sessions/login"
        }
        format.xml { head :unauthorized }
      end
    end

    def new_session_timeout
      30.minutes.from_now
    end

  end
end
