require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe LocalesController do

  def mock_locale(stubs={})
    @mock_locale ||= mock_model(Locale, stubs)
  end

  def mock_array(*args)
    a = args
    def a.model
      Locale
    end
    a
  end

  def mock_arguments(merge = {})
    args = merge
    args.merge!(:model => Locale, :key => 12)
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

    it "exposes all locales as @locales" do
      Locale.should_receive(:all).and_return(mock_array(mock_locale))
      get :index
      assigns[:locales].should == mock_array(mock_locale)
    end

    describe "with mime type of xml" do

      it "renders all localeses as xml" do
        Locale.should_receive(:all).and_return(locales = mock_array("Array of Locales"))
        locales.should_receive(:to_xml).and_return("generated XML")
        get :index, :format => 'xml'
        response.body.should == "generated XML"
      end

    end

  end

  describe "GET show" do

    it "exposes the requested locale as @locale" do
      Locale.should_receive(:get!).with("37").and_return(mock_locale(mock_arguments))
      get :show, :id => "37"
      assigns[:locale].should equal(mock_locale)
    end

    describe "with mime type of xml" do

      it "renders the requested locale as xml" do
        Locale.should_receive(:get!).with("37").and_return(mock_locale(mock_arguments))
        mock_locale.should_receive(:to_xml).and_return("generated XML")
        get :show, :id => "37", :format => 'xml'
        response.body.should == "generated XML"
      end

    end

  end

  describe "GET new" do

    it "exposes a new locale as @locale" do
      Locale.should_receive(:new).and_return(mock_locale(mock_arguments))
      get :new
      assigns[:locale].should equal(mock_locale)
    end

  end

  describe "GET edit" do

    it "exposes the requested locale as @locale" do
      Locale.should_receive(:get!).with("37").and_return(mock_locale(mock_arguments))
      get :edit, :id => "37"
      assigns[:locale].should equal(mock_locale)
    end

  end

  describe "POST create" do

    describe "with valid params" do

      it "exposes a newly created locale as @locale" do
        Locale.should_receive(:new).with({'these' => 'params'}).and_return(mock_locale(mock_arguments(:save => true)))
        post :create, :locale => {:these => 'params'}
        assigns(:locale).should equal(mock_locale)
      end

      it "redirects to the created locale" do
        Locale.stub!(:new).and_return(mock_locale(mock_arguments(:save => true)))
        post :create, :locale => {}
        response.should redirect_to(locale_url(mock_locale))
      end

    end

    describe "with invalid params" do

      it "exposes a newly created but unsaved locale as @locale" do
        Locale.stub!(:new).with({'these' => 'params'}).and_return(mock_locale(mock_arguments(:save => false)))
        post :create, :locale => {:these => 'params'}
        assigns(:locale).should equal(mock_locale)
      end

      it "re-renders the 'new' template" do
        Locale.stub!(:new).and_return(mock_locale(mock_arguments(:save => false)))
        post :create, :locale => {}
        response.should render_template('new')
      end

    end

  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested locale" do
        Locale.should_receive(:get!).with("37").and_return(mock_locale(mock_arguments))
        mock_locale.should_receive(:update).with({'these' => 'params'})
        mock_locale.should_receive(:dirty?)
        put :update, :id => "37", :locale => {:these => 'params'}
      end

      it "exposes the requested locale as @locale" do
        Locale.stub!(:get!).and_return(mock_locale(mock_arguments(:update => true)))
        put :update, :id => "1"
        assigns(:locale).should equal(mock_locale)
      end

      it "redirects to the locale" do
        Locale.stub!(:get!).and_return(mock_locale(mock_arguments(:update => true)))
        put :update, :id => "1"
        response.should redirect_to(locale_url(mock_locale))
      end

    end

    describe "with invalid params" do

      it "updates the requested locale" do
        Locale.should_receive(:get!).with("37").and_return(mock_locale(mock_arguments))
        mock_locale.should_receive(:update).with({'these' => 'params'})
        mock_locale.should_receive(:dirty?)
        put :update, :id => "37", :locale => {:these => 'params'}
      end

      it "exposes the locale as @locale" do
        Locale.stub!(:get!).and_return(mock_locale(mock_arguments(:update => false)))
        mock_locale.should_receive(:dirty?)
        put :update, :id => "1"
        assigns(:locale).should equal(mock_locale)
      end

      it "re-renders the 'edit' template" do
        Locale.stub!(:get!).and_return(mock_locale(mock_arguments(:update => false, :dirty? => true)))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

  describe "DELETE destroy" do

    it "destroys the requested locale" do
      Locale.should_receive(:get).with("37").and_return(mock_locale(mock_arguments))
      mock_locale.should_receive(:destroy)
      delete :destroy, :id => "37"
    end

    it "redirects to the locales list" do
      Locale.should_receive(:get).with("1").and_return(mock_locale(mock_arguments(:destroy => true)))
      delete :destroy, :id => "1"
      response.should redirect_to(locales_url)
    end

  end

end
