class CourseTypeLocation
  include DataMapper::Resource

  property :location_id, Integer, :nullable => false, :key => true
  property :course_type_id, Integer, :nullable => false, :key => true
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

  belongs_to :course_type
  
   def to_s
     "CourseTypeLocation(#{attribute_get(:course_type_id)}, #{attribute_get(:location_id)})"
   end
end
