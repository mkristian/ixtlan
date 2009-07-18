require 'pathname'

__dir__ = Pathname(__FILE__).dirname.expand_path
require __dir__.parent.parent + 'spec_helper'
require __dir__ + 'spec_helper'

describe 'Inferred validations' do
  it "allow overriding a single error message" do
    custom_boat = Class.new do
      include DataMapper::Resource
      property :id,   DataMapper::Types::Serial
      property :name, String,  :nullable => false, :message => "This boat must have name"
    end
    boat = custom_boat.new
    boat.should_not be_valid
    boat.errors.on(:name).should include('This boat must have name')
  end

  it "should have correct error messages" do
    custom_boat = Class.new do
      include DataMapper::Resource
      property :id,   DataMapper::Types::Serial
      property :name, String,  :nullable => false, :length => 5..20, :format => /^[a-z]+$/,
               :messages => {
                 :presence => "This boat must have name",
                 :length => "Name must have at least 4 and at most 20 chars",
                 :format => "Please use only small letters"
               }
    end

    boat = custom_boat.new
    boat.should_not be_valid
    boat.errors.on(:name).should include("This boat must have name")
    boat.errors.on(:name).should include("Name must have at least 4 and at most 20 chars")
    boat.errors.on(:name).should include("Please use only small letters")

    boat.name = "%%"
    boat.should_not be_valid
    boat.errors.on(:name).should_not include("This boat must have name")
    boat.errors.on(:name).should include("Name must have at least 4 and at most 20 chars")
    boat.errors.on(:name).should include("Please use only small letters")

    boat.name = "%%asd"
    boat.should_not be_valid
    boat.errors.on(:name).should_not include("This boat must have name")
    boat.errors.on(:name).should_not include("Name must have at least 4 and at most 20 chars")
    boat.errors.on(:name).should include("Please use only small letters")

    boat.name = "superboat"
    boat.should be_valid
    boat.errors.on(:name).should be_nil
  end
end
