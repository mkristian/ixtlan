require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe ResetPasswordsController do

  def mock_reset_password(stubs={})
    @mock_reset_password ||= mock_model(ResetPassword, stubs)
  end
  
  def mock_user(stubs={})
    @mock_user ||= mock_model(User, stubs)
  end
  
  before(:each) do
    user = User.new(:id => 0)
    def user.groups
      []
    end
    controller.send(:current_user=, user)
    mock_configuration = mock_model(Configuration,{})
    Configuration.should_receive(:instance).any_number_of_times.and_return(mock_configuration)
    mock_configuration.should_receive(:session_idle_timeout).any_number_of_times.and_return(1)
  end

  describe "GET show" do

    it "should redirect to edit requested reset_password" do
      session[:reset_password] = mock_reset_password(:token => "15")
      get :show, :id => "37"
      response.should redirect_to(edit_reset_password_url(session[:reset_password].token))
    end
       
    it "should redirect to edit profile since no reset_password was given" do
      session[:reset_password] = nil
      get :show, :id => "37"
      response.should redirect_to(edit_profile_url)
    end
       
  end

  describe "GET edit" do
  
    it "exposes the requested reset_password as @reset_password" do
      ResetPassword.should_receive(:first).and_return(mock_reset_password(:user => User.new))
      get :edit, :id => "37"
      assigns[:reset_password].should equal(mock_reset_password)
    end

  end

  describe "POST create" do

    describe "with valid params" do
      
      it "redirects to the created reset_password" do
        ResetPassword.stub!(:new).and_return(mock_reset_password(:save => true, :token => "tkn", :success_url => "http://somewhere"))
        User.stub!(:first).and_return(mock_user)
        mock_reset_password.should_receive(:user=).with(mock_user)
        post :create, :reset_password => {}
        response.should redirect_to(mock_reset_password.success_url)
      end
      
    end
    
    describe "with invalid params" do

      it "re-renders the 'new' template" do
        ResetPassword.stub!(:new).and_return(mock_reset_password(:save => false, :token => "tkn", :success_url => "http://somewhere"))
        User.stub!(:first).and_return(mock_user)
        mock_reset_password.should_receive(:user=).with(mock_user)
        post :create, :reset_password => {}
        response.should redirect_to(mock_reset_password.success_url)
      end
      
    end
    
  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested reset_password" do
        session[:reset_password] = mock_reset_password(:user => mock_user(:update_attributes => false, :login => "login"))
        put :update, :id => "37", :reset_password => {:these => 'params'}
      end

      it "redirects to the reset_password" do
        session[:reset_password] = mock_reset_password(:user => mock_user(:update_attributes => false, :login => "login"), :success_url => "http://somewhere")
        mock_user.should_receive(:update_attributes).and_return(true)
        mock_reset_password.should_receive(:destroy)
        put :update, :id => "1"
        response.should redirect_to(mock_reset_password.success_url)
      end

    end
    
    describe "with invalid params" do

      it "updates the requested reset_password" do
        session[:reset_password] = mock_reset_password(:user => mock_user(:update_attributes => false, :login => "login"))
        mock_reset_password.should_receive(:user)
        mock_user.should_receive(:update_attributes).with({'these' => 'params'})
        put :update, :id => "37", :user => {:these => 'params'}
      end

      it "updates the requested reset_password without session" do
        put :update, :id => "37", :user => {:these => 'params'}
        response.should redirect_to(edit_profile_url)
      end

      it "exposes the reset_password as @reset_password" do
        session[:reset_password] = mock_reset_password(:user => mock_user(:update_attributes => false, :login => "login"))
        put :update, :id => "1"
        assigns(:reset_password).should equal(mock_reset_password)
        assigns(:user).should equal(mock_user)
      end

      it "re-renders the 'edit' template" do
        session[:reset_password] = mock_reset_password(:user => mock_user(:update_attributes => false, :login => "login"))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

end
