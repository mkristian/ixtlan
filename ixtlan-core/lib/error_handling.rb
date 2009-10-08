require 'ixtlan/error_handling'

ActionController::Base.send(:include, Ixtlan::ErrorHandling)
