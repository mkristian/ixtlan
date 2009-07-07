#require 'datamapper4rails/datamapper_store'
ActionController::Base.session = { :key => "_client_session" , :cache => true }
#ActionController::Base.session_store = :datamapper_store
