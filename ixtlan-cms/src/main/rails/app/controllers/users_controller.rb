class UsersController < ApplicationController

  # GET /users
  # GET /users.xml
  def index
    @users = User.all()

    respond_to do |format|
      format.html
      format.xml  { render :xml => @users }
    end
  end

  # GET /users/1
  # GET /users/1.xml
  def show
    @user = User.get!(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @user }
    end
  end

  # GET /users/new
  # GET /users/new.xml
  def new
    @user = User.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @user }
    end
  end

  # GET /users/1/edit
  def edit
    @user = User.get!(params[:id])
  end

  # POST /users
  # POST /users.xml
  def create
    @user = User.new(params[:user])
    @user.current_user = current_user

    respond_to do |format|
      if @user.save
        flash[:notice] = 'User was successfully created.'
        format.html { redirect_to(user_url(@user.id)) }
        format.xml  { render :xml => @user, :status => :created, :location => @user }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @user.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /users/1
  # PUT /users/1.xml
  def update
    @user = User.get!(params[:id])
    @user.current_user = current_user

    respond_to do |format|
      if @user.update(params[:user]) or not @user.dirty?
        flash[:notice] = 'User was successfully updated.'
        format.html { redirect_to(user_url(@user.id)) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @user.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /users/1
  # DELETE /users/1.xml
  def destroy
    @user = User.get(params[:id])
    @user.current_user = current_user
    @user.destroy if @user

    respond_to do |format|
      flash[:notice] = 'User was successfully deleted.'
      format.html { redirect_to(users_url) }
      format.xml  { head :ok }
    end
  end
end
