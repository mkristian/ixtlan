
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
    Ixtlan::Group.all.destroy!
    user  = Ixtlan::User.new(:login => :marvin, :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "xx", :id => 1, :created_at => DateTime.now, :updated_at => DateTime.now)
    user.created_by_id = 1
    user.updated_by_id = 1
    user.save!
    group = Ixtlan::Group.create :name => 'root', :current_user => user
    user.groups << group
    group.save
    group.locales << Ixtlan::Locale.create(:code => "DEFAULT")
    group.locales << Ixtlan::Locale.create(:code => "en")
    @authentication = Ixtlan::Authentication.create(:login => user.login)
    @authentication.user = user
  end

  it "should" do
    @authentication.to_xml.gsub!(/[0-9-]{10}T[0-9+-:]{14}/, "").gsub!(/ type='[^']+'/, '').should == "<authentication><login>marvin</login><user><id>1</id><login>marvin</login><name>marvin the robot</name><email>marvin@universe.example.com</email><language>xx</language><created_at></created_at><updated_at></updated_at><created_by_id>1</created_by_id><updated_by_id>1</updated_by_id><groups><group><id>1</id><name>root</name><created_at></created_at><created_by_id>1</created_by_id><updated_by_id>1</updated_by_id><locales><locale><code>DEFAULT</code><created_at></created_at></locale><locale><code>en</code><created_at></created_at></locale></locales></group></groups></user></authentication>"
  end

end
