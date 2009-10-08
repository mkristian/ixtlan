require 'ixtlan/session_timeout'

ActionController::Base.send(:include, Ixtlan::SessionTimeout)
