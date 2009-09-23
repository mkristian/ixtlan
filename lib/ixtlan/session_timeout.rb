module Ixtlan
  module SessionTimeout

    def check_session_expiry
      if !session[:expiry_time].nil? and session[:expiry_time] < Time.now
        # Session has expired.
        expire_session
      else
        # Assign a new expiry time
        session[:expiry_time] = new_session_timeout
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

    def expire_session
      session[:expiry_time] = nil
      session[:session_ip] = nil
      reset_session
      render_session_timeout
      return false
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
        flash[:notice] = "session timeout" unless flash[:notice]
        format.html {  render :template => "sessions/login" }
        format.xml { head :unauthorized }
      end
    end

    def new_session_timeout
      30.minutes.from_now
    end

  end
end
