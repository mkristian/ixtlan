module Ixtlan
  module Controllers
    module ConfigurationsController

      def self.included(base)
        base.cache_headers :protected
      end

      private

      CONFIGURATION = Object.full_const_get(::Ixtlan::Models::CONFIGURATION)
      LOCALE = Object.full_const_get(::Ixtlan::Models::LOCALE)

      public

      # GET /configuration
      # GET /configuration.xml
      def show
        @configuration = CONFIGURATION.instance
        respond_to do |format|
          format.html # show.html.erb
          format.xml  { render :xml => @configuration }
        end
      end

      # GET /configuration/edit
      def edit
        @configuration = CONFIGURATION.instance
      end

      # PUT /configuration
      # PUT /configuration.xml
      def update
        @configuration = CONFIGURATION.instance
        @configuration.current_user = current_user

        locales = params[:configuration].delete(:locales)
        @configuration.update_children(locales, :locales)
        @configuration.attributes = params[:configuration]

        respond_to do |format|
          if @configuration.save() or not @configuration.dirty?
            flash[:notice] = 'Configuration was successfully updated.'
            format.html { redirect_to(configuration_url) }
            format.xml  { render :xml => @configuration }
          else
            format.html { render :action => "edit" }
            format.xml  { render :xml => @configuration.errors, :status => :unprocessable_entity }
          end
        end
      end
    end
  end
end
