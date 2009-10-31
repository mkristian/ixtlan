require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe User do
  before(:each) do
    user = Ixtlan::User.first
    unless user
      user = Ixtlan::User.new(:login => 'root', :email => 'root@exmple.com', :name => 'Superuser', :language => 'en', :id => 1, :created_at => DateTime.now, :updated_at => DateTime.now)
      user.created_by_id = 1
      user.updated_by_id = 1
      user.save!
    end
    @valid_attributes = {
      :current_user => user,
      :login => "valueForLogin",
      :name => "value for name",
      :email => "value@for.email",
      :language => "vl"
    }
  end

  it "should create a new instance given valid attributes" do
    user = User.create(@valid_attributes)
    user.valid?.should be_true
  end

  it "should require login" do
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

  it "should require name" do
    user = User.create(@valid_attributes.merge(:name => nil))
    user.errors.on(:name).should_not == nil
  end

  it 'should not match name' do
    user = User.create(@valid_attributes.merge(:name => "<script" ))
    user.errors.on(:name).should_not == nil

    user.errors.on(:name).should_not == nil

    user.errors.on(:name).should_not == nil
    user = User.create(@valid_attributes.merge(:name => 'scr"ipt' ))
    user.errors.on(:name).should_not == nil
    user = User.create(@valid_attributes.merge(:name => "script>" ))
    user.errors.on(:name).should_not == nil
  end

  it "should require email" do
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

  it "should require language" do
    user = User.create(@valid_attributes.merge(:language => nil))
    user.errors.on(:language).should_not == nil
  end

  it 'should not match language' do
    user = User.create(@valid_attributes.merge(:language => "<script" ))
    user.errors.on(:language).should_not == nil
    user = User.create(@valid_attributes.merge(:language => "sc'ript" ))
    user.errors.on(:language).should_not == nil
    user = User.create(@valid_attributes.merge(:language => "scr&ipt" ))
    user.errors.on(:language).should_not == nil
    user = User.create(@valid_attributes.merge(:language => 'scr"ipt' ))
    user.errors.on(:language).should_not == nil
    user = User.create(@valid_attributes.merge(:language => "script>" ))
    user.errors.on(:language).should_not == nil
  end

end
