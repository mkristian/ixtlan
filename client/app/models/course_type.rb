class CourseType
  include DataMapper::Resource

  property :id, Serial

  property :course_description, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :old_or_special, Boolean, :nullable => false 
  property :at_course_type, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :default_accept_file, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :increment_courses, Boolean, :nullable => false 
  property :course_stat_level, Integer, :nullable => false 
  property :confirm_required_flag, Boolean, :nullable => false 
  property :confirm_days_out_start, Integer, :nullable => false 
  property :confirm_days_out_end, Integer, :nullable => false 
  property :confirmation_reply, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :reminder_flag, Boolean, :nullable => false 
  property :reminder_days_out, Integer, :nullable => false 
  property :bump_flag, Boolean, :nullable => false 
  property :bump_days_out, Integer, :nullable => false 
  property :bump_notification, Boolean, :nullable => false 

  timestamps :at


   def to_s
     "CourseType(#{attribute_get(:id)})"
   end
end
