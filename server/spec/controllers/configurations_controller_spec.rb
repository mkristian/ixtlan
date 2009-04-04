require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe ConfigurationsController do

  def mock_configuration(stubs={})
    @mock_configuration ||= mock_model(Configuration, stubs)
  end
  
  before(:each) do
    user = User.new(:id => 1)
    def user.groups
      [Group.new(:name => "root")]
    end
    controller.send(:current_user=, user)
  end

  describe "GET index" do

    it "exposes all configurationses as @configurationses" do
      Configuration.should_receive(:all).and_return([mock_configuration])
      get :index
      assigns[:configurations].should == [mock_configuration]
    end

    describe "with mime type of xml" do
  
      it "renders all configurationses as xml" do
        Configuration.should_receive(:all).and_return(configurations = mock("Array of Configurations"))
        configurations.should_receive(:to_xml).and_return("generated XML")
        get :index, :format => 'xml'
        response.body.should == "generated XML"
      end
    
    end

  end

  describe "GET show" do

    it "exposes the requested configuration as @configuration" do
      Configuration.should_receive(:get!).with("37").and_return(mock_configuration)
      get :show, :id => "37"
      assigns[:configuration].should equal(mock_configuration)
    end
    
    describe "with mime type of xml" do

      it "renders the requested configuration as xml" do
        Configuration.should_receive(:get!).with("37").and_return(mock_configuration)
        mock_configuration.should_receive(:to_xml).and_return("generated XML")
        get :show, :id => "37", :format => 'xml'
        response.body.should == "generated XML"
      end

    end
    
  end

  describe "GET new" do
  
    it "exposes a new configuration as @configuration" do
      Configuration.should_receive(:new).and_return(mock_configuration)
      get :new
      assigns[:configuration].should equal(mock_configuration)
    end

  end

  describe "GET edit" do
  
    it "exposes the requested configuration as @configuration" do
      Configuration.should_receive(:get!).with("37").and_return(mock_configuration)
      get :edit, :id => "37"
      assigns[:configuration].should equal(mock_configuration)
    end

  end

  describe "POST create" do

    describe "with valid params" do
      
      it "exposes a newly created configuration as @configuration" do
        Configuration.should_receive(:new).with({'these' => 'params'}).and_return(mock_configuration(:save => true))
        post :create, :configuration => {:these => 'params'}
        assigns(:configuration).should equal(mock_configuration)
      end

      it "redirects to the created configuration" do
        Configuration.stub!(:new).and_return(mock_configuration(:save => true))
        post :create, :configuration => {}
        response.should redirect_to(configuration_url(mock_configuration))
      end
      
    end
    
    describe "with invalid params" do

      it "exposes a newly created but unsaved configuration as @configuration" do
        Configuration.stub!(:new).with({'these' => 'params'}).and_return(mock_configuration(:save => false))
        post :create, :configuration => {:these => 'params'}
        assigns(:configuration).should equal(mock_configuration)
      end

      it "re-renders the 'new' template" do
        Configuration.stub!(:new).and_return(mock_configuration(:save => false))
        post :create, :configuration => {}
        response.should render_template('new')
      end
      
    end
    
  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested configuration" do
        Configuration.should_receive(:get!).with("37").and_return(mock_configuration)
        mock_configuration.should_receive(:update_attributes).with({'these' => 'params'})
        mock_configuration.should_receive(:dirty?)
        put :update, :id => "37", :configuration => {:these => 'params'}
      end

      it "exposes the requested configuration as @configuration" do
        Configuration.stub!(:get!).and_return(mock_configuration(:update_attributes => true))
        put :update, :id => "1"
        assigns(:configuration).should equal(mock_configuration)
      end

      it "redirects to the configuration" do
        Configuration.stub!(:get!).and_return(mock_configuration(:update_attributes => true))
        put :update, :id => "1"
        response.should redirect_to(configuration_url(mock_configuration))
      end

    end
    
    describe "with invalid params" do

      it "updates the requested configuration" do
        Configuration.should_receive(:get!).with("37").and_return(mock_configuration)
        mock_configuration.should_receive(:update_attributes).with({'these' => 'params'})
        mock_configuration.should_receive(:dirty?)
        put :update, :id => "37", :configuration => {:these => 'params'}
      end

      it "exposes the configuration as @configuration" do
        Configuration.stub!(:get!).and_return(mock_configuration(:update_attributes => false))
        mock_configuration.should_receive(:dirty?)
        put :update, :id => "1"
        assigns(:configuration).should equal(mock_configuration)
      end

      it "re-renders the 'edit' template" do
        Configuration.stub!(:get!).and_return(mock_configuration(:update_attributes => false, :dirty? => true))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

  describe "DELETE destroy" do

    it "destroys the requested configuration" do
      Configuration.should_receive(:get).with("37").and_return(mock_configuration)
      mock_configuration.should_receive(:destroy)
      delete :destroy, :id => "37"
    end
  
    it "redirects to the configurations list" do
      Configuration.should_receive(:get).with("1").and_return(mock_configuration(:destroy => true))
      delete :destroy, :id => "1"
      response.should redirect_to(configurations_url)
    end

  end

end
