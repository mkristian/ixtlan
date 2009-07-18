require 'pathname'

__dir__ = Pathname(__FILE__).dirname.expand_path
require __dir__.parent.parent + 'spec_helper'
require __dir__ + 'spec_helper'


describe DataMapper::Validate::Fixtures::BillOfLading do
  def valid_attributes
    { :id => 1, :doc_no => 'A1234', :email => 'user@example.com', :url => 'http://example.com' }
  end

  describe "with code of 123456" do
    before :all do
      @model = DataMapper::Validate::Fixtures::BillOfLading.new(valid_attributes.merge(:code => '123456'))
    end

    it_should_behave_like 'valid model'
  end


  describe "with code of 12" do
    before :all do
      @model = DataMapper::Validate::Fixtures::BillOfLading.new(valid_attributes.merge(:code => '12'))
    end

    it_should_behave_like 'invalid model'

    it "has a meaningful error message" do
      @model.errors.on(:code).should include("Code format is invalid")
    end
  end
end
