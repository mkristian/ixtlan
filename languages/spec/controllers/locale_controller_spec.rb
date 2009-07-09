require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe LocaleController do

  def mock_locale(stubs={})
    @mock_locale ||= mock_model(Locale, stubs)
  end
  
  describe "GET index" do

    it "exposes all localeses as @localeses" do
      Locale.should_receive(:all).and_return([mock_locale])
      get :index
      assigns[:locales].should == [mock_locale]
    end

    describe "with mime type of xml" do
  
      it "renders all localeses as xml" do
        Locale.should_receive(:all).and_return(locales = mock("Array of Locales"))
        locales.should_receive(:to_xml).and_return("generated XML")
        get :index, :format => 'xml'
        response.body.should == "generated XML"
      end
    
    end

  end

  describe "GET show" do

    it "exposes the requested locale as @locale" do
      Locale.should_receive(:get!).with("37").and_return(mock_locale)
      get :show, :id => "37"
      assigns[:locale].should equal(mock_locale)
    end
    
    describe "with mime type of xml" do

      it "renders the requested locale as xml" do
        Locale.should_receive(:get!).with("37").and_return(mock_locale)
        mock_locale.should_receive(:to_xml).and_return("generated XML")
        get :show, :id => "37", :format => 'xml'
        response.body.should == "generated XML"
      end

    end
    
  end

  describe "GET new" do
  
    it "exposes a new locale as @locale" do
      Locale.should_receive(:new).and_return(mock_locale)
      get :new
      assigns[:locale].should equal(mock_locale)
    end

  end

  describe "GET edit" do
  
    it "exposes the requested locale as @locale" do
      Locale.should_receive(:get!).with("37").and_return(mock_locale)
      get :edit, :id => "37"
      assigns[:locale].should equal(mock_locale)
    end

  end

  describe "POST create" do

    describe "with valid params" do
      
      it "exposes a newly created locale as @locale" do
        Locale.should_receive(:new).with({'these' => 'params'}).and_return(mock_locale(:save => true))
        post :create, :locale => {:these => 'params'}
        assigns(:locale).should equal(mock_locale)
      end

      it "redirects to the created locale" do
        Locale.stub!(:new).and_return(mock_locale(:save => true))
        post :create, :locale => {}
        response.should redirect_to(locale_url(mock_locale))
      end
      
    end
    
    describe "with invalid params" do

      it "exposes a newly created but unsaved locale as @locale" do
        Locale.stub!(:new).with({'these' => 'params'}).and_return(mock_locale(:save => false))
        post :create, :locale => {:these => 'params'}
        assigns(:locale).should equal(mock_locale)
      end

      it "re-renders the 'new' template" do
        Locale.stub!(:new).and_return(mock_locale(:save => false))
        post :create, :locale => {}
        response.should render_template('new')
      end
      
    end
    
  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested locale" do
        Locale.should_receive(:get!).with("37").and_return(mock_locale)
        mock_locale.should_receive(:update_attributes).with({'these' => 'params'})
        mock_locale.should_receive(:dirty?)
        put :update, :id => "37", :locale => {:these => 'params'}
      end

      it "exposes the requested locale as @locale" do
        Locale.stub!(:get!).and_return(mock_locale(:update_attributes => true))
        put :update, :id => "1"
        assigns(:locale).should equal(mock_locale)
      end

      it "redirects to the locale" do
        Locale.stub!(:get!).and_return(mock_locale(:update_attributes => true))
        put :update, :id => "1"
        response.should redirect_to(locale_url(mock_locale))
      end

    end
    
    describe "with invalid params" do

      it "updates the requested locale" do
        Locale.should_receive(:get!).with("37").and_return(mock_locale)
        mock_locale.should_receive(:update_attributes).with({'these' => 'params'})
        mock_locale.should_receive(:dirty?)
        put :update, :id => "37", :locale => {:these => 'params'}
      end

      it "exposes the locale as @locale" do
        Locale.stub!(:get!).and_return(mock_locale(:update_attributes => false))
        mock_locale.should_receive(:dirty?)
        put :update, :id => "1"
        assigns(:locale).should equal(mock_locale)
      end

      it "re-renders the 'edit' template" do
        Locale.stub!(:get!).and_return(mock_locale(:update_attributes => false, :dirty? => true))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

  describe "DELETE destroy" do

    it "destroys the requested locale" do
      Locale.should_receive(:get).with("37").and_return(mock_locale)
      mock_locale.should_receive(:destroy)
      delete :destroy, :id => "37"
    end
  
    it "redirects to the locales list" do
      Locale.should_receive(:get).with("1").and_return(mock_locale(:destroy => true))
      delete :destroy, :id => "1"
      response.should redirect_to(locales_url)
    end
  end
end
