module Ixtlan
  module Controllers
    module UsersController

      include SearchQuery

      private

      USER = Object.full_const_get(::Ixtlan::Models::USER)

      def adjust_params(user_params)
        lang = user_params.delete(:preferred_language)
        user_params[:language] = lang.nil? ? nil : lang[:code][0..1]
        user_params.delete(:groups)
      end

      public

      # GET /users
      # GET /users.xml
      def index
        @users = USER.all(simple_query(:login))

        respond_to do |format|
          format.html
          format.xml  { render :xml => @users }
        end
      end

      # GET /users/1
      # GET /users/1.xml
      def show
        @user = USER.first_or_get!(params[:id])

        respond_to do |format|
          format.html # show.html.erb
          format.xml  { render :xml => @user }
        end
      end

      # GET /users/new
      # GET /users/new.xml
      def new
        @user = USER.new

        respond_to do |format|
          format.html # new.html.erb
          format.xml  { render :xml => @user }
        end
      end

      # GET /users/1/edit
      def edit
        @user = USER.first_or_get!(params[:id])
      end

      # POST /users
      # POST /users.xml
      def create
        user_params = params[:user]
        groups = adjust_params(user_params)
        @user = USER.new(user_params)
        @user.current_user = current_user
        @user.reset_password
        @user.update_all_children(groups, current_user)

        respond_to do |format|
          if @user.save
            flash[:notice] = 'User was successfully created.'
            format.html { redirect_to(user_url(@user.id)) }
            format.xml  { render :xml => @user, :status => :created, :location => user_url(@user.id) + ".xml" }

            Mailer.deliver_password(@user.email, "Configuration.instance.password_sender_email", @user.password)
          else
            format.html { render :action => "new" }
            format.xml  { render :xml => @user.errors, :status => :unprocessable_entity }
          end
        end
      end

      # PUT /users/1
      # PUT /users/1.xml
      def update
        @user = USER.first_or_get!(params[:id])
        @user.current_user = current_user
        user_params = params[:user]
        @user.update_all_children(adjust_params(user_params), current_user)
        @user.attributes = user_params

        respond_to do |format|
          if @user.save() or not @user.dirty?
            flash[:notice] = 'User was successfully updated.'
            format.html { redirect_to(user_url(@user.id)) }
            format.xml  { render :xml => @user }
          else
            format.html { render :action => "edit" }
            format.xml  { render :xml => @user.errors, :status => :unprocessable_entity }
          end
        end
      end

      # DELETE /users/1
      # DELETE /users/1.xml
      def destroy
        @user = USER.first_or_get(params[:id])
        # @user.current_user = current_user
        @user.destroy if @user

        respond_to do |format|
          flash[:notice] = 'User was successfully deleted.'
          format.html { redirect_to(users_url) }
          format.xml  { head :ok }
        end
      end
    end
  end
end