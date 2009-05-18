require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe Location do
  before(:each) do
    @valid_attributes = {
      :name => "value for name",
      :signature => "value for signature",
      :next_add_student_id => 1,
      :default_state => "value for default_state",
      :default_country => "value for default_country",
      :default_native_language => "value for default_native_language",
      :default_news_language => "value for default_news_language",
      :default_app_origin => "value for default_app_origin",
      :accept_confirm_weeks => 1,
      :course_loc_id => "value for course_loc_id",
      :rides_rpt_name => "value for rides_rpt_name",
      :ignore_accents => false,
      :registrar_email_address => "value for registrar_email_address",
      :from_name => "value for from_name",
      :email_signature => "value for email_signature",
      :bounce_to_email_address => "value for bounce_to_email_address",
      :enable_confirm_module => false
    }
  end

  it "should require name" do
    location = Location.create(@valid_attributes.merge(:name => nil))
    location.errors.on(:name).should_not == nil
  end

  it 'should not match name' do
    location = Location.create(@valid_attributes.merge(:name => "<script" ))
    location.errors.on(:name).should_not == nil
    location = Location.create(@valid_attributes.merge(:name => "sc'ript" ))
    location.errors.on(:name).should_not == nil
    location = Location.create(@valid_attributes.merge(:name => "scr&ipt" ))
    location.errors.on(:name).should_not == nil
    location = Location.create(@valid_attributes.merge(:name => 'scr"ipt' ))
    location.errors.on(:name).should_not == nil
    location = Location.create(@valid_attributes.merge(:name => "script>" ))
    location.errors.on(:name).should_not == nil
  end

  it "should require signature" do
    location = Location.create(@valid_attributes.merge(:signature => nil))
    location.errors.on(:signature).should_not == nil
  end

  it 'should not match signature' do
    location = Location.create(@valid_attributes.merge(:signature => "<script" ))
    location.errors.on(:signature).should_not == nil
    location = Location.create(@valid_attributes.merge(:signature => "sc'ript" ))
    location.errors.on(:signature).should_not == nil
    location = Location.create(@valid_attributes.merge(:signature => "scr&ipt" ))
    location.errors.on(:signature).should_not == nil
    location = Location.create(@valid_attributes.merge(:signature => 'scr"ipt' ))
    location.errors.on(:signature).should_not == nil
    location = Location.create(@valid_attributes.merge(:signature => "script>" ))
    location.errors.on(:signature).should_not == nil
  end

  it "should require next_add_student_id" do
    location = Location.create(@valid_attributes.merge(:next_add_student_id => nil))
    location.errors.on(:next_add_student_id).should_not == nil
  end


  it "should be numerical next_add_student_id" do
    location = Location.create(@valid_attributes.merge(:next_add_student_id => "none-numberic" ))
    location.next_add_student_id.to_i.should == 0
    location.errors.size.should == 1
  end

  it "should require default_state" do
    location = Location.create(@valid_attributes.merge(:default_state => nil))
    location.errors.on(:default_state).should_not == nil
  end

  it 'should not match default_state' do
    location = Location.create(@valid_attributes.merge(:default_state => "<script" ))
    location.errors.on(:default_state).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_state => "sc'ript" ))
    location.errors.on(:default_state).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_state => "scr&ipt" ))
    location.errors.on(:default_state).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_state => 'scr"ipt' ))
    location.errors.on(:default_state).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_state => "script>" ))
    location.errors.on(:default_state).should_not == nil
  end

  it "should require default_country" do
    location = Location.create(@valid_attributes.merge(:default_country => nil))
    location.errors.on(:default_country).should_not == nil
  end

  it 'should not match default_country' do
    location = Location.create(@valid_attributes.merge(:default_country => "<script" ))
    location.errors.on(:default_country).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_country => "sc'ript" ))
    location.errors.on(:default_country).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_country => "scr&ipt" ))
    location.errors.on(:default_country).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_country => 'scr"ipt' ))
    location.errors.on(:default_country).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_country => "script>" ))
    location.errors.on(:default_country).should_not == nil
  end

  it "should require default_native_language" do
    location = Location.create(@valid_attributes.merge(:default_native_language => nil))
    location.errors.on(:default_native_language).should_not == nil
  end

  it 'should not match default_native_language' do
    location = Location.create(@valid_attributes.merge(:default_native_language => "<script" ))
    location.errors.on(:default_native_language).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_native_language => "sc'ript" ))
    location.errors.on(:default_native_language).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_native_language => "scr&ipt" ))
    location.errors.on(:default_native_language).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_native_language => 'scr"ipt' ))
    location.errors.on(:default_native_language).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_native_language => "script>" ))
    location.errors.on(:default_native_language).should_not == nil
  end

  it "should require default_news_language" do
    location = Location.create(@valid_attributes.merge(:default_news_language => nil))
    location.errors.on(:default_news_language).should_not == nil
  end

  it 'should not match default_news_language' do
    location = Location.create(@valid_attributes.merge(:default_news_language => "<script" ))
    location.errors.on(:default_news_language).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_news_language => "sc'ript" ))
    location.errors.on(:default_news_language).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_news_language => "scr&ipt" ))
    location.errors.on(:default_news_language).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_news_language => 'scr"ipt' ))
    location.errors.on(:default_news_language).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_news_language => "script>" ))
    location.errors.on(:default_news_language).should_not == nil
  end

  it "should require default_app_origin" do
    location = Location.create(@valid_attributes.merge(:default_app_origin => nil))
    location.errors.on(:default_app_origin).should_not == nil
  end

  it 'should not match default_app_origin' do
    location = Location.create(@valid_attributes.merge(:default_app_origin => "<script" ))
    location.errors.on(:default_app_origin).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_app_origin => "sc'ript" ))
    location.errors.on(:default_app_origin).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_app_origin => "scr&ipt" ))
    location.errors.on(:default_app_origin).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_app_origin => 'scr"ipt' ))
    location.errors.on(:default_app_origin).should_not == nil
    location = Location.create(@valid_attributes.merge(:default_app_origin => "script>" ))
    location.errors.on(:default_app_origin).should_not == nil
  end

  it "should require accept_confirm_weeks" do
    location = Location.create(@valid_attributes.merge(:accept_confirm_weeks => nil))
    location.errors.on(:accept_confirm_weeks).should_not == nil
  end


  it "should be numerical accept_confirm_weeks" do
    location = Location.create(@valid_attributes.merge(:accept_confirm_weeks => "none-numberic" ))
    location.accept_confirm_weeks.to_i.should == 0
    location.errors.size.should == 1
  end

  it "should require course_loc_id" do
    location = Location.create(@valid_attributes.merge(:course_loc_id => nil))
    location.errors.on(:course_loc_id).should_not == nil
  end

  it 'should not match course_loc_id' do
    location = Location.create(@valid_attributes.merge(:course_loc_id => "<script" ))
    location.errors.on(:course_loc_id).should_not == nil
    location = Location.create(@valid_attributes.merge(:course_loc_id => "sc'ript" ))
    location.errors.on(:course_loc_id).should_not == nil
    location = Location.create(@valid_attributes.merge(:course_loc_id => "scr&ipt" ))
    location.errors.on(:course_loc_id).should_not == nil
    location = Location.create(@valid_attributes.merge(:course_loc_id => 'scr"ipt' ))
    location.errors.on(:course_loc_id).should_not == nil
    location = Location.create(@valid_attributes.merge(:course_loc_id => "script>" ))
    location.errors.on(:course_loc_id).should_not == nil
  end

  it "should require rides_rpt_name" do
    location = Location.create(@valid_attributes.merge(:rides_rpt_name => nil))
    location.errors.on(:rides_rpt_name).should_not == nil
  end

  it 'should not match rides_rpt_name' do
    location = Location.create(@valid_attributes.merge(:rides_rpt_name => "<script" ))
    location.errors.on(:rides_rpt_name).should_not == nil
    location = Location.create(@valid_attributes.merge(:rides_rpt_name => "sc'ript" ))
    location.errors.on(:rides_rpt_name).should_not == nil
    location = Location.create(@valid_attributes.merge(:rides_rpt_name => "scr&ipt" ))
    location.errors.on(:rides_rpt_name).should_not == nil
    location = Location.create(@valid_attributes.merge(:rides_rpt_name => 'scr"ipt' ))
    location.errors.on(:rides_rpt_name).should_not == nil
    location = Location.create(@valid_attributes.merge(:rides_rpt_name => "script>" ))
    location.errors.on(:rides_rpt_name).should_not == nil
  end

  it "should require ignore_accents" do
    location = Location.create(@valid_attributes.merge(:ignore_accents => nil))
    location.errors.on(:ignore_accents).should_not == nil
  end

  it "should require registrar_email_address" do
    location = Location.create(@valid_attributes.merge(:registrar_email_address => nil))
    location.errors.on(:registrar_email_address).should_not == nil
  end

  it 'should not match registrar_email_address' do
    location = Location.create(@valid_attributes.merge(:registrar_email_address => "<script" ))
    location.errors.on(:registrar_email_address).should_not == nil
    location = Location.create(@valid_attributes.merge(:registrar_email_address => "sc'ript" ))
    location.errors.on(:registrar_email_address).should_not == nil
    location = Location.create(@valid_attributes.merge(:registrar_email_address => "scr&ipt" ))
    location.errors.on(:registrar_email_address).should_not == nil
    location = Location.create(@valid_attributes.merge(:registrar_email_address => 'scr"ipt' ))
    location.errors.on(:registrar_email_address).should_not == nil
    location = Location.create(@valid_attributes.merge(:registrar_email_address => "script>" ))
    location.errors.on(:registrar_email_address).should_not == nil
  end

  it "should require from_name" do
    location = Location.create(@valid_attributes.merge(:from_name => nil))
    location.errors.on(:from_name).should_not == nil
  end

  it 'should not match from_name' do
    location = Location.create(@valid_attributes.merge(:from_name => "<script" ))
    location.errors.on(:from_name).should_not == nil
    location = Location.create(@valid_attributes.merge(:from_name => "sc'ript" ))
    location.errors.on(:from_name).should_not == nil
    location = Location.create(@valid_attributes.merge(:from_name => "scr&ipt" ))
    location.errors.on(:from_name).should_not == nil
    location = Location.create(@valid_attributes.merge(:from_name => 'scr"ipt' ))
    location.errors.on(:from_name).should_not == nil
    location = Location.create(@valid_attributes.merge(:from_name => "script>" ))
    location.errors.on(:from_name).should_not == nil
  end

  it "should require email_signature" do
    location = Location.create(@valid_attributes.merge(:email_signature => nil))
    location.errors.on(:email_signature).should_not == nil
  end

  it 'should not match email_signature' do
    location = Location.create(@valid_attributes.merge(:email_signature => "<script" ))
    location.errors.on(:email_signature).should_not == nil
    location = Location.create(@valid_attributes.merge(:email_signature => "sc'ript" ))
    location.errors.on(:email_signature).should_not == nil
    location = Location.create(@valid_attributes.merge(:email_signature => "scr&ipt" ))
    location.errors.on(:email_signature).should_not == nil
    location = Location.create(@valid_attributes.merge(:email_signature => 'scr"ipt' ))
    location.errors.on(:email_signature).should_not == nil
    location = Location.create(@valid_attributes.merge(:email_signature => "script>" ))
    location.errors.on(:email_signature).should_not == nil
  end

  it "should require bounce_to_email_address" do
    location = Location.create(@valid_attributes.merge(:bounce_to_email_address => nil))
    location.errors.on(:bounce_to_email_address).should_not == nil
  end

  it 'should not match bounce_to_email_address' do
    location = Location.create(@valid_attributes.merge(:bounce_to_email_address => "<script" ))
    location.errors.on(:bounce_to_email_address).should_not == nil
    location = Location.create(@valid_attributes.merge(:bounce_to_email_address => "sc'ript" ))
    location.errors.on(:bounce_to_email_address).should_not == nil
    location = Location.create(@valid_attributes.merge(:bounce_to_email_address => "scr&ipt" ))
    location.errors.on(:bounce_to_email_address).should_not == nil
    location = Location.create(@valid_attributes.merge(:bounce_to_email_address => 'scr"ipt' ))
    location.errors.on(:bounce_to_email_address).should_not == nil
    location = Location.create(@valid_attributes.merge(:bounce_to_email_address => "script>" ))
    location.errors.on(:bounce_to_email_address).should_not == nil
  end

  it "should require enable_confirm_module" do
    location = Location.create(@valid_attributes.merge(:enable_confirm_module => nil))
    location.errors.on(:enable_confirm_module).should_not == nil
  end

end
