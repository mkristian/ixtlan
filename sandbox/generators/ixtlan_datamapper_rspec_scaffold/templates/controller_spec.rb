require File.expand_path(File.dirname(__FILE__) + '<%= '/..' * class_nesting_depth %>/../spec_helper')

describe <%= controller_class_name %>Controller do

  def mock_<%= file_name %>(stubs={})
    @mock_<%= file_name %> ||= mock_model(<%= class_name %>, stubs)
  end

  def mock_array(*args)
    a = args
    def a.model
      <%= class_name %>
    end
    a
  end

  def mock_arguments(merge = {})
    args = merge
<% unless options[:skip_modified_by] -%>
    args.merge!(:current_user= => nil)
<% end -%>
<% unless options[:skip_audit] -%>
    args.merge!(:model => <%= class_name %>, :key => 12)
<% end -%>
  args
  end

<% unless options[:skip_guard] -%>
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
<% end -%>

  describe "GET index" do

    it "exposes all <%= table_name %> as @<%= table_name %>" do
      <%= class_name %>.should_receive(:all).and_return(mock_array(mock_<%= file_name %>))
      get :index
      assigns[:<%= table_name %>].should == mock_array(mock_<%= file_name %>)
    end

    describe "with mime type of xml" do

      it "renders all <%= table_name.pluralize %> as xml" do
        <%= class_name %>.should_receive(:all).and_return(<%= file_name.pluralize %> = mock_array("Array of <%= class_name.pluralize %>"))
        <%= file_name.pluralize %>.should_receive(:to_xml).and_return("generated XML")
        get :index, :format => 'xml'
        response.body.should == "generated XML"
      end

    end

  end

  describe "GET show" do

    it "exposes the requested <%= file_name %> as @<%= file_name %>" do
      <%= class_name %>.should_receive(:get!).with("37").and_return(mock_<%= file_name %>(mock_arguments))
      get :show, :id => "37"
      assigns[:<%= file_name %>].should equal(mock_<%= file_name %>)
    end

    describe "with mime type of xml" do

      it "renders the requested <%= file_name %> as xml" do
        <%= class_name %>.should_receive(:get!).with("37").and_return(mock_<%= file_name %>(mock_arguments))
        mock_<%= file_name %>.should_receive(:to_xml).and_return("generated XML")
        get :show, :id => "37", :format => 'xml'
        response.body.should == "generated XML"
      end

    end

  end

  describe "GET new" do

    it "exposes a new <%= file_name %> as @<%= file_name %>" do
      <%= class_name %>.should_receive(:new).and_return(mock_<%= file_name %>(mock_arguments))
      get :new
      assigns[:<%= file_name %>].should equal(mock_<%= file_name %>)
    end

  end

  describe "GET edit" do

    it "exposes the requested <%= file_name %> as @<%= file_name %>" do
      <%= class_name %>.should_receive(:get!).with("37").and_return(mock_<%= file_name %>(mock_arguments))
      get :edit, :id => "37"
      assigns[:<%= file_name %>].should equal(mock_<%= file_name %>)
    end

  end

  describe "POST create" do

    describe "with valid params" do

      it "exposes a newly created <%= file_name %> as @<%= file_name %>" do
        <%= class_name %>.should_receive(:new).with({'these' => 'params'}).and_return(mock_<%= file_name %>(mock_arguments(:save => true)))
        post :create, :<%= file_name %> => {:these => 'params'}
        assigns(:<%= file_name %>).should equal(mock_<%= file_name %>)
      end

      it "redirects to the created <%= file_name %>" do
        <%= class_name %>.stub!(:new).and_return(mock_<%= file_name %>(mock_arguments(:save => true)))
        post :create, :<%= file_name %> => {}
        response.should redirect_to(<%= table_name.singularize %>_url(mock_<%= file_name %>))
      end

    end

    describe "with invalid params" do

      it "exposes a newly created but unsaved <%= file_name %> as @<%= file_name %>" do
        <%= class_name %>.stub!(:new).with({'these' => 'params'}).and_return(mock_<%= file_name %>(mock_arguments(:save => false)))
        post :create, :<%= file_name %> => {:these => 'params'}
        assigns(:<%= file_name %>).should equal(mock_<%= file_name %>)
      end

      it "re-renders the 'new' template" do
        <%= class_name %>.stub!(:new).and_return(mock_<%= file_name %>(mock_arguments(:save => false)))
        post :create, :<%= file_name %> => {}
        response.should render_template('new')
      end

    end

  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested <%= file_name %>" do
        <%= class_name %>.should_receive(:get!).with("37").and_return(mock_<%= file_name %>(mock_arguments))
        mock_<%= file_name %>.should_receive(:update).with({'these' => 'params'})
        mock_<%= file_name %>.should_receive(:dirty?)
        put :update, :id => "37", :<%= file_name %> => {:these => 'params'}
      end

      it "exposes the requested <%= file_name %> as @<%= file_name %>" do
        <%= class_name %>.stub!(:get!).and_return(mock_<%= file_name %>(mock_arguments(:update => true)))
        put :update, :id => "1"
        assigns(:<%= file_name %>).should equal(mock_<%= file_name %>)
      end

      it "redirects to the <%= file_name %>" do
        <%= class_name %>.stub!(:get!).and_return(mock_<%= file_name %>(mock_arguments(:update => true)))
        put :update, :id => "1"
        response.should redirect_to(<%= table_name.singularize %>_url(mock_<%= file_name %>))
      end

    end

    describe "with invalid params" do

      it "updates the requested <%= file_name %>" do
        <%= class_name %>.should_receive(:get!).with("37").and_return(mock_<%= file_name %>(mock_arguments))
        mock_<%= file_name %>.should_receive(:update).with({'these' => 'params'})
        mock_<%= file_name %>.should_receive(:dirty?)
        put :update, :id => "37", :<%= file_name %> => {:these => 'params'}
      end

      it "exposes the <%= file_name %> as @<%= file_name %>" do
        <%= class_name %>.stub!(:get!).and_return(mock_<%= file_name %>(mock_arguments(:update => false)))
        mock_<%= file_name %>.should_receive(:dirty?)
        put :update, :id => "1"
        assigns(:<%= file_name %>).should equal(mock_<%= file_name %>)
      end

      it "re-renders the 'edit' template" do
        <%= class_name %>.stub!(:get!).and_return(mock_<%= file_name %>(mock_arguments(:update => false, :dirty? => true)))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

  describe "DELETE destroy" do

    it "destroys the requested <%= file_name %>" do
      <%= class_name %>.should_receive(:get).with("37").and_return(mock_<%= file_name %>(mock_arguments))
      mock_<%= file_name %>.should_receive(:destroy)
      delete :destroy, :id => "37"
    end

    it "redirects to the <%= table_name %> list" do
      <%= class_name %>.should_receive(:get).with("1").and_return(mock_<%= file_name %>(mock_arguments(:destroy => true)))
      delete :destroy, :id => "1"
      response.should redirect_to(<%= table_name %>_url)
    end

  end

end
