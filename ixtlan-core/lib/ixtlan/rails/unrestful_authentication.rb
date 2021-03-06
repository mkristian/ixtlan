
module Ixtlan
module Rails
  module UnrestfulAuthentication

    private

    USER = Object.full_const_get(Ixtlan::Models::USER)

    AUTHENTICATION = Object.full_const_get(Ixtlan::Models::AUTHENTICATION)

    def authentication_logger
      @authentication_logger ||= Ixtlan::UserLogger.new(Ixtlan::Rails::UnrestfulAuthentication)
    end

    protected

    def logged_in?
      !session[:user_id].nil?
    end

    def current_user
      session[:user] = login_from_session if logged_in? and session[:user].nil?
      session[:user]
    end

    def current_user=(new_user)
      session[:user_id] = new_user ? new_user.id : nil
      session[:user] = new_user
    end

    def verify_authenticity
      if request.content_type == 'application/xml'
        params[request_forgery_protection_token] = request.headers[:authenticity_token]
      end
      verify_authenticity_token if logged_in?
    end

    def authenticate
      if logged_in?
        case(request.method)
        when :delete
          logout
        else
          true
        end
      else
        case(request.method)
        when :get
          session.clear
          render_login_page
        when :post
          user = login_from_params || "no username or password"
          if user.instance_of? String
            authentication_logger.log_user(params[:login] || (params[:authentication] || {})[:login], user + " from IP #{request.headers['REMOTE_ADDR']}")
            session.clear
            render_access_denied
          else
            authentication_logger.log_user(user.nil? ? nil : user.login, "logged in")
            session.clear
            # reset session
            self.current_user = user
            render_successful_login
          end
        else
          session.clear
          render_access_denied
        end
        false
      end
    end

    def login_from_params
      USER.authenticate(params[:login], params[:password])
    end

    def login_from_session
      USER.get(session[:user_id])
    end

    def logout
      if(params[:login] == current_user.login && request.content_type != 'application/xml')
        authentication_logger.log_user(current_user.login, "logged out")
        current_user = nil
        session.clear
        # reset session
        render_logout_page
        false
      else
        true
      end
    end

    def render_successful_login
      respond_to do |format|
        format.html { redirect_to request.url, :status => :moved_permanently}
        format.xml do
          authentication = AUTHENTICATION.new
          authentication.login = self.current_user.login
          authentication.user = self.current_user
          response.headers["authentication-token"] = form_authenticity_token
          render :xml => authentication.to_xml
        end
      end
    end

    def render_access_denied
      respond_to do |format|
        format.html do
          @notice = "access denied" unless @notice
          render :template => "sessions/login", :status => :unauthorized
        end
        format.xml { head :unauthorized}
      end
    end

    def render_login_page
      respond_to do |format|
        format.html { render :template => "sessions/login" }
        format.xml { head :unauthorized }
      end
    end

    def render_logout_page
      respond_to do |format|
        format.html do
          @notice = "logged out" unless @notice
          render :template => "sessions/login"
        end
        format.xml { head :ok }
      end
    end
  end
end
end

ActionController::Base.send(:include, Ixtlan::Rails::UnrestfulAuthentication)
ActionController::Base.send(:prepend_before_filter, :authenticate)
