require 'pathname'
module ConfigurationsControllerModule
  
  def self.included(base)
    base.prepend_view_path((Pathname(__FILE__).dirname + "../views").to_s)
  end

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
    keep_audit_logs = @configuration.keep_audit_logs  
    respond_to do |format|
      if @configuration.update_attributes(params[:configuration]) or not @configuration.dirty?

        # reconfigure audit logging if changed
        if keep_audit_logs != @configuration.keep_audit_logs  
          AuditConfig.reconfigure(Configuration.instance.keep_audit_logs, 
                                  Rails.root.join('log').to_s + '/audit.log')
        end

        flash[:notice] = 'Configuration was successfully updated.'
        format.html { redirect_to(configuration_url) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @configuration.errors, :status => :unprocessable_entity }
      end
    end
  end

  private 

  def audit
    Configuration.instance.to_s
  end
end
