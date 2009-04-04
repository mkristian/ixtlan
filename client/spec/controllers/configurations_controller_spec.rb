require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe ConfigurationsController do

  def mock_configuration(stubs={})
    @mock_configuration ||= mock_model(Configuration, stubs)
  end
  
  before(:each) do
    user = Login.new(:id => 1)
    def user.groups
      [Role.new(:name => "root")]
    end
    token = Token.new(:token => 'hmm')
    token.user = user
    controller.send(:current_user=, token)
  end

  describe "GET show" do

    it "exposes the requested configuration as @configuration" do
      Configuration.should_receive(:instance).and_return(mock_configuration)
      get :show
      assigns[:configuration].should equal(mock_configuration)
    end
    
    describe "with mime type of xml" do

      it "renders the requested configuration as xml" do
        Configuration.should_receive(:instance).and_return(mock_configuration)
        mock_configuration.should_receive(:to_xml).and_return("generated XML")
        get :show, :format => 'xml'
        response.body.should == "generated XML"
      end

    end
    
  end

  describe "GET edit" do
  
    it "exposes the requested configuration as @configuration" do
      Configuration.should_receive(:instance).and_return(mock_configuration)
      get :edit
      assigns[:configuration].should equal(mock_configuration)
    end

  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested configuration" do
        Configuration.should_receive(:instance).and_return(mock_configuration)
        mock_configuration.should_receive(:update_attributes).with({'these' => 'params'})
        mock_configuration.should_receive(:dirty?)
        put :update, :configuration => {:these => 'params'}
      end

      it "exposes the requested configuration as @configuration" do
        Configuration.stub!(:get!).and_return(mock_configuration(:update_attributes => true))
        put :update
        assigns(:configuration).should equal(mock_configuration)
      end

      it "redirects to the configuration" do
        Configuration.stub!(:get!).and_return(mock_configuration(:update_attributes => true))
        put :update
        response.should redirect_to(configuration_url)
      end

    end
    
    describe "with invalid params" do

      it "updates the requested configuration" do
        Configuration.should_receive(:instance).and_return(mock_configuration)
        mock_configuration.should_receive(:update_attributes).with({'these' => 'params'})
        mock_configuration.should_receive(:dirty?)
        put :update, :configuration => {:these => 'params'}
      end

      it "exposes the configuration as @configuration" do
        Configuration.stub!(:get!).and_return(mock_configuration(:update_attributes => false))
        mock_configuration.should_receive(:dirty?)
        put :update
        assigns(:configuration).should equal(mock_configuration)
      end

      it "re-renders the 'edit' template" do
        Configuration.stub!(:get!).and_return(mock_configuration(:update_attributes => false, :dirty? => true))
        put :update
        response.should render_template('edit')
      end

    end

  end

end
