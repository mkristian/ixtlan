require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe DispatchesController do
  describe "route generation" do
   
    it "maps #create" do
      route_for(:controller => "dispatches", :action => "create").should == {:path => "/dispatches", :method => :post}
    end
  end

  describe "route recognition" do
    
    it "generates params for #create" do
      params_from(:post, "/dispatches").should == {:controller => "dispatches", :action => "create"}
    end
  
  end
end
