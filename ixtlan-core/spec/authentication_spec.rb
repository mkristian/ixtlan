
require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'


require 'dm-timestamps'
require 'ixtlan' / 'user'
require 'ixtlan' / 'group'
require 'ixtlan' / 'group_user'
require 'ixtlan' / 'locale'
require 'ixtlan' / 'authentication'

DataMapper.setup(:default, :adapter => :in_memory)

describe Ixtlan::Authentication do

  before :each do
    user  = Ixtlan::User.new(:login => :marvin, :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "xx", :id => 1, :created_at => DateTime.now, :updated_at => DateTime.now)
    user.created_by_id = 1
    user.updated_by_id = 1
    user.save!
    group = Ixtlan::Group.create :name => 'root', :current_user => user
    group.locales << Ixtlan::Locale.create(:code => "DEFAULT")
    group.locales << Ixtlan::Locale.create(:code => "en")
    group.save
    user.groups << group
    @authentication = Ixtlan::Authentication.create(:login => user.login)
    @authentication.user = user
  end

  it "should" do
    p @authentication.to_xml
  end

end
