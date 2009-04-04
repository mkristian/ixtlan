require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe User do
  @@count = 0
  before(:each) do
    @@count += 1
    @valid_attributes = {
      :login => "login#{@@count}#{Time.now.usec}",
      :email => "email#{@@count}#{Time.now.usec}@example.com",
      :name => "value for name",
      :password => "passWord1",
      :password_confirmation => "passWord1",
      :created_at => Time.now,
      :updated_at => Time.now
    }
  end

  it "should create a new instance given valid attributes" do
    user = User.create(@valid_attributes)
    user.valid?.should be_true
    user.new_record?.should be_false
  end

  it 'should require login' do
    user = User.create(@valid_attributes.merge(:login => nil))
    user.errors.on(:login).should_not == nil
  end

  it 'should not match login' do
    user = User.create(@valid_attributes.merge(:login => "<script" ))
    user.errors.on(:login).should_not == nil
    user = User.create(@valid_attributes.merge(:login => "sc'ript" ))
    user.errors.on(:login).should_not == nil
    user = User.create(@valid_attributes.merge(:login => "scr&ipt" ))
    user.errors.on(:login).should_not == nil
    user = User.create(@valid_attributes.merge(:login => 'scr"ipt' ))
    user.errors.on(:login).should_not == nil
    user = User.create(@valid_attributes.merge(:login => "script>" ))
    user.errors.on(:login).should_not == nil
  end

  it 'should require email' do
    user = User.create(@valid_attributes.merge(:email => nil))
    user.errors.on(:email).should_not == nil
  end

  it 'should not match email' do
    user = User.create(@valid_attributes.merge(:email => "<script" ))
    user.errors.on(:email).should_not == nil
    user = User.create(@valid_attributes.merge(:email => "sc'ript" ))
    user.errors.on(:email).should_not == nil
    user = User.create(@valid_attributes.merge(:email => "scr&ipt" ))
    user.errors.on(:email).should_not == nil
    user = User.create(@valid_attributes.merge(:email => 'scr"ipt' ))
    user.errors.on(:email).should_not == nil
    user = User.create(@valid_attributes.merge(:email => "script>" ))
    user.errors.on(:email).should_not == nil
  end

  it 'should require name' do
    user = User.create(@valid_attributes.merge(:name => nil))
    user.errors.on(:name).should_not == nil
  end

  it 'should not match name' do
    user = User.create(@valid_attributes.merge(:name => "<script" ))
    user.errors.on(:name).should_not == nil
    user = User.create(@valid_attributes.merge(:name => "sc'ript" ))
    user.errors.on(:name).should == nil
    user = User.create(@valid_attributes.merge(:name => "scr&ipt" ))
    user.errors.on(:name).should == nil
    user = User.create(@valid_attributes.merge(:name => 'scr"ipt' ))
    user.errors.on(:name).should_not == nil
    user = User.create(@valid_attributes.merge(:name => "script>" ))
    user.errors.on(:name).should_not == nil
  end

end
