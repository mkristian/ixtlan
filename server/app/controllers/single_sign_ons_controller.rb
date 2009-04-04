class SingleSignOnsController < ApplicationController

  skip_before_filter :guard, :authentication, :reset_password

  # GET /single_sign_ons/1
  # GET /single_sign_ons/1.xml
  def show
    @single_sign_on = SingleSignOn.first(:token => params[:id], :ip => request.ip)
    unless @single_sign_on
      @single_sign_on = SingleSignOn.first(:one_time => params[:id], :ip => request.ip)
      if @single_sign_on
        #@single_sign_on.one_time = nil
      end
    end
p  @single_sign_on
    if @single_sign_on
      render :xml => @single_sign_on.to_xml(:only => [:token, :one_time], :methods => [:user])
      @single_sign_on.save
    else
      head :not_found
    end
  end

  # POST /single_sign_ons
  # POST /single_sign_ons.xml
  def create
    @single_sign_on = SingleSignOn.new()
    @single_sign_on.ip = request.ip
    @single_sign_on.user = User.authenticate(params[:single_sign_on][:login], params[:single_sign_on][:password])

puts "---------------------------------------------"
p params
p @single_sign_on.user

    respond_to do |format|
      if @single_sign_on.save
        flash[:notice] = 'SingleSignOn was successfully created.'
        format.html { redirect_to(single_sign_on_url(@single_sign_on.id)) }
        format.xml  { render :xml => @single_sign_on.to_xml(:only => [:token], :methods => [:user]), :status => :created, :location => @single_sign_on }
      else
p @single_sign_on.errors.full_messages
        format.html { render :action => "new" }
        format.xml  { render :xml => @single_sign_on.errors.full_messages.to_s, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /single_sign_ons/1
  # PUT /single_sign_ons/1.xml
  def update
    @single_sign_on = SingleSignOn.get!(params[:id])
#    @single_sign_on.one_time = Passwords.generate
    respond_to do |format|
      if @single_sign_on.save
puts "saved"
p @single_sign_on
        flash[:notice] = 'SingleSignOn was successfully updated.'
        format.html { redirect_to(single_sign_on_url(@single_sign_on.id)) }
        format.xml  { head :ok }
      else
        p @single_sign_on.errors
        format.html { render :action => "edit" }
        format.xml  { render :xml => @single_sign_on.errors.to_xml, :status => :unprocessable_entity }
      end
    end
  end
end
