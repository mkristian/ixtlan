
require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'


require 'ixtlan' / 'models' / 'authentication'

describe Ixtlan::Models::Authentication do

  before :each do
    #Ixtlan::Models::Group.all.destroy!
    #Ixtlan::Models::User.all.destroy!
    user  = Ixtlan::Models::User.new(:login => "marvin2", :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "xx", :id => 1356, :created_at => DateTime.now, :updated_at => DateTime.now)
    user.created_by_id = 1356
    user.updated_by_id = 1356
    user.save!
    group = Ixtlan::Models::Group.create(:id => 1356, :name => 'marvin2_root', :current_user => user)
    user.groups << group
    group.save
    group.locales << Ixtlan::Models::Locale.default
    group.locales << Ixtlan::Models::Locale.first_or_create(:code => "en")
    @authentication = Ixtlan::Models::Authentication.create(:login => user.login, :user => user)
  end

  it "should" do
    xml = @authentication.to_xml
    xml.gsub!(/[0-9-]{10}T[0-9+-:]{14}/, "").gsub!(/ type='[^']+'/, '').gsub!(/<created_at><\/created_at>/, "<created_at/>").gsub!(/<locale><id>[0-9]*<\/id>/, "<locale>").should == "<authentication><id>1</id><login>marvin2</login><user><id>1356</id><login>marvin2</login><name>marvin the robot</name><email>marvin@universe.example.com</email><language>xx</language><created_at/><updated_at></updated_at><created_by_id>1356</created_by_id><updated_by_id>1356</updated_by_id><groups><group><id>1356</id><name>marvin2_root</name><created_at/><created_by_id>1356</created_by_id><updated_by_id>1356</updated_by_id><locales><locale><code>DEFAULT</code><created_at/></locale><locale><code>en</code><created_at/></locale></locales></group></groups></user></authentication>"
  end

end
