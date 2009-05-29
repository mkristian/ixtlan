require File.expand_path(File.dirname(__FILE__) + '/../spec_helper')

describe CourseTypeLocation do
  before(:each) do
    @valid_attributes = {
      :location_id => 1,
      :course_type_id => 1,
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

  it "should require location_id" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:location_id => nil))
    course_type_location.errors.on(:location_id).should_not == nil
  end


  it "should be numerical location_id" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:location_id => "none-numberic" ))
    course_type_location.location_id.to_i.should == 0
    course_type_location.errors.size.should == 1
  end

  it "should require course_type_id" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:course_type_id => nil))
    course_type_location.errors.on(:course_type_id).should_not == nil
  end


  it "should be numerical course_type_id" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:course_type_id => "none-numberic" ))
    course_type_location.course_type_id.to_i.should == 0
    course_type_location.errors.size.should == 1
  end

  it "should require confirm_required_flag" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirm_required_flag => nil))
    course_type_location.errors.on(:confirm_required_flag).should_not == nil
  end

  it "should require confirm_days_out_start" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirm_days_out_start => nil))
    course_type_location.errors.on(:confirm_days_out_start).should_not == nil
  end


  it "should be numerical confirm_days_out_start" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirm_days_out_start => "none-numberic" ))
    course_type_location.confirm_days_out_start.to_i.should == 0
    course_type_location.errors.size.should == 1
  end

  it "should require confirm_days_out_end" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirm_days_out_end => nil))
    course_type_location.errors.on(:confirm_days_out_end).should_not == nil
  end


  it "should be numerical confirm_days_out_end" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirm_days_out_end => "none-numberic" ))
    course_type_location.confirm_days_out_end.to_i.should == 0
    course_type_location.errors.size.should == 1
  end

  it "should require confirmation_reply" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirmation_reply => nil))
    course_type_location.errors.on(:confirmation_reply).should_not == nil
  end

  it 'should not match confirmation_reply' do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirmation_reply => "<script" ))
    course_type_location.errors.on(:confirmation_reply).should_not == nil
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirmation_reply => "sc'ript" ))
    course_type_location.errors.on(:confirmation_reply).should_not == nil
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirmation_reply => "scr&ipt" ))
    course_type_location.errors.on(:confirmation_reply).should_not == nil
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirmation_reply => 'scr"ipt' ))
    course_type_location.errors.on(:confirmation_reply).should_not == nil
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:confirmation_reply => "script>" ))
    course_type_location.errors.on(:confirmation_reply).should_not == nil
  end

  it "should require reminder_flag" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:reminder_flag => nil))
    course_type_location.errors.on(:reminder_flag).should_not == nil
  end

  it "should require reminder_days_out" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:reminder_days_out => nil))
    course_type_location.errors.on(:reminder_days_out).should_not == nil
  end


  it "should be numerical reminder_days_out" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:reminder_days_out => "none-numberic" ))
    course_type_location.reminder_days_out.to_i.should == 0
    course_type_location.errors.size.should == 1
  end

  it "should require bump_flag" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:bump_flag => nil))
    course_type_location.errors.on(:bump_flag).should_not == nil
  end

  it "should require bump_days_out" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:bump_days_out => nil))
    course_type_location.errors.on(:bump_days_out).should_not == nil
  end


  it "should be numerical bump_days_out" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:bump_days_out => "none-numberic" ))
    course_type_location.bump_days_out.to_i.should == 0
    course_type_location.errors.size.should == 1
  end

  it "should require bump_notification" do
    course_type_location = CourseTypeLocation.create(@valid_attributes.merge(:bump_notification => nil))
    course_type_location.errors.on(:bump_notification).should_not == nil
  end

end
