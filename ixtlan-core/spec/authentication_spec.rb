
require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'


require 'ixtlan' / 'models' / 'authentication'

class Authentication
  include Ixtlan::Models::Authentication
end

describe Ixtlan::Models::Authentication do

  before :each do
    #Ixtlan::Models::Group.all.destroy!
    #Ixtlan::Models::User.all.destroy!
    user  = User.new(:login => "marvin2", :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "xx", :id => 1356, :created_at => DateTime.now, :updated_at => DateTime.now)
    user.created_by = user #_id = 1356
    user.updated_by = user #_id = 1356
    user.save!
    group = Group.create(:id => 1356, :name => 'marvin2_root', :current_user => user)
    user.groups << group
    group.save

    group.locales << Locale.default
    group.locales << (Locale.first(:code => "en") || Locale.create(:code => "en", :current_user => User.first))
    @authentication = Authentication.create(:login => user.login, :user => user)

    Ixtlan::Guard.load( Slf4r::LoggerFacade.new(:root), :root, (Pathname(__FILE__).dirname + 'guards').expand_path )
  end

  it "should" do
    xml = @authentication.to_xml
    xml.gsub!(/[0-9-]{10}T[0-9+-:]{14}/, "").gsub!(/<created_at><\/created_at>/, "<created_at/>").gsub!(/<locale><id>[0-9]*<\/id>/, "<locale>").gsub!(/<permission>.*<\/permission>/, '').should == "<authentication><login>marvin2</login><user><id>1356</id><login>marvin2</login><name>marvin the robot</name><email>marvin@universe.example.com</email><language>xx</language><created_at/><updated_at></updated_at><groups><group><id>1356</id><name>marvin2_root</name><locales><locale><code>DEFAULT</code><created_by_id>1</created_by_id></locale><locale><code>en</code><created_by_id>1</created_by_id></locale></locales></group></groups><created_by><id>1356</id><login>marvin2</login><name>marvin the robot</name><email>marvin@universe.example.com</email></created_by><updated_by><id>1356</id><login>marvin2</login><name>marvin the robot</name><email>marvin@universe.example.com</email></updated_by></user><permissions></permissions></authentication>"
  end

end
