require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe CourseTypeLocationsController do

  def mock_course_type_location(stubs={})
    @mock_course_type_location ||= mock_model(CourseTypeLocation, stubs)
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

    it "exposes all course_type_locationses as @course_type_locationses" do
      CourseTypeLocation.should_receive(:all).and_return([mock_course_type_location])
      get :index
      assigns[:course_type_locations].should == [mock_course_type_location]
    end

    describe "with mime type of xml" do
  
      it "renders all course_type_locationses as xml" do
        CourseTypeLocation.should_receive(:all).and_return(course_type_locations = mock("Array of CourseTypeLocations"))
        course_type_locations.should_receive(:to_xml).and_return("generated XML")
        get :index, :format => 'xml'
        response.body.should == "generated XML"
      end
    
    end

  end

  describe "GET show" do

    it "exposes the requested course_type_location as @course_type_location" do
      CourseTypeLocation.should_receive(:get!).with("37").and_return(mock_course_type_location)
      get :show, :id => "37"
      assigns[:course_type_location].should equal(mock_course_type_location)
    end
    
    describe "with mime type of xml" do

      it "renders the requested course_type_location as xml" do
        CourseTypeLocation.should_receive(:get!).with("37").and_return(mock_course_type_location)
        mock_course_type_location.should_receive(:to_xml).and_return("generated XML")
        get :show, :id => "37", :format => 'xml'
        response.body.should == "generated XML"
      end

    end
    
  end

  describe "GET new" do
  
    it "exposes a new course_type_location as @course_type_location" do
      CourseTypeLocation.should_receive(:new).and_return(mock_course_type_location)
      get :new
      assigns[:course_type_location].should equal(mock_course_type_location)
    end

  end

  describe "GET edit" do
  
    it "exposes the requested course_type_location as @course_type_location" do
      CourseTypeLocation.should_receive(:get!).with("37").and_return(mock_course_type_location)
      get :edit, :id => "37"
      assigns[:course_type_location].should equal(mock_course_type_location)
    end

  end

  describe "POST create" do

    describe "with valid params" do
      
      it "exposes a newly created course_type_location as @course_type_location" do
        CourseTypeLocation.should_receive(:new).with({'these' => 'params'}).and_return(mock_course_type_location(:save => true))
        post :create, :course_type_location => {:these => 'params'}
        assigns(:course_type_location).should equal(mock_course_type_location)
      end

      it "redirects to the created course_type_location" do
        CourseTypeLocation.stub!(:new).and_return(mock_course_type_location(:save => true))
        post :create, :course_type_location => {}
        response.should redirect_to(course_type_location_url(mock_course_type_location))
      end
      
    end
    
    describe "with invalid params" do

      it "exposes a newly created but unsaved course_type_location as @course_type_location" do
        CourseTypeLocation.stub!(:new).with({'these' => 'params'}).and_return(mock_course_type_location(:save => false))
        post :create, :course_type_location => {:these => 'params'}
        assigns(:course_type_location).should equal(mock_course_type_location)
      end

      it "re-renders the 'new' template" do
        CourseTypeLocation.stub!(:new).and_return(mock_course_type_location(:save => false))
        post :create, :course_type_location => {}
        response.should render_template('new')
      end
      
    end
    
  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested course_type_location" do
        CourseTypeLocation.should_receive(:get!).with("37").and_return(mock_course_type_location)
        mock_course_type_location.should_receive(:update_attributes).with({'these' => 'params'})
        mock_course_type_location.should_receive(:dirty?)
        put :update, :id => "37", :course_type_location => {:these => 'params'}
      end

      it "exposes the requested course_type_location as @course_type_location" do
        CourseTypeLocation.stub!(:get!).and_return(mock_course_type_location(:update_attributes => true))
        put :update, :id => "1"
        assigns(:course_type_location).should equal(mock_course_type_location)
      end

      it "redirects to the course_type_location" do
        CourseTypeLocation.stub!(:get!).and_return(mock_course_type_location(:update_attributes => true))
        put :update, :id => "1"
        response.should redirect_to(course_type_location_url(mock_course_type_location))
      end

    end
    
    describe "with invalid params" do

      it "updates the requested course_type_location" do
        CourseTypeLocation.should_receive(:get!).with("37").and_return(mock_course_type_location)
        mock_course_type_location.should_receive(:update_attributes).with({'these' => 'params'})
        mock_course_type_location.should_receive(:dirty?)
        put :update, :id => "37", :course_type_location => {:these => 'params'}
      end

      it "exposes the course_type_location as @course_type_location" do
        CourseTypeLocation.stub!(:get!).and_return(mock_course_type_location(:update_attributes => false))
        mock_course_type_location.should_receive(:dirty?)
        put :update, :id => "1"
        assigns(:course_type_location).should equal(mock_course_type_location)
      end

      it "re-renders the 'edit' template" do
        CourseTypeLocation.stub!(:get!).and_return(mock_course_type_location(:update_attributes => false, :dirty? => true))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

  describe "DELETE destroy" do

    it "destroys the requested course_type_location" do
      CourseTypeLocation.should_receive(:get).with("37").and_return(mock_course_type_location)
      mock_course_type_location.should_receive(:destroy)
      delete :destroy, :id => "37"
    end
  
    it "redirects to the course_type_locations list" do
      CourseTypeLocation.should_receive(:get).with("1").and_return(mock_course_type_location(:destroy => true))
      delete :destroy, :id => "1"
      response.should redirect_to(course_type_locations_url)
    end

  end

end
