require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe WordController do

  def mock_word(stubs={})
    @mock_word ||= mock_model(Word, stubs)
  end
  
  describe "GET index" do

    it "exposes all wordses as @wordses" do
      Word.should_receive(:all).and_return([mock_word])
      get :index
      assigns[:words].should == [mock_word]
    end

    describe "with mime type of xml" do
  
      it "renders all wordses as xml" do
        Word.should_receive(:all).and_return(words = mock("Array of Words"))
        words.should_receive(:to_xml).and_return("generated XML")
        get :index, :format => 'xml'
        response.body.should == "generated XML"
      end
    
    end

  end

  describe "GET show" do

    it "exposes the requested word as @word" do
      Word.should_receive(:get!).with("37").and_return(mock_word)
      get :show, :id => "37"
      assigns[:word].should equal(mock_word)
    end
    
    describe "with mime type of xml" do

      it "renders the requested word as xml" do
        Word.should_receive(:get!).with("37").and_return(mock_word)
        mock_word.should_receive(:to_xml).and_return("generated XML")
        get :show, :id => "37", :format => 'xml'
        response.body.should == "generated XML"
      end

    end
    
  end

  describe "GET new" do
  
    it "exposes a new word as @word" do
      Word.should_receive(:new).and_return(mock_word)
      get :new
      assigns[:word].should equal(mock_word)
    end

  end

  describe "GET edit" do
  
    it "exposes the requested word as @word" do
      Word.should_receive(:get!).with("37").and_return(mock_word)
      get :edit, :id => "37"
      assigns[:word].should equal(mock_word)
    end

  end

  describe "POST create" do

    describe "with valid params" do
      
      it "exposes a newly created word as @word" do
        Word.should_receive(:new).with({'these' => 'params'}).and_return(mock_word(:save => true))
        post :create, :word => {:these => 'params'}
        assigns(:word).should equal(mock_word)
      end

      it "redirects to the created word" do
        Word.stub!(:new).and_return(mock_word(:save => true))
        post :create, :word => {}
        response.should redirect_to(word_url(mock_word))
      end
      
    end
    
    describe "with invalid params" do

      it "exposes a newly created but unsaved word as @word" do
        Word.stub!(:new).with({'these' => 'params'}).and_return(mock_word(:save => false))
        post :create, :word => {:these => 'params'}
        assigns(:word).should equal(mock_word)
      end

      it "re-renders the 'new' template" do
        Word.stub!(:new).and_return(mock_word(:save => false))
        post :create, :word => {}
        response.should render_template('new')
      end
      
    end
    
  end

  describe "PUT udpate" do

    describe "with valid params" do

      it "updates the requested word" do
        Word.should_receive(:get!).with("37").and_return(mock_word)
        mock_word.should_receive(:update_attributes).with({'these' => 'params'})
        mock_word.should_receive(:dirty?)
        put :update, :id => "37", :word => {:these => 'params'}
      end

      it "exposes the requested word as @word" do
        Word.stub!(:get!).and_return(mock_word(:update_attributes => true))
        put :update, :id => "1"
        assigns(:word).should equal(mock_word)
      end

      it "redirects to the word" do
        Word.stub!(:get!).and_return(mock_word(:update_attributes => true))
        put :update, :id => "1"
        response.should redirect_to(word_url(mock_word))
      end

    end
    
    describe "with invalid params" do

      it "updates the requested word" do
        Word.should_receive(:get!).with("37").and_return(mock_word)
        mock_word.should_receive(:update_attributes).with({'these' => 'params'})
        mock_word.should_receive(:dirty?)
        put :update, :id => "37", :word => {:these => 'params'}
      end

      it "exposes the word as @word" do
        Word.stub!(:get!).and_return(mock_word(:update_attributes => false))
        mock_word.should_receive(:dirty?)
        put :update, :id => "1"
        assigns(:word).should equal(mock_word)
      end

      it "re-renders the 'edit' template" do
        Word.stub!(:get!).and_return(mock_word(:update_attributes => false, :dirty? => true))
        put :update, :id => "1"
        response.should render_template('edit')
      end

    end

  end

  describe "DELETE destroy" do

    it "destroys the requested word" do
      Word.should_receive(:get).with("37").and_return(mock_word)
      mock_word.should_receive(:destroy)
      delete :destroy, :id => "37"
    end
  
    it "redirects to the words list" do
      Word.should_receive(:get).with("1").and_return(mock_word(:destroy => true))
      delete :destroy, :id => "1"
      response.should redirect_to(words_url)
    end
  end
end
