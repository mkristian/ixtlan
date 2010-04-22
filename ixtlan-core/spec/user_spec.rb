require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'

User = Ixtlan::Models::User
Group = Ixtlan::Models::Group
Locale = Ixtlan::Models::Locale
describe Ixtlan::Models::User do

  before(:all) do

    DataMapper.setup(:default, "sqlite3::memory:")
    DataMapper.auto_migrate!

    Ixtlan::Models::GroupUser.all.destroy!
    Ixtlan::Models::GroupLocaleUser.all.destroy!
    Locale.all.destroy!
    Group.all.destroy!
    User.all.destroy!

    @root = User.new(:login => 'root', :email => 'root@example.com', :name => 'Superuser', :id => 1)
    @root.created_at = DateTime.now
    @root.updated_at = @root.created_at
    @root.created_by_id = 1
    @root.updated_by_id = 1
    @root.save!
    @root_group = Group.create(:name => 'root', :current_user => @root)
    @root.groups << @root_group
    @root.save
    @users = Group.create(:name => 'users', :current_user => @root)
    @locales = Group.create(:name => 'locales', :current_user => @root)
    Locale.create(:code => Locale::DEFAULT, :current_user => User.first)
    Locale.create(:code => Locale::ALL, :current_user => User.first)
    @en = Locale.create(:code => 'en', :current_user => User.first)
    @fr = Locale.create(:code => 'fr', :current_user => User.first)

    @groups = {
      :group => [ { :id => @users.id.to_s,
                    :locales => {
                      :locale => [{ :id => @fr.id.to_s },
                                  { :id => @en.id.to_s } ]
                    }
                  },
                  { :id => @locales.id.to_s,
                    :locales => {
                      :locale => { :id => @en.id.to_s }
                    }
                  } ]
    }
  end

  describe 'root' do

    it 'should be root and should not be locales_admin' do
      @root.root?.should be_true
      @root.locales_admin?.should be_false
    end

    it 'should be able to create an user' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @root)

      groups = @groups.dup
      groups[:group] << { :id => @root_group.id.to_s }
      u.update_all_children(groups, @root)
      u.save

      u.groups.should == [@users, @locales, @root_group]
      u.groups[0].locales.should == [@fr, @en]
      u.groups[1].locales.should == [@en]
      u.groups[2].locales.should == []
    end

    it 'should be able to create an user without groups' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @root)
      u.update_all_children(nil, @root)
      u.save

      u.groups.should == []
    end
  end

  describe 'user_admin and locales_admin' do
    before(:each) do
      @admin = User.create(:login => 'admin', :email => 'admin@example.com', :name => 'admin', :current_user => @root)
      @admin.groups << @users
      @admin.groups << @locales
      @admin.save
    end

    it 'should not be root but should be locales_admin' do
      @admin.root?.should be_false
      @admin.locales_admin?.should be_true
    end

    it 'should be able to create an user' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @root)
      u.update_all_children(@groups, @admin)
      u.save

      u.groups.should == [@users, @locales]
      u.groups[0].locales.should == [@fr, @en]
      u.groups[1].locales.should == [@en]
    end

    it 'should be able to create an user but ignore disallowed group root' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @root)

      groups = @groups.dup
      groups[:group] << { :id => @root_group.id.to_s }
      u.update_all_children(groups, @admin)
      u.save

      u.groups.should == [@users, @locales]
      u.groups[0].locales.should == [@fr, @en]
      u.groups[1].locales.should == [@en]
    end
  end

  describe 'user_admin without locales' do
    before(:each) do
      @admin = User.create(:login => 'admin', :email => 'admin@example.com', :name => 'admin', :current_user => @root)
      @admin.groups << @users
      @admin.save
    end

    it 'should not be root and not be locales_admin' do
      @admin.root?.should be_false
      @admin.locales_admin?.should be_false
    end

    it 'should be able to create an user' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @root)
      u.update_all_children({ :group => { :id => @users.id.to_s } }, @admin)
      u.save

      u.groups.should == [@users]
      u.groups[0].locales.should == []
    end

    it 'should be able to create an user without disallowed groups and locales' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @root)
      u.update_all_children(@groups, @admin)
      u.save

      u.groups.should == [@users]
      u.groups[0].locales.should == []
    end
  end

  describe 'user_admin with locales' do
    before(:each) do
      @admin = User.create(:login => 'admin', :email => 'admin@example.com', :name => 'admin', :current_user => @root)
      @admin.groups << @users
      @admin.groups[0].locales << @en
      @admin.save
    end

    it 'should be able to create an user without disallowed groups and locales but with allowed locales' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @root)
      u.update_all_children(@groups, @admin)
      u.save

      u.groups.should == [@users]
      u.groups[0].locales.should == [@en]
    end
  end
end
