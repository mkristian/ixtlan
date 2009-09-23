module Ixtlan
  class Permission
    include DataMapper::Resource

    property :resource, String,:format => /^[a-zA-Z0-9_.]*$/, :key => true

    property :action, String, :format => /^[a-zA-Z0-9_.]*$/, :key => true

    has n, :roles, :model => "Ixtlan::Role"

    alias :to_x :to_xml_document
    def to_xml_document(opts, doc = nil)
      opts.merge!({:element_name => 'permission', :roles => {:collection_element_name => 'roles'}, :methods => [:roles]})
      to_x(opts, doc)
    end
  end
end
