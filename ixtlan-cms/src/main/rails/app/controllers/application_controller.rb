# Filters added to this controller apply to all controllers in the application.
# Likewise, all the methods added will be available for all controllers.

class ApplicationController < ActionController::Base
  filter_parameter_logging :password, :login
  before_filter :check_session_expiry

  # override default to use nice User (without namespace)
  def login_from_params
    User.authenticate(params[:login], params[:password])
  end

  def login_from_session
    User.get(session[:user_id])
  end

  # override default to use value from configuration
  def new_session_timeout
    Ixtlan::Configuration.instance.session_idle_timeout.minutes.from_now
  end

  # needs 'optimistic_persistence'
  rescue_from DataMapper::StaleResourceError, :with => :stale_resource

  # needs 'guard'
  rescue_from Ixtlan::GuardException, :with => :page_not_found
  rescue_from Ixtlan::PermissionDenied, :with => :page_not_found

  # rest is standard rails or datamapper
  rescue_from DataMapper::ObjectNotFoundError, :with => :page_not_found
  rescue_from ActionController::RoutingError, :with => :page_not_found
  rescue_from ActionController::UnknownAction, :with => :page_not_found
  rescue_from ActionController::MethodNotAllowed, :with => :page_not_found
  rescue_from ActionController::NotImplemented, :with => :page_not_found

  # have nice stacktraces in development mode
  unless consider_all_requests_local
    rescue_from ActionView::MissingTemplate, :with => :internal_server_error
    rescue_from ActionView::TemplateError, :with => :internal_server_error
  end

  protect_from_forgery # See ActionController::RequestForgeryProtection for details

  # Scrub sensitive parameters from your log
  # filter_parameter_logging :password
end
