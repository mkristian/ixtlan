# Filters added to this controller apply to all controllers in the application.
# Likewise, all the methods added will be available for all controllers.
class ApplicationController < ActionController::Base
  #helper :all # include all helpers, all the time
  protect_from_forgery # See ActionController::RequestForgeryProtection for details

  # Scrub sensitive parameters from your log
  filter_parameter_logging :login, :username, :password

  before_filter :check_session, :authentication, :guard, :locale

  skip_before_filter :verify_authenticity_token

  include UnrestfulAuthentication

  include SessionTimeout 

  # helper to use authentication token only after being logged in
  def authentication
    login_required and 
      (params["commit"] == "Login" or verify_authenticity_token) 
  end

  def session_expired
    flash[:notice] = "session expired"
  end

  def new_session_timeout
    logger.debug "#{Configuration.instance.session_idle_timeout.minutes.from_now}"
    Configuration.instance.session_idle_timeout.minutes.from_now
  end

  def login
    repository(:single_sign_on) do
      token = ClientToken.create(:login => params[:username], 
                                 :password => params[:password])
      if token.user
        token
      end
    end
  end

  def locale
    if current_user 
      I18n.locale = current_user.language
    end
  end
end
