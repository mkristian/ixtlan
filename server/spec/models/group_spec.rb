require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe Group do
  before(:each) do
    @valid_attributes = {
      :name => "value for name",
      :description => "value for description"
    }
  end

  it "should create a new instance given valid attributes" do
    group = Group.create(@valid_attributes)
    group.valid?.should be_true
    group.new_record?.should be_false
  end

  it 'should require name' do
    group = Group.create(@valid_attributes.merge(:name => nil))
    group.errors.on(:name).should_not == nil
  end

  it 'should not match name' do
    group = Group.create(@valid_attributes.merge(:name => "<script" ))
    group.errors.on(:name).should_not == nil
    group = Group.create(@valid_attributes.merge(:name => "sc'ript" ))
    group.errors.on(:name).should_not == nil
    group = Group.create(@valid_attributes.merge(:name => "scr&ipt" ))
    group.errors.on(:name).should_not == nil
    group = Group.create(@valid_attributes.merge(:name => 'scr"ipt' ))
    group.errors.on(:name).should_not == nil
    group = Group.create(@valid_attributes.merge(:name => "script>" ))
    group.errors.on(:name).should_not == nil
  end

  it 'should require description' do
    group = Group.create(@valid_attributes.merge(:description => nil))
    group.errors.on(:description).should_not == nil
  end

  it 'should not match description' do
    group = Group.create(@valid_attributes.merge(:description => "<script" ))
    group.errors.on(:description).should_not == nil
    group = Group.create(@valid_attributes.merge(:description => "sc'ript" ))
    group.errors.on(:description).should_not == nil
    group = Group.create(@valid_attributes.merge(:description => "scr&ipt" ))
    group.errors.on(:description).should_not == nil
    group = Group.create(@valid_attributes.merge(:description => 'scr"ipt' ))
    group.errors.on(:description).should_not == nil
    group = Group.create(@valid_attributes.merge(:description => "script>" ))
    group.errors.on(:description).should_not == nil
  end

end
