require 'ixtlan/guard'

ActionController::Base.send(:include, Ixtlan::ActionController::Guard)
ActionController::Base.send(:before_filter, :guard)
ActionView::Base.send(:include, Ixtlan::Allowed)
module Erector
  class Widget
    include Ixtlan::Allowed
  end
end
