module Ixtlan
  class Group
    include DataMapper::Resource
    
    def self.default_storage_name
      "Group"
    end

    property :id, Serial, :field => "gidnumber"
    
    property :name, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 32, :field => "cn", :unique_index => true
    
    timestamps :created_at
    
    # modified_by Ixtlan::User

    has n, :locales, :model => "Ixtlan::Locale"
    
    alias :to_x :to_xml_document
    def to_xml_document(opts, doc = nil)
      opts.merge!({:element_name => 'group', :methods => [:locales], :locales => {:collection_element_name => 'locales'}})
      to_x(opts, doc)
    end
  end
end
