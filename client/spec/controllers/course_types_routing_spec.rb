require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe CourseTypesController do
  describe "route generation" do
    it "maps #index" do
      route_for(:controller => "course_types", :action => "index").should == "/course_types"
    end
  
    it "maps #new" do
      route_for(:controller => "course_types", :action => "new").should == "/course_types/new"
    end
  
    it "maps #show" do
      route_for(:controller => "course_types", :action => "show", :id => "1").should == "/course_types/1"
    end
  
    it "maps #edit" do
      route_for(:controller => "course_types", :action => "edit", :id => "1").should == "/course_types/1/edit"
    end

  it "maps #create" do
    route_for(:controller => "course_types", :action => "create").should == {:path => "/course_types", :method => :post}
  end

  it "maps #update" do
    route_for(:controller => "course_types", :action => "update", :id => "1").should == {:path =>"/course_types/1", :method => :put}
  end
  
    it "maps #destroy" do
      route_for(:controller => "course_types", :action => "destroy", :id => "1").should == {:path =>"/course_types/1", :method => :delete}
    end
  end

  describe "route recognition" do
    it "generates params for #index" do
      params_from(:get, "/course_types").should == {:controller => "course_types", :action => "index"}
    end
  
    it "generates params for #new" do
      params_from(:get, "/course_types/new").should == {:controller => "course_types", :action => "new"}
    end
  
    it "generates params for #create" do
      params_from(:post, "/course_types").should == {:controller => "course_types", :action => "create"}
    end
  
    it "generates params for #show" do
      params_from(:get, "/course_types/1").should == {:controller => "course_types", :action => "show", :id => "1"}
    end
  
    it "generates params for #edit" do
      params_from(:get, "/course_types/1/edit").should == {:controller => "course_types", :action => "edit", :id => "1"}
    end
  
    it "generates params for #update" do
      params_from(:put, "/course_types/1").should == {:controller => "course_types", :action => "update", :id => "1"}
    end
  
    it "generates params for #destroy" do
      params_from(:delete, "/course_types/1").should == {:controller => "course_types", :action => "destroy", :id => "1"}
    end
  end
end
