require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe UsersController do

  def mock_user(stubs={})
    @mock_user ||= mock_model(User, stubs)
  end
  
  before(:each) do
    user = User.new(:id => 1)
    def user.groups
      [Group.new(:name => "root")]
    end
    controller.send(:current_user=, user)
  end

  describe "GET index" do

    it "exposes all userses as @userses" do
      User.should_receive(:all).and_return([mock_user])
      get :index
      assigns[:users].should == [mock_user]
    end

    describe "with mime type of xml" do
  
      it "renders all userses as xml" do
        User.should_receive(:all).and_return(users = mock("Array of Users"))
        users.should_receive(:to_xml).and_return("generated XML")
        get :index, :format => 'xml'
        response.body.should == "generated XML"
      end
    
    end

  end

  describe "GET show" do

    it "exposes the requested user as @user" do
      User.should_receive(:get!).with("37").and_return(mock_user)
      get :show, :id => "37"
      assigns[:user].should equal(mock_user)
    end
    
    describe "with mime type of xml" do

      it "renders the requested user as xml" do
        User.should_receive(:get!).with("37").and_return(mock_user)
        mock_user.should_receive(:to_xml).and_return("generated XML")
        get :show, :id => "37", :format => 'xml'
        response.body.should == "generated XML"
      end

    end
    
  end

  describe "GET new" do
  
    it "exposes a new user as @user" do
      User.should_receive(:new).and_return(mock_user)
      get :new
      assigns[:user].should equal(mock_user)
    end

  end

  describe "GET edit" do
  
    it "exposes the requested user as @user" do
      User.should_receive(:get!).with("37").and_return(mock_user)
      get :edit, :id => "37"
      assigns[:user].should equal(mock_user)
    end

  end

  describe "POST create" do

    describe "with valid params" do
      
      it "exposes a newly created user as @user" do
        User.should_receive(:new).with({'these' => 'params'}).and_return(mock_user(:save => true, :groups => [], :reset_password => "secret", :login => "me", :password => "secret"))
        post :create, :user => {:these => 'params'}
        assigns(:user).should equal(mock_user)
      end

      it "redirects to the created user" do
        User.stub!(:new).and_return(mock_user(:save => true, :groups => [], :reset_password => "secret", :login => "me", :password => "secret"))
        post :create, :user => {}
        response.should redirect_to(user_url(mock_user))
      end
      
    end
    
    describe "with invalid params" do

      it "exposes a newly created but unsaved user as @user" do
        User.stub!(:new).with({'these' => 'params'}).and_return(mock_user(:save => false, :groups => [], :reset_password => "secret"))
        post :create, :user => {:these => 'params'}
        assigns(:user).should equal(mock_user)
      end

      it "re-renders the 'new' template" do
        User.stub!(:new).and_return(mock_user(:save => false, :groups => [], :reset_password => "secret"))
        post :create, :user => {}
        response.should render_template('new')
      end
      
    end
    
  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested user" do
        User.should_receive(:get!).with("37").and_return(mock_user(:groups => [], :dirty? => true))
        mock_user.should_receive(:update_attributes).with({'these' => 'params'})
        put :update, :id => "37", :user => {:these => 'params'}
      end

      it "exposes the requested user as @user" do
        User.stub!(:get!).and_return(mock_user(:update_attributes => true, :groups => []))
        put :update, :id => "1"
        assigns(:user).should equal(mock_user)
      end

      it "redirects to the user" do
        User.stub!(:get!).and_return(mock_user(:update_attributes => true, :groups => []))
        put :update, :id => "1"
        response.should redirect_to(user_url(mock_user))
      end

    end
    
    describe "with invalid params" do

      it "updates the requested user" do
        User.should_receive(:get!).with("37").and_return(mock_user(:groups => []))
        mock_user.should_receive(:update_attributes).with({'these' => 'params'})
        mock_user.should_receive(:dirty?)
        put :update, :id => "37", :user => {:these => 'params'}
      end

      it "exposes the user as @user" do
        User.stub!(:get!).and_return(mock_user(:update_attributes => false, :groups => []))
        mock_user.should_receive(:dirty?)
        put :update, :id => "1"
        assigns(:user).should equal(mock_user)
      end

      it "re-renders the 'edit' template" do
        User.stub!(:get!).and_return(mock_user(:update_attributes => false, :dirty? => true, :groups => []))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

  describe "DELETE destroy" do

    it "destroys the requested user" do
      User.should_receive(:get).with("37").and_return(mock_user)
      mock_user.should_receive(:destroy)
      delete :destroy, :id => "37"
    end
  
    it "redirects to the users list" do
      User.should_receive(:get).with("1").and_return(mock_user(:destroy => true))
      delete :destroy, :id => "1"
      response.should redirect_to(users_url)
    end

  end

end
