class Role
  include DataMapper::Resource

  def self.sname
    "group"
  end

  property :id, Serial, :field => "gidnumber"

  property :name, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 32, :field => "cn", :unique_index => true
  
 #  alias :to_x :to_xml_document
#   def to_xml_document(opts = {}, doc = nil)
#     to_x({:exclude => [:id]}, doc)
#   end

end
