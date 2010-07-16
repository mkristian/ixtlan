module Ixtlan
  module Rails
    module RescueModule
      def self.included(controller)
        # needs 'optimistic_persistence'
        controller.rescue_from ::Ixtlan::StaleResourceError, :with => :stale_resource

        # needs 'guard'
        controller.rescue_from ::Ixtlan::GuardException, :with => :page_not_found
        controller.rescue_from ::Ixtlan::PermissionDenied, :with => :page_not_found
        
        # rest is standard rails or datamapper
        controller.rescue_from ::DataMapper::ObjectNotFoundError, :with => :page_not_found
        controller.rescue_from ::ActionController::RoutingError, :with => :page_not_found
        controller.rescue_from ::ActionController::UnknownAction, :with => :page_not_found
        controller.rescue_from ::ActionController::MethodNotAllowed, :with => :page_not_found
        controller.rescue_from ::ActionController::NotImplemented, :with => :page_not_found
        controller.rescue_from ::ActionController::InvalidAuthenticityToken, :with => :stale_resource
        
        # have nice stacktraces in development mode
        unless controller.consider_all_requests_local
          controller.rescue_from ::ActionView::MissingTemplate, :with => :internal_server_error
          controller.rescue_from ::ActionView::TemplateError, :with => :internal_server_error
        end
      end
    end
  end
end

ActionController::Base.send(:include, Ixtlan::Rails::RescueModule)
