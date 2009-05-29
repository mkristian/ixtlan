# Filters added to this controller apply to all controllers in the application.
# Likewise, all the methods added will be available for all controllers.

class ApplicationController < ActionController::Base

  private 

  include ApplicationHelper

  # helper :all # include all helpers, all the time
  protect_from_forgery # See ActionController::RequestForgeryProtection for details

  # Scrub sensitive parameters from your log
  filter_parameter_logging :password, :login, :username, :password_confirmation

  before_filter :reset_password_mode, :check_session, :authentication, :guard

  skip_before_filter :verify_authenticity_token

  include UnrestfulAuthentication

  include SessionTimeout 

  include CacheHeaders

  # only browser can cache, no persistent storage and no max-age
  def cache_headers
    # max-age = 0, no-store = true
    only_browser_can_cache(true, 0)
  end

  def reset_password_mode
    @message = ::Base64.decode64(params[:flash] || "===")
    if session[:reset_password_mode]
      user = current_user
      def user.groups
        []
      end
    end
  end

  # overwrite methods from session_timeout
  def session_expired
    flash[:notice] = "session expired"
  end

  def new_session_timeout
    logger.debug "timeout at #{Configuration.instance.session_idle_timeout.minutes.from_now}"
    Configuration.instance.session_idle_timeout.minutes.from_now
  end

  # helper to use authentication token only after being logged in
  def authentication
    if dummy_user?
      reset_session
    end
    login_required and 
      (params["commit"] == "Login" or verify_authenticity_token) 
  end

  def create_dummy_user
    dummy = User.new(:id => -1, :login => @reset_password.user.login)
    def dummy.roles
      []
    end
    session[:user_id] = dummy.id
    session[:user] = dummy
  end

  def login
    repository(:single_sign_on) do
      token = SingleSignOn.new(:ip => request.ip)
      token.user = User.authenticate(params[:username], params[:password])
      if token.user
        token.save 
        token
      end
    end
  end
  
  def login_from_token(token)
    repository(:single_sign_on) do
      sso = SingleSignOn.get(token) || SingleSignOn.first(:one_time => token)
#      puts "login from token"
#      p sso
#      p sso.user
      sso
    end
  end
end
