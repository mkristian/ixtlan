require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe SingleSignOnsController do
  describe "route generation" do
    it "maps #index" do
      route_for(:controller => "single_sign_ons", :action => "index").should == "/single_sign_ons"
    end
  
    it "maps #new" do
      route_for(:controller => "single_sign_ons", :action => "new").should == "/single_sign_ons/new"
    end
  
    it "maps #show" do
      route_for(:controller => "single_sign_ons", :action => "show", :id => "1").should == "/single_sign_ons/1"
    end
  
    it "maps #edit" do
      route_for(:controller => "single_sign_ons", :action => "edit", :id => "1").should == "/single_sign_ons/1/edit"
    end

  it "maps #create" do
    route_for(:controller => "single_sign_ons", :action => "create").should == {:path => "/single_sign_ons", :method => :post}
  end

  it "maps #update" do
    route_for(:controller => "single_sign_ons", :action => "update", :id => "1").should == {:path =>"/single_sign_ons/1", :method => :put}
  end
  
    it "maps #destroy" do
      route_for(:controller => "single_sign_ons", :action => "destroy", :id => "1").should == {:path =>"/single_sign_ons/1", :method => :delete}
    end
  end

  describe "route recognition" do
    it "generates params for #index" do
      params_from(:get, "/single_sign_ons").should == {:controller => "single_sign_ons", :action => "index"}
    end
  
    it "generates params for #new" do
      params_from(:get, "/single_sign_ons/new").should == {:controller => "single_sign_ons", :action => "new"}
    end
  
    it "generates params for #create" do
      params_from(:post, "/single_sign_ons").should == {:controller => "single_sign_ons", :action => "create"}
    end
  
    it "generates params for #show" do
      params_from(:get, "/single_sign_ons/1").should == {:controller => "single_sign_ons", :action => "show", :id => "1"}
    end
  
    it "generates params for #edit" do
      params_from(:get, "/single_sign_ons/1/edit").should == {:controller => "single_sign_ons", :action => "edit", :id => "1"}
    end
  
    it "generates params for #update" do
      params_from(:put, "/single_sign_ons/1").should == {:controller => "single_sign_ons", :action => "update", :id => "1"}
    end
  
    it "generates params for #destroy" do
      params_from(:delete, "/single_sign_ons/1").should == {:controller => "single_sign_ons", :action => "destroy", :id => "1"}
    end
  end
end
