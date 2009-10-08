require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'
require 'dm-core'
require 'ixtlan' / 'modified_by'

class User
  include DataMapper::Resource

  property :login, String, :key => true
end

class AuditedName
  include DataMapper::Resource

  property :name, String, :length => 2..255, :key => true

  modified_by User
end

describe Ixtlan::ModifiedBy do

  before :each do
    @user = User.create(:login => 'spock')
    @second = User.create(:login => 'dr pill')
    @name = AuditedName.create(:name => 'kirk', :current_user => @user)
  end
  
  it 'should create resource with modified by properties' do
    @name.new?.should be_false
    @name.instance_variable_get(:@current_user).should be_nil
    @name.created_by.should == @user
    @name.updated_by.should == @user
  end

  it 'should new/save resource with modified by properties' do
    name = AuditedName.new(:name => 'seven of nine')
    name.current_user = @user
    name.save.should be_true
    name.created_by.should == @user
    name.updated_by.should == @user
  end
  
  it 'should modify updated_by on change' do
    @name.current_user = @second
    @name.name = "scotty"
    @name.save.should be_true
    @name.created_by.should == @user
    @name.updated_by.should == @second
  end

  it 'should modify updated_by on update' do
    @name.update(:name => "captain kirk", :current_user => @second)
    @name.instance_variable_get(:@current_user).should be_nil
    @name.updated_by.should == @second
  end

  it 'should not modify updated_by on update without change' do
    @name.update(:name => "kirk", :current_user => @second)
    @name.updated_by.should == @user
    @name.instance_variable_get(:@current_user).should be_nil
  end

  it 'should modify updated_by on update with current_user' do
    @name.current_user = @second
    @name.update(:name => "captain kirk")
    @name.updated_by.should == @second
  end

  it 'should fail create without current_user' do
    lambda { AuditedName.create(:name => "ohura") }.should raise_error(DataMapper::MissingCurrentUserError)
  end

  it 'should fail save without current_user' do
    @name.name = "ohura"
    lambda { @name.save }.should raise_error(DataMapper::MissingCurrentUserError)
  end

  it 'should fail update without current_user' do
    lambda { @name.update(:name => "ohura") }.should raise_error(DataMapper::MissingCurrentUserError)
  end
end