require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe DispatchesController do

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

  describe "POST create" do

    describe "with valid params" do
      
     
      it "redirects to the created dispatch" do
        post :create, :application => 'demo'
        response.should redirect_to("http://example.com")
      end
      
    end
    
    describe "with invalid params" do

      it "re-renders the current page" do
        post :create, :application => 'example'
        response.should render_template('errors/general')
      end
      
    end
    
  end

end
