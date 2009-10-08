class CourseTypeLocation
  include DataMapper::Resource

  property :id, Serial

  property :location_id, Integer, :nullable => false 
  property :course_type_id, Integer, :nullable => false 
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
     "CourseTypeLocation(#{attribute_get(:id)})"
   end
end
