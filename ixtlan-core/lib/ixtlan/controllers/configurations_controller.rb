module Ixtlan
  module Controllers
    module ConfigurationsController

      # GET /configuration
      # GET /configuration.xml
      def show
        @configuration = Configuration.instance

        respond_to do |format|
          format.html # show.html.erb
          format.xml  { render :xml => @configuration }
        end
      end

      # GET /configuration/edit
      def edit
        @configuration = Configuration.instance
      end

      # PUT /configuration
      # PUT /configuration.xml
      def update
        @configuration = Configuration.instance
        @configuration.current_user = current_user

        locales =  params[:configuration].delete(:locales)
        respond_to do |format|
          if @configuration.update(params[:configuration]) or not @configuration.dirty?
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
