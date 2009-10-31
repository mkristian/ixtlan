class PermissionsController < ApplicationController

  skip_before_filter :authenticate, :guard

  def index
     render :xml => Ixtlan::Guard.export_xml
  end

end
