require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe Locale do
  before(:each) do
    @valid_attributes = {
      :code => "vc"
    }
  end

  it "should create a new instance given valid attributes" do
    locale = Locale.create(@valid_attributes)
    locale.valid?.should be_true
  end

  it "should require code" do
    locale = Locale.create(@valid_attributes.merge(:code => nil))
    locale.errors.on(:code).should_not == nil
  end

  it 'should not match code' do
    locale = Locale.create(@valid_attributes.merge(:code => "<script" ))
    locale.errors.on(:code).should_not == nil
    locale = Locale.create(@valid_attributes.merge(:code => "sc'ript" ))
    locale.errors.on(:code).should_not == nil
    locale = Locale.create(@valid_attributes.merge(:code => "scr&ipt" ))
    locale.errors.on(:code).should_not == nil
    locale = Locale.create(@valid_attributes.merge(:code => 'scr"ipt' ))
    locale.errors.on(:code).should_not == nil
    locale = Locale.create(@valid_attributes.merge(:code => "script>" ))
    locale.errors.on(:code).should_not == nil
  end

end
