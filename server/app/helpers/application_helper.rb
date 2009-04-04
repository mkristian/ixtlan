# Methods added to this helper will be available to all templates in the application.
module ApplicationHelper
  def dummy_user?
    session[:user_id] == -1 #and session[:user].new_record?
  end
end
