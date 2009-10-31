require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe UsersController do

  def mock_user(stubs={})
    @mock_user ||= mock_model(User, stubs)
  end

  def mock_array(*args)
    a = args
    def a.model
      User
    end
    a
  end

  def mock_arguments(merge = {})
    args = merge
    args.merge!(:current_user= => nil)
    args.merge!(:model => User, :key => 12)
  args
  end

  before(:each) do
    user = Ixtlan::User.new(:id => 1, :login => 'root')
    def user.groups
      [Ixtlan::Group.new(:name => "root")]
    end
    controller.send(:current_user=, user)
    mock_configuration = mock_model(Ixtlan::Configuration,{})
    Ixtlan::Configuration.should_receive(:instance).any_number_of_times.and_return(mock_configuration)
    mock_configuration.should_receive(:session_idle_timeout).any_number_of_times.and_return(1)
  end

  describe "GET index" do

    it "exposes all users as @users" do
      User.should_receive(:all).and_return(mock_array(mock_user))
      get :index
      assigns[:users].should == mock_array(mock_user)
    end

    describe "with mime type of xml" do

      it "renders all userses as xml" do
        User.should_receive(:all).and_return(users = mock_array("Array of Users"))
        users.should_receive(:to_xml).and_return("generated XML")
        get :index, :format => 'xml'
        response.body.should == "generated XML"
      end

    end

  end

  describe "GET show" do

    it "exposes the requested user as @user" do
      User.should_receive(:get!).with("37").and_return(mock_user(mock_arguments))
      get :show, :id => "37"
      assigns[:user].should equal(mock_user)
    end

    describe "with mime type of xml" do

      it "renders the requested user as xml" do
        User.should_receive(:get!).with("37").and_return(mock_user(mock_arguments))
        mock_user.should_receive(:to_xml).and_return("generated XML")
        get :show, :id => "37", :format => 'xml'
        response.body.should == "generated XML"
      end

    end

  end

  describe "GET new" do

    it "exposes a new user as @user" do
      User.should_receive(:new).and_return(mock_user(mock_arguments))
      get :new
      assigns[:user].should equal(mock_user)
    end

  end

  describe "GET edit" do

    it "exposes the requested user as @user" do
      User.should_receive(:get!).with("37").and_return(mock_user(mock_arguments))
      get :edit, :id => "37"
      assigns[:user].should equal(mock_user)
    end

  end

  describe "POST create" do

    describe "with valid params" do

      it "exposes a newly created user as @user" do
        User.should_receive(:new).with({'these' => 'params'}).and_return(mock_user(mock_arguments(:save => true)))
        post :create, :user => {:these => 'params'}
        assigns(:user).should equal(mock_user)
      end

      it "redirects to the created user" do
        User.stub!(:new).and_return(mock_user(mock_arguments(:save => true)))
        post :create, :user => {}
        response.should redirect_to(user_url(mock_user))
      end

    end

    describe "with invalid params" do

      it "exposes a newly created but unsaved user as @user" do
        User.stub!(:new).with({'these' => 'params'}).and_return(mock_user(mock_arguments(:save => false)))
        post :create, :user => {:these => 'params'}
        assigns(:user).should equal(mock_user)
      end

      it "re-renders the 'new' template" do
        User.stub!(:new).and_return(mock_user(mock_arguments(:save => false)))
        post :create, :user => {}
        response.should render_template('new')
      end

    end

  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested user" do
        User.should_receive(:get!).with("37").and_return(mock_user(mock_arguments))
        mock_user.should_receive(:update).with({'these' => 'params'})
        mock_user.should_receive(:dirty?)
        put :update, :id => "37", :user => {:these => 'params'}
      end

      it "exposes the requested user as @user" do
        User.stub!(:get!).and_return(mock_user(mock_arguments(:update => true)))
        put :update, :id => "1"
        assigns(:user).should equal(mock_user)
      end

      it "redirects to the user" do
        User.stub!(:get!).and_return(mock_user(mock_arguments(:update => true)))
        put :update, :id => "1"
        response.should redirect_to(user_url(mock_user))
      end

    end

    describe "with invalid params" do

      it "updates the requested user" do
        User.should_receive(:get!).with("37").and_return(mock_user(mock_arguments))
        mock_user.should_receive(:update).with({'these' => 'params'})
        mock_user.should_receive(:dirty?)
        put :update, :id => "37", :user => {:these => 'params'}
      end

      it "exposes the user as @user" do
        User.stub!(:get!).and_return(mock_user(mock_arguments(:update => false)))
        mock_user.should_receive(:dirty?)
        put :update, :id => "1"
        assigns(:user).should equal(mock_user)
      end

      it "re-renders the 'edit' template" do
        User.stub!(:get!).and_return(mock_user(mock_arguments(:update => false, :dirty? => true)))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

  describe "DELETE destroy" do

    it "destroys the requested user" do
      User.should_receive(:get).with("37").and_return(mock_user(mock_arguments))
      mock_user.should_receive(:destroy)
      delete :destroy, :id => "37"
    end

    it "redirects to the users list" do
      User.should_receive(:get).with("1").and_return(mock_user(mock_arguments(:destroy => true)))
      delete :destroy, :id => "1"
      response.should redirect_to(users_url)
    end

  end

end
