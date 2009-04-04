require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe ConfigurationsController do
  describe "route generation" do
    
    it "maps #show" do
      route_for(:controller => "configurations", :action => "show").should == "/configuration"
    end
  
    it "maps #edit" do
      route_for(:controller => "configurations", :action => "edit").should == "/configuration/edit"
    end

    it "maps #update" do
      route_for(:controller => "configurations", :action => "update").should == {:path =>"/configuration", :method => :put}
    end
    
  end

  describe "route recognition" do
  
    it "generates params for #create" do
      params_from(:post, "/configuration").should == {:controller => "configurations", :action => "create"}
    end
  
    it "generates params for #show" do
      params_from(:get, "/configuration").should == {:controller => "configurations", :action => "show"}
    end
  
    it "generates params for #edit" do
      params_from(:get, "/configuration/edit").should == {:controller => "configurations", :action => "edit"}
    end
  
    it "generates params for #edit" do
      params_from(:post, "/configuration/edit").should == {:controller => "configurations", :action => "edit"}
    end
  
    it "generates params for #edit" do
      params_from(:delete, "/configuration/edit").should == {:controller => "configurations", :action => "edit"}
    end
  
    it "generates params for #update" do
      params_from(:put, "/configuration").should == {:controller => "configurations", :action => "update"}
    end
  
    it "generates params for #destroy" do
      params_from(:delete, "/configuration").should == {:controller => "configurations", :action => "destroy"}
    end
  end
end
