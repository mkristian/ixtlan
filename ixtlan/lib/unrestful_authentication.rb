module UnrestfulAuthentication

  protected

  def logged_in?
    !session[:token].nil?
  end

  def current_user 
    if logged_in? and session[:user].nil?
      token = login_from_session
      session[:user] = token.user if token
    end
    session[:user]
  end 

  def current_user=(token)
    logger.debug { "token=" + token.inspect }
    session[:token] = token ? token.token : nil
    session[:user] = token ? token.user : nil
  end

  def reset_session_and_keep_flash
    keep = flash.dup
    reset_session
    flash.merge!(keep)
  end

  def login_required
    if logged_in?
      if request.method == :delete and request.params['commit'] == 'logout'
        reset_session_and_keep_flash
        login_page
        false
      else
        true
      end
    else
      if request.method == :get
        if params[:token]
          if token = login_from_params
            reset_session
            self.current_user = token
            logger.debug { "session = " + session.inspect }
            logger.debug { "url = " + request.url.to_s.sub(/.token=.*/,'') }
            redirect_to request.url.to_s.sub(/.token=.*/,''), :status => :moved_permanently
            true
          else
            reset_session_and_keep_flash
            access_page
            false
          end
        else
          reset_session_and_keep_flash
          login_page
        end  
      elsif request.method == :post or request.method == :put or request.method == :delete
        token = login
        reset_session
        if token and token.token
          self.current_user = token
          redirect_to request.url, :status => :moved_permanently
        else
          access_denied
        end
      end
      false
    end
  end

  def login
    load 'token.rb'
    load 'login.rb'
    load 'role.rb'
    repository(:single_sign_on) do
      AuthenticationToken.create(:login => params[:username],:password => params[:password])
    end
  end

  def login_from_token(token)
    load 'token.rb'
    load 'login.rb'
    load 'role.rb'
    repository(:single_sign_on) do
      Token.get(token)
    end
  end

  def login_from_session
    login_from_token(session[:token])
  end

  def login_from_params
    login_from_token(params[:token])
  end

  def logout
    if token = login_from_session
      token.destroy
    end
    session.delete
    current_user = nil
  end

  def access_denied
    flash[:notice] = "access denied" unless flash[:notice]
    render :template => "sessions/login", :status => :unauthorized
  end

  def login_page
    render :template => "sessions/login"
  end

end
