module Ixtlan
  class Group
    include DataMapper::Resource
    
    def storage_name
      "group"
    end

    property :id, Serial, :field => "gidnumber"
    
    property :name, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 32, :field => "cn", :unique_index => true
    
    property :created_at, DateTime, :nullable => false, :auto_validation => false
    
    has n, :locales
    
    alias :to_x :to_xml_document
    def to_xml_document(opts, doc = nil)
      opts.merge!({:element_name => 'group', :methods => [:locales], :locales => {:collection_element_name => 'locales'}})
      to_x(opts, doc)
    end
  end
end
