require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe LocalesController do
  describe "route generation" do
    it "maps #index" do
      route_for(:controller => "locales", :action => "index").should == "/locales"
    end

    it "maps #new" do
      route_for(:controller => "locales", :action => "new").should == "/locales/new"
    end

    it "maps #show" do
      route_for(:controller => "locales", :action => "show", :id => "1").should == "/locales/1"
    end

    it "maps #edit" do
      route_for(:controller => "locales", :action => "edit", :id => "1").should == "/locales/1/edit"
    end

    it "maps #create" do
      route_for(:controller => "locales", :action => "create").should == {:path => "/locales", :method => :post}
    end

    it "maps #update" do
      route_for(:controller => "locales", :action => "update", :id => "1").should == {:path =>"/locales/1", :method => :put}
    end

    it "maps #destroy" do
      route_for(:controller => "locales", :action => "destroy", :id => "1").should == {:path =>"/locales/1", :method => :delete}
    end
  end

  describe "route recognition" do
    it "generates params for #index" do
      params_from(:get, "/locales").should == {:controller => "locales", :action => "index"}
    end

    it "generates params for #new" do
      params_from(:get, "/locales/new").should == {:controller => "locales", :action => "new"}
    end

    it "generates params for #create" do
      params_from(:post, "/locales").should == {:controller => "locales", :action => "create"}
    end

    it "generates params for #show" do
      params_from(:get, "/locales/1").should == {:controller => "locales", :action => "show", :id => "1"}
    end

    it "generates params for #edit" do
      params_from(:get, "/locales/1/edit").should == {:controller => "locales", :action => "edit", :id => "1"}
    end

    it "generates params for #update" do
      params_from(:put, "/locales/1").should == {:controller => "locales", :action => "update", :id => "1"}
    end

    it "generates params for #destroy" do
      params_from(:delete, "/locales/1").should == {:controller => "locales", :action => "destroy", :id => "1"}
    end
  end
end
