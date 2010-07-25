require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'
require 'dm-migrations'

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
    @domains = Group.create(:name => 'domains', :current_user => @root)
    Locale.create(:code => Locale::DEFAULT, :current_user => User.first)
    Locale.create(:code => Locale::ALL, :current_user => User.first)
    @en = Locale.create(:code => 'en', :current_user => User.first)
    @fr = Locale.create(:code => 'fr', :current_user => User.first)
    Domain.create(:name => Domain::ALL, :current_user => User.first)
    @dvara = Domain.create(:name => 'dvara', :current_user => User.first)
    @dhara = Domain.create(:name => 'dhara', :current_user => User.first)

    @groups = {
      :group => [ { :id => @users.id.to_s,
                    :locales => {
                      :locale => [{ :id => @fr.id.to_s },
                                  { :id => @en.id.to_s } ]
                    },
                    :domains => {
                      :domain => [{ :id => @dvara.id.to_s },
                                  { :id => @dhara.id.to_s } ]
                    }
                  },
                  { :id => @locales.id.to_s,
                    :locales => {
                      :locale => { :id => @en.id.to_s }
                    }
                  },
                  { :id => @domains.id.to_s,
                    :domains => {
                      :domain => { :id => @dvara.id.to_s }
                    }
                  } ]
    }
  end

  describe 'root' do

    it 'should be root and should not be locales/domains_admin' do
      @root.root?.should be_true
      @root.locales_admin?.should be_false
      @root.domains_admin?.should be_false
    end

    it 'should be able to create an user' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @root)

      groups = @groups.dup
      groups[:group] << { :id => @root_group.id.to_s }
      u.update_all_children(groups)
      u.save

      u.groups.should == [@users, @locales, @domains, @root_group]
      u.groups[0].locales.should == [@fr, @en]
      u.groups[1].locales.should == [@en]
      u.groups[2].locales.should == []
      u.groups[3].locales.should == []

      u.groups[0].domains.should == [@dvara, @dhara]
      u.groups[1].domains.should == []
      u.groups[2].domains.should == [@dvara]
      u.groups[3].domains.should == []
    end

    it 'should be able to create an user without groups' do
      u = User.new(:login => 'user_no_groups', :email => 'user_no_groups@example.com', :name => 'user', :current_user => @root)
      u.update_all_children(nil)
      u.save

      u.groups.should == []
    end
  end

  describe 'user_admin and locales/domains_admin' do
    before(:each) do
      @admin = User.create(:login => 'admin_user', :email => 'admin_user@example.com', :name => 'admin', :current_user => @root)
      @admin.groups << @users
      @admin.groups << @locales
      @admin.groups << @domains
      @admin.save
    end

    it 'should not be root but should be locales/domains_admin' do
      @admin.root?.should be_false
      @admin.locales_admin?.should be_true
      @admin.domains_admin?.should be_true
    end

    it 'should be able to create an user' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @admin)
      u.update_all_children(@groups)
      u.save

      u.groups.should == [@users, @locales, @domains]
      u.groups[0].locales.should == [@fr, @en]
      u.groups[1].locales.should == [@en]
      u.groups[2].locales.should == []

      u.groups[0].domains.should == [@dvara, @dhara]
      u.groups[1].domains.should == []
      u.groups[2].domains.should == [@dvara]
    end

    it 'should be able to create an user but ignore disallowed group root' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @admin)

      groups = @groups.dup
      groups[:group] << { :id => @root_group.id.to_s }
      u.update_all_children(groups)
      u.save

      u.groups.should == [@users, @locales, @domains]
      u.groups[0].locales.should == [@fr, @en]
      u.groups[1].locales.should == [@en]
      u.groups[2].locales.should == []

      u.groups[0].domains.should == [@dvara, @dhara]
      u.groups[1].domains.should == []
      u.groups[2].domains.should == [@dvara]
    end
  end

  describe 'user_admin without locales/domains' do
    before(:all) do
      @admin = User.create(:login => 'admin', :email => 'admin@example.com', :name => 'admin', :current_user => @root)
      @admin.groups << @users
      @admin.save
    end

    it 'should not be root and not be locales/domains_admin' do
      @admin.root?.should be_false
      @admin.locales_admin?.should be_false
      @admin.domains_admin?.should be_false
    end

    it 'should be able to create an user' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @admin)
      u.update_all_children({ :group => { :id => @users.id.to_s } })
      u.save

      u.groups.should == [@users]
      u.groups[0].locales.should == []
    end

    it 'should be able to create an user without disallowed groups and locales' do
      u = User.new(:login => 'user_admin', :email => 'user_admin@example.com', :name => 'user', :current_user => @admin)
      u.update_all_children(@groups)
      u.save

      u.groups.should == [@users]
      u.groups[0].locales.should == []
      u.groups[0].domains.should == []
    end
  end

  describe 'user_admin with locales/domains' do
    before(:each) do
      @admin = User.create(:login => 'admin', :email => 'admin@example.com', :name => 'admin', :current_user => @root)
      @admin.groups << @users
      @admin.groups[0].locales << @en
      @admin.groups[0].domains << @dhara
      @admin.save
    end

    it 'should be able to create an user without disallowed groups and locales/domains but with allowed locales/domains' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @admin)
      u.update_all_children(@groups)
      u.save

      u.groups.should == [@users]
      u.groups[0].locales.should == [@en]
      u.groups[0].domains.should == [@dhara]
    end
  end

  describe 'user_admin with locales and as domains_admin' do
    before(:each) do
      @admin = User.create(:login => 'admin', :email => 'admin@example.com', :name => 'admin', :current_user => @root)
      @admin.groups << @users
      @admin.groups[0].locales << @en
      @admin.groups[0].domains << @dhara
      @admin.groups << @domains
      @admin.save
    end

    it 'should be able to create an user without disallowed groups and domains but with allowed domains' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @admin)
      u.update_all_children(@groups)
      u.save

      u.groups.should == [@users, @domains]
      u.groups[0].locales.should == [@en]
      u.groups[1].locales.should == []
      u.groups[0].domains.should == [@dvara, @dhara]
      u.groups[1].domains.should == [@dvara]
    end
  end

  describe 'user_admin with domains and as locales_admin' do
    before(:each) do
      @admin = User.create(:login => 'admin', :email => 'admin@example.com', :name => 'admin', :current_user => @root)
      @admin.groups << @users
      @admin.groups[0].locales << @en
      @admin.groups[0].domains << @dhara
      @admin.groups << @locales
      @admin.save
    end

    it 'should be able to create an user without disallowed groups and locales but with allowed locales' do
      u = User.new(:login => 'user', :email => 'user@example.com', :name => 'user', :current_user => @admin)
      u.update_all_children(@groups)
      u.save

      u.groups.should == [@users, @locales]
      u.groups[0].locales.should == [@fr, @en]
      u.groups[1].locales.should == [@en]
      u.groups[0].domains.should == [@dhara]
      u.groups[1].domains.should == []
    end
  end
end
