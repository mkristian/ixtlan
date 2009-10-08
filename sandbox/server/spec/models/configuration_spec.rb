require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe Configuration do
  before(:each) do
    @valid_attributes = {
      :session_idle_timeout => 1,
      :password_length => 1,
      :keep_audit_logs => 1
    }
  end

  it "should require session_idle_timeout" do
    configuration = Configuration.create(@valid_attributes.merge(:session_idle_timeout => nil))
    configuration.errors.on(:session_idle_timeout).should_not == nil
  end


  it "should be numerical session_idle_timeout" do
    configuration = Configuration.create(@valid_attributes.merge(:session_idle_timeout => "none-numberic" ))
    configuration.session_idle_timeout.to_i.should == 0
    configuration.errors.size.should == 1
  end

  it "should require password_length" do
    configuration = Configuration.create(@valid_attributes.merge(:password_length => nil))
    configuration.errors.on(:password_length).should_not == nil
  end


  it "should be numerical password_length" do
    configuration = Configuration.create(@valid_attributes.merge(:password_length => "none-numberic" ))
    configuration.password_length.to_i.should == 0
    configuration.errors.size.should == 1
  end

  it "should require keep_audit_logs" do
    configuration = Configuration.create(@valid_attributes.merge(:keep_audit_logs => nil))
    configuration.errors.on(:keep_audit_logs).should_not == nil
  end


  it "should be numerical keep_audit_logs" do
    configuration = Configuration.create(@valid_attributes.merge(:keep_audit_logs => "none-numberic" ))
    configuration.keep_audit_logs.to_i.should == 0
    configuration.errors.size.should == 1
  end

end
