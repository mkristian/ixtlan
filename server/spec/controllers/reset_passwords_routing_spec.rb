require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe ResetPasswordsController do
  describe "route generation" do
    it "maps #index" do
      route_for(:controller => "reset_passwords", :action => "index").should == "/reset_passwords"
    end
  
    it "maps #new" do
      route_for(:controller => "reset_passwords", :action => "new").should == "/reset_passwords/new"
    end
  
    it "maps #show" do
      route_for(:controller => "reset_passwords", :action => "show", :id => "1").should == "/reset_passwords/1"
    end
  
    it "maps #edit" do
      route_for(:controller => "reset_passwords", :action => "edit", :id => "1").should == "/reset_passwords/1/edit"
    end

  it "maps #create" do
    route_for(:controller => "reset_passwords", :action => "create").should == {:path => "/reset_passwords", :method => :post}
  end

  it "maps #update" do
    route_for(:controller => "reset_passwords", :action => "update", :id => "1").should == {:path =>"/reset_passwords/1", :method => :put}
  end
  
    it "maps #destroy" do
      route_for(:controller => "reset_passwords", :action => "destroy", :id => "1").should == {:path =>"/reset_passwords/1", :method => :delete}
    end
  end

  describe "route recognition" do
    it "generates params for #index" do
      params_from(:get, "/reset_passwords").should == {:controller => "reset_passwords", :action => "index"}
    end
  
    it "generates params for #new" do
      params_from(:get, "/reset_passwords/new").should == {:controller => "reset_passwords", :action => "new"}
    end
  
    it "generates params for #create" do
      params_from(:post, "/reset_passwords").should == {:controller => "reset_passwords", :action => "create"}
    end
  
    it "generates params for #show" do
      params_from(:get, "/reset_passwords/1").should == {:controller => "reset_passwords", :action => "show", :id => "1"}
    end
  
    it "generates params for #edit" do
      params_from(:get, "/reset_passwords/1/edit").should == {:controller => "reset_passwords", :action => "edit", :id => "1"}
    end
  
    it "generates params for #update" do
      params_from(:put, "/reset_passwords/1").should == {:controller => "reset_passwords", :action => "update", :id => "1"}
    end
  
    it "generates params for #destroy" do
      params_from(:delete, "/reset_passwords/1").should == {:controller => "reset_passwords", :action => "destroy", :id => "1"}
    end
  end
end
