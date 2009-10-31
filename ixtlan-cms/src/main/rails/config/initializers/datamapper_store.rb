# init a session store which uses a memory cache and drops the user object 
# and the flash which results into a very thin session and hardly any 
# database updates !
# cleanup can be a problem. jruby uses soft-references for the cache so
# memory cleanup with jruby is no problem.
require 'ixtlan/session'
ActionController::Base.session_store = :datamapper_store
ActionController::Base.session = {
  :cache       => true,
  :session_class => Ixtlan::Session
}
