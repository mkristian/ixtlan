module Ixtlan
  module Controllers
    module AuthenticationsController

      def self.included(base)
        base.skip_before_filter :guard
        base.skip_before_filter :authenticate, :only => :destroy

        # do not want to expose permissions settings on filesystem cache
        base.cache_headers :private
      end

      private

      USER = Object.full_const_get(::Ixtlan::Models::USER)

      protected
      def login_from_params
        auth = params[:authentication]
        USER.authenticate(auth[:login], auth[:password]) if auth
      end

      public
      def show
        render_successful_login
      end

      def create
        render_successful_login
      end

      def destroy
        authentication_logger.log_user(current_user.nil? ? nil : current_user.login, "logged out")
        session.clear
        head :ok
      end

    end
  end
end
