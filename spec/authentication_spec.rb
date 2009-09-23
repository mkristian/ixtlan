
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
    group = Ixtlan::Group.create :name => 'root'
    user  = Ixtlan::User.create(:login => :marvin, :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "all" )
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
