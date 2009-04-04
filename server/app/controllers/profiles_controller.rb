class ProfilesController < ApplicationController
  
  skip_before_filter :guard

  def edit
    @user = current_user
  end

  def show
    redirect_to edit_profile_path
  end

  def update
    @user = current_user
    if params[:old_password].blank?
      u = params[:user]
      u.delete(:email)
      u.delete(:password)
      u.delete(:password_confirmation)
    end
    if not User.authenticate(@user.login, params[:old_password])
      flash[:notice] = "permission denied - please supply right password"
      render :edit
    elsif @user.update_attributes(params[:user])
      flash[:notice] = "profile updated"
      redirect_to edit_profile_path
    else
      render :edit
    end
  end
end
