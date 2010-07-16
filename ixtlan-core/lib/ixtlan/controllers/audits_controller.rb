module Ixtlan
  module Controllers
    module AuditsController
      
      def self.included(base)
        base.send(:include, Ixtlan::Controllers::SearchQuery)

        base.cache_headers :protected, false #no_store == false
      end

      public

      # GET /audits
      # GET /audits.xml
      def index
        @audits = Audit.all(query(:login, params[:query])).reverse + Audit.all(query(:message, params[:query])).reverse

        respond_to do |format|
          format.html
          format.xml  { render :xml => @audits }
        end
      end
    end
  end
end
