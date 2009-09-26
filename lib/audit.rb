require 'ixtlan/audit'

::ActionController::Base.send(:include, Ixtlan::AuditBase)
