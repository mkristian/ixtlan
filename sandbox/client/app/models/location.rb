class Location
  include DataMapper::Resource

  property :id, Serial

  property :name, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :signature, Text, :nullable => false , :format => /^[^<'&">]*$/
  property :next_add_student_id, Integer, :nullable => false 
  property :default_state, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :default_country, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :default_native_language, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :default_news_language, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :default_app_origin, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :accept_confirm_weeks, Integer, :nullable => false 
  property :course_loc_id, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :rides_rpt_name, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :ignore_accents, Boolean, :nullable => false 
  property :registrar_email_address, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :from_name, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :email_signature, Text, :nullable => false , :format => /^[^<'&">]*$/
  property :bounce_to_email_address, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 255
  property :enable_confirm_module, Boolean, :nullable => false 

  timestamps :at


   def to_s
     "Location(#{attribute_get(:id)})"
   end
end
