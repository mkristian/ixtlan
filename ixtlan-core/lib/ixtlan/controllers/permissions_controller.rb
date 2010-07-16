module Ixtlan
  module Controllers
    module PermissionsController

      def self.included(base)
        base.skip_before_filter :guard

        # do not want to expose permissions settings on filesystem cache
        base.cache_headers :private
      end

      def index
        render :xml => Ixtlan::Guard.export_xml
      end

    end
  end
end
