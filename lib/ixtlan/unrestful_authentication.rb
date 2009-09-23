module Ixtlan
  module UnrestfulAuthentication

    def logged_in?
      !session[:user_id].nil?
    end

    def current_user
      session[:user] = login_from_session if logged_in? and session[:user].nil?
      session[:user]
    end 

    def current_user=(new_user)
      session[:user_id] = new_user ? new_user.id : nil
      session[:user] = new_user
    end

  def verify_authenticity
    # TODO handle xml differently then html
    verify_authenticity_token if logged_in?
  end

    def authenticate
      if logged_in?
        case(request.method)
        when :delete
          logout
        else
          true
        end
      else
        case(request.method)
        when :get   
          reset_session
          render_login
        when :post
          user = login_from_params
          if user
            #  reset_session
            self.current_user = user
            render_successful_login
          else
            reset_session
            render_access_denied
          end
        when :put
        when :delete
          reset_session
          render_access_denied
        end
        false
      end
    end

    def login_from_params
      Ixtlan::User.authenticate(params[:login], params[:password])
    end

    def login_from_session
      Ixtlan::User.get(session[:user_id])
    end

    def logout
      if(params[:logout] == current_user.id)
        reset_session
        current_user = nil
        render_login
        false
      else
        true
      end
    end

    def render_successful_login
      respond_to do |format|
        format.html { redirect_to request.url, :status => :moved_permanently}
        format.xml { head :ok }
      end
    end

    def render_access_denied
      respond_to do |format|
        format.html do
          flash[:notice] = "access_denied" unless flash[:notice]
          render :template => "sessions/login", :status => :unauthorized
        end
        format.xml { head :unauthorized}
      end
    end

    def render_login
      respond_to do |format|
        format.html { render :template => "sessions/login" }
        format.xml { head :ok }
      end
    end    
  end
end
