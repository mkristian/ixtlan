require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe CourseType do
  before(:each) do
    @valid_attributes = {
      :course_description => "value for course_description",
      :old_or_special => false,
      :at_course_type => "value for at_course_type",
      :default_accept_file => "value for default_accept_file",
      :increment_courses => false,
      :course_stat_level => 1,
      :confirm_required_flag => false,
      :confirm_days_out_start => 1,
      :confirm_days_out_end => 1,
      :confirmation_reply => "value for confirmation_reply",
      :reminder_flag => false,
      :reminder_days_out => 1,
      :bump_flag => false,
      :bump_days_out => 1,
      :bump_notification => false
    }
  end

  it "should require course_description" do
    course_type = CourseType.create(@valid_attributes.merge(:course_description => nil))
    course_type.errors.on(:course_description).should_not == nil
  end

  it 'should not match course_description' do
    course_type = CourseType.create(@valid_attributes.merge(:course_description => "<script" ))
    course_type.errors.on(:course_description).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:course_description => "sc'ript" ))
    course_type.errors.on(:course_description).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:course_description => "scr&ipt" ))
    course_type.errors.on(:course_description).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:course_description => 'scr"ipt' ))
    course_type.errors.on(:course_description).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:course_description => "script>" ))
    course_type.errors.on(:course_description).should_not == nil
  end

  it "should require old_or_special" do
    course_type = CourseType.create(@valid_attributes.merge(:old_or_special => nil))
    course_type.errors.on(:old_or_special).should_not == nil
  end

  it "should require at_course_type" do
    course_type = CourseType.create(@valid_attributes.merge(:at_course_type => nil))
    course_type.errors.on(:at_course_type).should_not == nil
  end

  it 'should not match at_course_type' do
    course_type = CourseType.create(@valid_attributes.merge(:at_course_type => "<script" ))
    course_type.errors.on(:at_course_type).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:at_course_type => "sc'ript" ))
    course_type.errors.on(:at_course_type).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:at_course_type => "scr&ipt" ))
    course_type.errors.on(:at_course_type).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:at_course_type => 'scr"ipt' ))
    course_type.errors.on(:at_course_type).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:at_course_type => "script>" ))
    course_type.errors.on(:at_course_type).should_not == nil
  end

  it "should require default_accept_file" do
    course_type = CourseType.create(@valid_attributes.merge(:default_accept_file => nil))
    course_type.errors.on(:default_accept_file).should_not == nil
  end

  it 'should not match default_accept_file' do
    course_type = CourseType.create(@valid_attributes.merge(:default_accept_file => "<script" ))
    course_type.errors.on(:default_accept_file).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:default_accept_file => "sc'ript" ))
    course_type.errors.on(:default_accept_file).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:default_accept_file => "scr&ipt" ))
    course_type.errors.on(:default_accept_file).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:default_accept_file => 'scr"ipt' ))
    course_type.errors.on(:default_accept_file).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:default_accept_file => "script>" ))
    course_type.errors.on(:default_accept_file).should_not == nil
  end

  it "should require increment_courses" do
    course_type = CourseType.create(@valid_attributes.merge(:increment_courses => nil))
    course_type.errors.on(:increment_courses).should_not == nil
  end

  it "should require course_stat_level" do
    course_type = CourseType.create(@valid_attributes.merge(:course_stat_level => nil))
    course_type.errors.on(:course_stat_level).should_not == nil
  end


  it "should be numerical course_stat_level" do
    course_type = CourseType.create(@valid_attributes.merge(:course_stat_level => "none-numberic" ))
    course_type.course_stat_level.to_i.should == 0
    course_type.errors.size.should == 1
  end

  it "should require confirm_required_flag" do
    course_type = CourseType.create(@valid_attributes.merge(:confirm_required_flag => nil))
    course_type.errors.on(:confirm_required_flag).should_not == nil
  end

  it "should require confirm_days_out_start" do
    course_type = CourseType.create(@valid_attributes.merge(:confirm_days_out_start => nil))
    course_type.errors.on(:confirm_days_out_start).should_not == nil
  end


  it "should be numerical confirm_days_out_start" do
    course_type = CourseType.create(@valid_attributes.merge(:confirm_days_out_start => "none-numberic" ))
    course_type.confirm_days_out_start.to_i.should == 0
    course_type.errors.size.should == 1
  end

  it "should require confirm_days_out_end" do
    course_type = CourseType.create(@valid_attributes.merge(:confirm_days_out_end => nil))
    course_type.errors.on(:confirm_days_out_end).should_not == nil
  end


  it "should be numerical confirm_days_out_end" do
    course_type = CourseType.create(@valid_attributes.merge(:confirm_days_out_end => "none-numberic" ))
    course_type.confirm_days_out_end.to_i.should == 0
    course_type.errors.size.should == 1
  end

  it "should require confirmation_reply" do
    course_type = CourseType.create(@valid_attributes.merge(:confirmation_reply => nil))
    course_type.errors.on(:confirmation_reply).should_not == nil
  end

  it 'should not match confirmation_reply' do
    course_type = CourseType.create(@valid_attributes.merge(:confirmation_reply => "<script" ))
    course_type.errors.on(:confirmation_reply).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:confirmation_reply => "sc'ript" ))
    course_type.errors.on(:confirmation_reply).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:confirmation_reply => "scr&ipt" ))
    course_type.errors.on(:confirmation_reply).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:confirmation_reply => 'scr"ipt' ))
    course_type.errors.on(:confirmation_reply).should_not == nil
    course_type = CourseType.create(@valid_attributes.merge(:confirmation_reply => "script>" ))
    course_type.errors.on(:confirmation_reply).should_not == nil
  end

  it "should require reminder_flag" do
    course_type = CourseType.create(@valid_attributes.merge(:reminder_flag => nil))
    course_type.errors.on(:reminder_flag).should_not == nil
  end

  it "should require reminder_days_out" do
    course_type = CourseType.create(@valid_attributes.merge(:reminder_days_out => nil))
    course_type.errors.on(:reminder_days_out).should_not == nil
  end


  it "should be numerical reminder_days_out" do
    course_type = CourseType.create(@valid_attributes.merge(:reminder_days_out => "none-numberic" ))
    course_type.reminder_days_out.to_i.should == 0
    course_type.errors.size.should == 1
  end

  it "should require bump_flag" do
    course_type = CourseType.create(@valid_attributes.merge(:bump_flag => nil))
    course_type.errors.on(:bump_flag).should_not == nil
  end

  it "should require bump_days_out" do
    course_type = CourseType.create(@valid_attributes.merge(:bump_days_out => nil))
    course_type.errors.on(:bump_days_out).should_not == nil
  end


  it "should be numerical bump_days_out" do
    course_type = CourseType.create(@valid_attributes.merge(:bump_days_out => "none-numberic" ))
    course_type.bump_days_out.to_i.should == 0
    course_type.errors.size.should == 1
  end

  it "should require bump_notification" do
    course_type = CourseType.create(@valid_attributes.merge(:bump_notification => nil))
    course_type.errors.on(:bump_notification).should_not == nil
  end

end
