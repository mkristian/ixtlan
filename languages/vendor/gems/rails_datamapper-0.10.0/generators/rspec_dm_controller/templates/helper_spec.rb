require File.expand_path(File.dirname(__FILE__) + '<%= '/..' * class_nesting_depth %>/../spec_helper')
require 'active_record'

describe <%= class_name %>Helper do
  before(:each) do
    include <%= class_name %>Helper
  end

  it "should be valid" do
  end
end
