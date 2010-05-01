module Ixtlan
  module Controllers
    module AuthenticationsController

      def self.included(base)
        base.skip_before_filter :guard
        base.skip_before_filter :authenticate, :only => :destroy
      end

      protected
      def login_from_params
        auth = params[:authentication]
        User.authenticate(auth[:login], auth[:password])
      end

      public
      def create
        render_successful_login
      end

      def destroy
        authentication_logger.log_user(current_user.nil? ? nil : current_user.login, "already logged out")
        session.clear
        head :ok
      end

    end
  end
end
