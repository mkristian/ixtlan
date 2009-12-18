module Ixtlan
  module Controllers
    module PermissionsController

      def index
        render :xml => Ixtlan::Guard.export_xml
      end

    end
  end
end
