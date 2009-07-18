require 'pathname'

__dir__ = Pathname(__FILE__).dirname.expand_path
require __dir__.parent.parent + 'spec_helper'
require __dir__ + 'spec_helper'

describe DataMapper::Validate::Fixtures::BetaTesterAccount do
  before :each do
    @model = DataMapper::Validate::Fixtures::BetaTesterAccount.new(:user_agreement    => true,
                                                                   :newsletter_signup => "",
                                                                   :privacy_agreement => "accept")
    @model.should be_valid
  end

  describe "with a missing newsletter signup field" do
    before :each do
      @model.newsletter_signup = nil
    end

    it "is perfectly valid" do
      @model.should be_valid
    end
  end

  describe "with a blank newsletter signup field" do
    before :each do
      @model.newsletter_signup = ""
    end

    it "is perfectly valid" do
      @model.should be_valid
    end
  end


  describe "with a blank user agreement field" do
    before :each do
      @model.user_agreement = ""
    end

    it "is NOT valid" do
      @model.should_not be_valid
    end
  end


  describe "with a nil user agreement field" do
    before :each do
      @model.user_agreement = nil
    end

    it "is NOT valid" do
      @model.should_not be_valid
    end
  end


  describe "with user agreement field having value of 1 (as integer)" do
    before :each do
      @model.user_agreement = 1
    end

    it "is valid" do
      @model.should be_valid
    end
  end


  describe "with user agreement field having value of 1 (as a string)" do
    before :each do
      @model.user_agreement = "1"
    end

    it "is valid" do
      @model.should be_valid
    end
  end


  describe "with user agreement field having value of 'true' (as a string)" do
    before :each do
      @model.user_agreement = 'true'
    end

    it "is valid" do
      @model.should be_valid
    end
  end


  describe "with user agreement field having value of true (TrueClass instance)" do
    before :each do
      @model.user_agreement = true
    end

    it "is valid" do
      @model.should be_valid
    end
  end


  describe "with user agreement field having value of 't' (The Lisp Way)" do
    before :each do
      @model.user_agreement = 't'
    end

    it "is valid" do
      @model.should be_valid
    end
  end


  describe "with user agreement field having value of 'f'" do
    before :each do
      @model.user_agreement = 'f'
    end

    it "is NOT valid" do
      @model.should_not be_valid
    end
  end


  describe "with privacy agreement field having value of 1" do
    before :each do
      @model.privacy_agreement = 1
    end

    it "is NOT valid" do
      @model.should_not be_valid
    end
  end

  describe "with privacy agreement field having value of true" do
    before :each do
      @model.privacy_agreement = true
    end

    it "is NOT valid" do
      @model.should_not be_valid
    end
  end


  describe "with privacy agreement field having value of '1'" do
    before :each do
      @model.privacy_agreement = '1'
    end

    it "is NOT valid" do
      @model.should_not be_valid
    end
  end


  describe "with privacy agreement field having value of 't'" do
    before :each do
      @model.privacy_agreement = 't'
    end

    it "is NOT valid" do
      @model.should_not be_valid
    end
  end


  describe "with privacy agreement field having value of 'accept'" do
    before :each do
      @model.privacy_agreement = 'accept'
    end

    it "is valid" do
      @model.should be_valid
    end
  end


  describe "with privacy agreement field having value of 'agreed'" do
    before :each do
      @model.privacy_agreement = 'agreed'
    end

    it "is valid" do
      @model.should be_valid
    end
  end


  describe "with privacy agreement field having value of 'ah, greed'" do
    before :each do
      @model.privacy_agreement = 'ah, greed'
    end

    it "is NOT valid" do
      # greed is invalid? can't be
      @model.should_not be_valid
    end
  end
end # describe DataMapper::Validate::Fixtures::BetaTesterAccount do
