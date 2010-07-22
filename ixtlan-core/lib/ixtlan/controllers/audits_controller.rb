module Ixtlan
  module Controllers
    module AuditsController
      
      def self.included(base)
        base.send(:include, Ixtlan::Controllers::SearchQuery)

        base.cache_headers :protected, false #no_store == false
      end

      private

      AUDIT = Object.full_const_get(::Ixtlan::Models::AUDIT)

      public

      # GET /audits
      # GET /audits.xml
      def index
        # limit all queries
        @audits = query_limit_all(AUDIT, :login, :message).reverse

        respond_to do |format|
          format.html
          format.xml  { render :xml => @audits }
        end
      end
    end
  end
end
