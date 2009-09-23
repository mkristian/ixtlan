module Ixtlan
  class Role
    include DataMapper::Resource

    property :name, String, :nullable => false , :format => /^[a-zA-Z0-9\-_.]*$/, :length => 32, :key => true

    alias :to_x :to_xml_document
    def to_xml_document(opts, doc = nil)
      opts.merge!({:element_name => 'role', :exclude => [:permission_resource,:permission_action]})
      to_x(opts, doc)
    end
  end
end
