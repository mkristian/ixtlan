class UsersController < ApplicationController

  private

  def set_groups(user, groups)
    for group in user.groups
      unless groups.member? group
        user.groups.delete(group)
      end
    end
    for group in groups
      unless user.groups.member? group
        user.groups << group
      end
    end
  end

  def ids_to_groups(ids)
    if ids
      ids.collect do |id|
        Group.get!(id)
      end
    else
      []
    end
  end

  public 

  # GET /users
  # GET /users.xml
  def index
    @users = User.all()

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @users }
    end
  end

  # GET /users/1
  # GET /users/1.xml
  def show
    @user = User.get!(params[:id])
    @groups = Group.all(:order => [:name.desc])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @user }
    end
  end

  # GET /users/new
  # GET /users/new.xml
  def new
    @user = User.new
    @groups = Group.all(:order => [:name.desc])

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @user }
    end
  end

  # GET /users/1/edit
  def edit
    @user = User.get!(params[:id])
    @groups = Group.all(:order => [:name.desc])
  end

  # POST /users
  # POST /users.xml
  def create
    @user = User.new(params[:user])
    set_groups(@user, ids_to_groups(params[:groups]))
    @user.reset_password
    respond_to do |format|
      if @user.save
        flash[:notice] = "User #{@user.login} created and password '#{@user.password}' sent."
        format.html { redirect_to(user_url(@user.id)) }
        format.xml  { render :xml => @user, :status => :created, :location => @user }
      else
        format.html { render :new }
        format.xml  { render :xml => @user.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /users/1
  # PUT /users/1.xml
  def update
    @user = User.get!(params[:id])
    set_groups(@user, ids_to_groups(params[:groups]))
    
    respond_to do |format|
      if @user.update_attributes(params[:user]) or not @user.dirty?
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
    user = User.get(params[:id])
    user.destroy if user

    respond_to do |format|
      format.html { redirect_to(users_url) }
      format.xml  { head :ok }
    end
  end
end
