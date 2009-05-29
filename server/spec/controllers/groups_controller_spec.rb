require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe GroupsController do

  def mock_group(stubs={})
    @mock_group ||= mock_model(Group, stubs)
  end
  
  before(:each) do
    user = User.new(:id => 1)
    def user.groups
      [Group.new(:name => "root")]
    end
    controller.send(:current_user=, user)
    mock_configuration = mock_model(Configuration,{})
    Configuration.should_receive(:instance).any_number_of_times.and_return(mock_configuration)
    mock_configuration.should_receive(:session_idle_timeout).any_number_of_times.and_return(1)
  end

  describe "GET index" do

    it "exposes all groupses as @groupses" do
      Group.should_receive(:all).and_return([mock_group])
      get :index
      assigns[:groups].should == [mock_group]
    end

    describe "with mime type of xml" do
  
      it "renders all groupses as xml" do
        Group.should_receive(:all).and_return(groups = mock("Array of Groups"))
        groups.should_receive(:to_xml).and_return("generated XML")
        get :index, :format => 'xml'
        response.body.should == "generated XML"
      end
    
    end

  end

  describe "GET show" do

    it "exposes the requested group as @group" do
      Group.should_receive(:get!).with("37").and_return(mock_group)
      get :show, :id => "37"
      assigns[:group].should equal(mock_group)
    end
    
    describe "with mime type of xml" do

      it "renders the requested group as xml" do
        Group.should_receive(:get!).with("37").and_return(mock_group)
        mock_group.should_receive(:to_xml).and_return("generated XML")
        get :show, :id => "37", :format => 'xml'
        response.body.should == "generated XML"
      end

    end
    
  end

  describe "GET new" do
  
    it "exposes a new group as @group" do
      Group.should_receive(:new).twice.and_return(mock_group(:name => "root"))
      get :new
      assigns[:group].should equal(mock_group)
    end

  end

  describe "GET edit" do
  
    it "exposes the requested group as @group" do
      Group.should_receive(:get!).with("37").and_return(mock_group)
      get :edit, :id => "37"
      assigns[:group].should equal(mock_group)
    end

  end

  describe "POST create" do

    describe "with valid params" do
      
      it "exposes a newly created group as @group" do
        Group.should_receive(:new).twice.and_return(mock_group(:save => true, :name => "root"))
        post :create, :group => {:these => 'params'}
        assigns(:group).should equal(mock_group)
      end

      it "redirects to the created group" do
        Group.stub!(:new).and_return(mock_group(:save => true, :name => "root"))
        post :create, :group => {}
        response.should redirect_to(group_url(mock_group))
      end
      
    end
    
    describe "with invalid params" do

      it "exposes a newly created but unsaved group as @group" do
        Group.stub!(:new).twice.and_return(mock_group(:save => false, :name => "root"))
        post :create, :group => {:these => 'params'}
        assigns(:group).should equal(mock_group)
      end

      it "re-renders the 'new' template" do
        Group.stub!(:new).and_return(mock_group(:save => false, :name => "root"))
        post :create, :group => {}
        response.should render_template('new')
      end
      
    end
    
  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested group" do
        Group.should_receive(:get!).with("37").and_return(mock_group)
        mock_group.should_receive(:update_attributes).with({'these' => 'params'})
        mock_group.should_receive(:dirty?)
        put :update, :id => "37", :group => {:these => 'params'}
      end

      it "exposes the requested group as @group" do
        Group.stub!(:get!).and_return(mock_group(:update_attributes => true))
        put :update, :id => "1"
        assigns(:group).should equal(mock_group)
      end

      it "redirects to the group" do
        Group.stub!(:get!).and_return(mock_group(:update_attributes => true))
        put :update, :id => "1"
        response.should redirect_to(group_url(mock_group))
      end

    end
    
    describe "with invalid params" do

      it "updates the requested group" do
        Group.should_receive(:get!).with("37").and_return(mock_group)
        mock_group.should_receive(:update_attributes).with({'these' => 'params'})
        mock_group.should_receive(:dirty?)
        put :update, :id => "37", :group => {:these => 'params'}
      end

      it "exposes the group as @group" do
        Group.stub!(:get!).and_return(mock_group(:update_attributes => false))
        mock_group.should_receive(:dirty?)
        put :update, :id => "1"
        assigns(:group).should equal(mock_group)
      end

      it "re-renders the 'edit' template" do
        Group.stub!(:get!).and_return(mock_group(:update_attributes => false, :dirty? => true))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

  describe "DELETE destroy" do

    it "destroys the requested group" do
      Group.should_receive(:get).with("37").and_return(mock_group)
      mock_group.should_receive(:destroy)
      delete :destroy, :id => "37"
    end
  
    it "redirects to the groups list" do
      Group.should_receive(:get).with("1").and_return(mock_group(:destroy => true))
      delete :destroy, :id => "1"
      response.should redirect_to(groups_url)
    end

  end

end
