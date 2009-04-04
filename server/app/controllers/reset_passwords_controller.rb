class ResetPasswordsController < ApplicationController
  
  skip_before_filter :authentication
  skip_before_filter :guard, :unless => :edit

  # GET /reset_passwords/1
  # GET /reset_passwords/1.xml
  def edit
    @reset_password = ResetPassword.first(:token => params[:id], 
                                          :expired_at.gt => Time.now )
    
    if @reset_password
      session[:reset_password] = @reset_password
      
      create_dummy_user
      puts current_user
      respond_to do |format|
        format.html { render :edit }
        format.xml  { render :status => :ok, :location => edit_profile_path }
      end
    else
      flash[:notice] = "unknown token"
      redirect_to edit_profile_path
    end
  end

  def show
    reset_password = session[:reset_password]
    if reset_password.nil?
      redirect_to edit_profile_path
    else
      redirect_to edit_reset_password_path(reset_password.token)
    end 
  end

  def update
    reset_password = session[:reset_password]
    if reset_password.nil?
      redirect_to edit_profile_path
    else
      if reset_password.user.update_attributes(params[:user])
        flash[:notice] = "password changed"
        reset_password.destroy
        current_user = reset_password.user
        single_sign_on = SingleSignOn.create(:one_time => Passwords.generate, :ip => reset_password.ip, :user =>reset_password.user )
        redirect_to reset_password.success_url + "?token=#{single_sign_on.one_time}"
      else
        @reset_password = reset_password
        @user = reset_password.user
        create_dummy_user
        render :edit
      end
    end
  end

  # POST /reset_passwords
  # POST /reset_passwords.xml
  def create
    reset_password = ResetPassword.new(params[:reset_password])
    reset_password.ip = request.ip
    reset_password.user = User.first(:email => params[:email])
    # TODO sent it, errors

    if reset_password.user.nil?
      flash[:reset_password_notice] = "email not found"
    elsif reset_password.save
      flash[:reset_password_notice] = "email sent to reset password. #{request.url}/#{reset_password.token}/edit"
    else
      p reset_password.errors
      flash[:reset_password_notice] = "some error occured"
    end
    respond_to do |format|
      url = reset_password.success_url + 
        if reset_password.success_url =~ /^http/
          "?flash=#{Base64.encode64('reset_password_notice$' + flash[:reset_password_notice])}"
        else
          ""
        end
      format.html { redirect_to(url) }
      format.xml  { render :status => :created, :location => reset_password.success_url }
    end
  end
end
