require 'ixtlan/unrestful_authentication'

ActionController::Base.send(:include, Ixtlan::UnrestfulAuthentication)
ActionController::Base.send(:prepend_before_filter, :authenticate)
