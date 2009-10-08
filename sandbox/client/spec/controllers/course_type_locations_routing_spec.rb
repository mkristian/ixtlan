require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe CourseTypeLocationsController do
  describe "route generation" do
    it "maps #index" do
      route_for(:controller => "course_type_locations", :action => "index").should == "/course_type_locations"
    end
  
    it "maps #new" do
      route_for(:controller => "course_type_locations", :action => "new").should == "/course_type_locations/new"
    end
  
    it "maps #show" do
      route_for(:controller => "course_type_locations", :action => "show", :id => "1").should == "/course_type_locations/1"
    end
  
    it "maps #edit" do
      route_for(:controller => "course_type_locations", :action => "edit", :id => "1").should == "/course_type_locations/1/edit"
    end

  it "maps #create" do
    route_for(:controller => "course_type_locations", :action => "create").should == {:path => "/course_type_locations", :method => :post}
  end

  it "maps #update" do
    route_for(:controller => "course_type_locations", :action => "update", :id => "1").should == {:path =>"/course_type_locations/1", :method => :put}
  end
  
    it "maps #destroy" do
      route_for(:controller => "course_type_locations", :action => "destroy", :id => "1").should == {:path =>"/course_type_locations/1", :method => :delete}
    end
  end

  describe "route recognition" do
    it "generates params for #index" do
      params_from(:get, "/course_type_locations").should == {:controller => "course_type_locations", :action => "index"}
    end
  
    it "generates params for #new" do
      params_from(:get, "/course_type_locations/new").should == {:controller => "course_type_locations", :action => "new"}
    end
  
    it "generates params for #create" do
      params_from(:post, "/course_type_locations").should == {:controller => "course_type_locations", :action => "create"}
    end
  
    it "generates params for #show" do
      params_from(:get, "/course_type_locations/1").should == {:controller => "course_type_locations", :action => "show", :id => "1"}
    end
  
    it "generates params for #edit" do
      params_from(:get, "/course_type_locations/1/edit").should == {:controller => "course_type_locations", :action => "edit", :id => "1"}
    end
  
    it "generates params for #update" do
      params_from(:put, "/course_type_locations/1").should == {:controller => "course_type_locations", :action => "update", :id => "1"}
    end
  
    it "generates params for #destroy" do
      params_from(:delete, "/course_type_locations/1").should == {:controller => "course_type_locations", :action => "destroy", :id => "1"}
    end
  end
end
