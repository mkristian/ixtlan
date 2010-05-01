module Ixtlan
  module Controllers
    module PermissionsController

      def self.included(base)
        # TODO the authenticate should NOT be there, i.e. it leaks too much info
        base.skip_before_filter :authenticate, :guard
      end

      def index
        render :xml => Ixtlan::Guard.export_xml
      end

    end
  end
end
