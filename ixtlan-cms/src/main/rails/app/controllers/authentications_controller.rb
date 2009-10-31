class AuthenticationsController < ApplicationController

  skip_before_filter :guard
  
  protected
  def login_from_params
    auth = params[:authentication]
    User.authenticate(auth[:login], auth[:password])
  end

  public
  def create
    render_successful_login
  end

  def destroy
    authentication_logger.log_user(current_user.nil? ? nil : current_user.login, "already logged out")
    session.clear
    head :ok
  end
end
