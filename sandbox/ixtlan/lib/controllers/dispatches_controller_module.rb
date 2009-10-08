module DispatchesControllerModule
  
  @@map = {}

  def self.included(base)
    base.skip_before_filter :guard
  end

  def url(application)
    @@map[application.to_sym] if application
  end

  def index
    flash[:notice] = 'missing url for dispatch'
    render "errors/general"    
  end

  # POST /dispatches
  def create
    if url = url(params[:application])
      token = nil
      repository(:single_sign_on) do
        token = Token.get(session[:token])
        token.save
        token.reload
      end
      redirect_to(url + "?token=#{token.one_time}")
    else
      flash[:notice] = 'missing url for dispatch'
      render "errors/general"
    end
  end

  private 

  def audit
    ""
  end
end
