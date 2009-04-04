require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe DispatchesController do


  describe "POST create" do

    describe "with valid params" do
      
     
      it "redirects to the created dispatch" do
        post :create, :url => 'http://example.com'
        response.should redirect_to("http://example.com")
      end
      
    end
    
    describe "with invalid params" do

      it "re-renders the current page" do
        post :create
        response.should render_template('error')
      end
      
    end
    
  end

end
