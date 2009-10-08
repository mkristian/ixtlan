require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe SingleSignOn do
  before(:each) do
    @valid_attributes = {
      :token => "value for token",
      :ip => "value for ip",
      :expired_at => Time.now,
      :one_time => "value for one_time"
    }
  end

  it "should require token" do
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:token => nil))
    single_sign_on.errors.on(:token).should_not == nil
  end

  it 'should not match token' do
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:token => "<script" ))
    single_sign_on.errors.on(:token).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:token => "sc'ript" ))
    single_sign_on.errors.on(:token).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:token => "scr&ipt" ))
    single_sign_on.errors.on(:token).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:token => 'scr"ipt' ))
    single_sign_on.errors.on(:token).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:token => "script>" ))
    single_sign_on.errors.on(:token).should_not == nil
  end

  it "should require ip" do
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:ip => nil))
    single_sign_on.errors.on(:ip).should_not == nil
  end

  it 'should not match ip' do
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:ip => "<script" ))
    single_sign_on.errors.on(:ip).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:ip => "sc'ript" ))
    single_sign_on.errors.on(:ip).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:ip => "scr&ipt" ))
    single_sign_on.errors.on(:ip).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:ip => 'scr"ipt' ))
    single_sign_on.errors.on(:ip).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:ip => "script>" ))
    single_sign_on.errors.on(:ip).should_not == nil
  end

  it "should require expired_at" do
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:expired_at => nil))
    single_sign_on.errors.on(:expired_at).should_not == nil
  end

  it "should require one_time" do
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:one_time => nil))
    single_sign_on.errors.on(:one_time).should_not == nil
  end

  it 'should not match one_time' do
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:one_time => "<script" ))
    single_sign_on.errors.on(:one_time).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:one_time => "sc'ript" ))
    single_sign_on.errors.on(:one_time).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:one_time => "scr&ipt" ))
    single_sign_on.errors.on(:one_time).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:one_time => 'scr"ipt' ))
    single_sign_on.errors.on(:one_time).should_not == nil
    single_sign_on = SingleSignOn.create(@valid_attributes.merge(:one_time => "script>" ))
    single_sign_on.errors.on(:one_time).should_not == nil
  end

end
