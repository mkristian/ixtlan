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
    Configuration.should_receive(:instance).any_number_of_times.and_return(mock_configuration)
    mock_configuration.should_receive(:session_idle_timeout).any_number_of_times.and_return(1)
  end

  describe "GET show" do

    it "exposes the requested configuration as @configuration" do
      get :show
      assigns[:configuration].should equal(mock_configuration)
    end
    
    describe "with mime type of xml" do

      it "renders the requested configuration as xml" do
        mock_configuration.should_receive(:to_xml).and_return("generated XML")
        get :show, :format => 'xml'
        response.body.should == "generated XML"
      end

    end
    
  end

  describe "GET edit" do
  
    it "exposes the requested configuration as @configuration" do
      get :edit
      assigns[:configuration].should equal(mock_configuration)
    end

  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested configuration" do
        mock_configuration.should_receive(:keep_audit_logs).twice
        mock_configuration.should_receive(:update_attributes).with({'these' => 'params'})
        mock_configuration.should_receive(:dirty?)
        put :update, :configuration => {:these => 'params'}
      end

      it "exposes the requested configuration as @configuration" do
        Configuration.stub!(:get!).and_return(mock_configuration)
        mock_configuration.should_receive(:keep_audit_logs).twice
        mock_configuration.should_receive(:update_attributes).and_return(true)
        put :update
        assigns(:configuration).should equal(mock_configuration)
      end

      it "redirects to the configuration" do
        Configuration.stub!(:get!).and_return(mock_configuration)
        mock_configuration.should_receive(:keep_audit_logs).twice
        mock_configuration.should_receive(:update_attributes).and_return(true)
        put :update
        response.should redirect_to(configuration_url)
      end

    end
    
    describe "with invalid params" do

      it "updates the requested configuration" do
        mock_configuration.should_receive(:keep_audit_logs).twice
        mock_configuration.should_receive(:update_attributes).with({'these' => 'params'})
        mock_configuration.should_receive(:dirty?)
        put :update, :configuration => {:these => 'params'}
      end

      it "exposes the configuration as @configuration" do
        Configuration.stub!(:get!).and_return(mock_configuration)
        mock_configuration.should_receive(:keep_audit_logs)
        mock_configuration.should_receive(:update_attributes).and_return(false)
        mock_configuration.should_receive(:dirty?).and_return(true)
        put :update
        assigns(:configuration).should equal(mock_configuration)
      end

      it "re-renders the 'edit' template" do
        Configuration.stub!(:get!).and_return(mock_configuration)
        mock_configuration.should_receive(:keep_audit_logs)
        mock_configuration.should_receive(:update_attributes).and_return(false)
        mock_configuration.should_receive(:dirty?).and_return(true)
        put :update
        response.should render_template('edit')
      end

    end

  end

end
