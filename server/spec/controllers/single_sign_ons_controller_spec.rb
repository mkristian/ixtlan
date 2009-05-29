require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe SingleSignOnsController do

  def mock_single_sign_on(stubs={})
    @mock_single_sign_on ||= mock_model(SingleSignOn, stubs)
  end

  before(:each)do
    mock_configuration = mock_model(Configuration,{})
    Configuration.should_receive(:instance).any_number_of_times.and_return(mock_configuration)
    mock_configuration.should_receive(:session_idle_timeout).any_number_of_times.and_return(1)
  end

  describe "GET index" do

    it "exposes all single_sign_onses as @single_sign_onses" do
      SingleSignOn.should_receive(:all).and_return([mock_single_sign_on])
      get :index
      assigns[:single_sign_ons].should == [mock_single_sign_on]
    end

    describe "with mime type of xml" do
  
      it "renders all single_sign_onses as xml" do
        SingleSignOn.should_receive(:all).and_return(single_sign_ons = mock("Array of SingleSignOns"))
        single_sign_ons.should_receive(:to_xml).and_return("generated XML")
        get :index, :format => 'xml'
        response.body.should == "generated XML"
      end
    
    end

  end

  describe "GET show" do

    it "exposes the requested single_sign_on as @single_sign_on" do
      SingleSignOn.should_receive(:get!).with("37").and_return(mock_single_sign_on)
      get :show, :id => "37"
      assigns[:single_sign_on].should equal(mock_single_sign_on)
    end
    
    describe "with mime type of xml" do

      it "renders the requested single_sign_on as xml" do
        SingleSignOn.should_receive(:get!).with("37").and_return(mock_single_sign_on)
        mock_single_sign_on.should_receive(:to_xml).and_return("generated XML")
        get :show, :id => "37", :format => 'xml'
        response.body.should == "generated XML"
      end

    end
    
  end

  describe "GET new" do
  
    it "exposes a new single_sign_on as @single_sign_on" do
      SingleSignOn.should_receive(:new).and_return(mock_single_sign_on)
      get :new
      assigns[:single_sign_on].should equal(mock_single_sign_on)
    end

  end

  describe "GET edit" do
  
    it "exposes the requested single_sign_on as @single_sign_on" do
      SingleSignOn.should_receive(:get!).with("37").and_return(mock_single_sign_on)
      get :edit, :id => "37"
      assigns[:single_sign_on].should equal(mock_single_sign_on)
    end

  end

  describe "POST create" do

    describe "with valid params" do
      
      it "exposes a newly created single_sign_on as @single_sign_on" do
        SingleSignOn.should_receive(:new).with({'these' => 'params'}).and_return(mock_single_sign_on(:save => true))
        post :create, :single_sign_on => {:these => 'params'}
        assigns(:single_sign_on).should equal(mock_single_sign_on)
      end

      it "redirects to the created single_sign_on" do
        SingleSignOn.stub!(:new).and_return(mock_single_sign_on(:save => true))
        post :create, :single_sign_on => {}
        response.should redirect_to(single_sign_on_url(mock_single_sign_on))
      end
      
    end
    
    describe "with invalid params" do

      it "exposes a newly created but unsaved single_sign_on as @single_sign_on" do
        SingleSignOn.stub!(:new).with({'these' => 'params'}).and_return(mock_single_sign_on(:save => false))
        post :create, :single_sign_on => {:these => 'params'}
        assigns(:single_sign_on).should equal(mock_single_sign_on)
      end

      it "re-renders the 'new' template" do
        SingleSignOn.stub!(:new).and_return(mock_single_sign_on(:save => false))
        post :create, :single_sign_on => {}
        response.should render_template('new')
      end
      
    end
    
  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested single_sign_on" do
        SingleSignOn.should_receive(:get!).with("37").and_return(mock_single_sign_on)
        mock_single_sign_on.should_receive(:update_attributes).with({'these' => 'params'})
        mock_single_sign_on.should_receive(:dirty?)
        put :update, :id => "37", :single_sign_on => {:these => 'params'}
      end

      it "exposes the requested single_sign_on as @single_sign_on" do
        SingleSignOn.stub!(:get!).and_return(mock_single_sign_on(:update_attributes => true))
        put :update, :id => "1"
        assigns(:single_sign_on).should equal(mock_single_sign_on)
      end

      it "redirects to the single_sign_on" do
        SingleSignOn.stub!(:get!).and_return(mock_single_sign_on(:update_attributes => true))
        put :update, :id => "1"
        response.should redirect_to(single_sign_on_url(mock_single_sign_on))
      end

    end
    
    describe "with invalid params" do

      it "updates the requested single_sign_on" do
        SingleSignOn.should_receive(:get!).with("37").and_return(mock_single_sign_on)
        mock_single_sign_on.should_receive(:update_attributes).with({'these' => 'params'})
        mock_single_sign_on.should_receive(:dirty?)
        put :update, :id => "37", :single_sign_on => {:these => 'params'}
      end

      it "exposes the single_sign_on as @single_sign_on" do
        SingleSignOn.stub!(:get!).and_return(mock_single_sign_on(:update_attributes => false))
        mock_single_sign_on.should_receive(:dirty?)
        put :update, :id => "1"
        assigns(:single_sign_on).should equal(mock_single_sign_on)
      end

      it "re-renders the 'edit' template" do
        SingleSignOn.stub!(:get!).and_return(mock_single_sign_on(:update_attributes => false, :dirty? => true))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

  describe "DELETE destroy" do

    it "destroys the requested single_sign_on" do
      SingleSignOn.should_receive(:get).with("37").and_return(mock_single_sign_on)
      mock_single_sign_on.should_receive(:destroy)
      delete :destroy, :id => "37"
    end
  
    it "redirects to the single_sign_ons list" do
      SingleSignOn.should_receive(:get).with("1").and_return(mock_single_sign_on(:destroy => true))
      delete :destroy, :id => "1"
      response.should redirect_to(single_sign_ons_url)
    end

  end

end
