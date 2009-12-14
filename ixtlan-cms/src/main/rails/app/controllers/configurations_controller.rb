class ConfigurationsController < ApplicationController

  # GET /configurations
  # GET /configurations.xml
  def show
    @configuration = Configuration.instance

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @configuration }
    end
  end

  # GET /configurations/edit
  def edit
    @configuration = Configuration.instance
  end

  # PUT /configurations
  # PUT /configurations.xml
  def update
    @configuration = Configuration.instance

    respond_to do |format|
      if @configuration.update(params[:configuration]) or not @configuration.dirty?
        flash[:notice] = 'Configuration was successfully updated.'
        format.html { redirect_to(configuration_url) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @configuration.errors, :status => :unprocessable_entity }
      end
    end
  end

end
