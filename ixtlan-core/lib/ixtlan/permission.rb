module Ixtlan
  class Permission

    include DataMapper::Resource

    def self.name
      "Permission"
    end

    def self.to_s
      "Permission"
    end

    def self.default_storage_name
      "Permission"
    end

    property :resource, String,:format => /^[a-zA-Z0-9_.]*$/, :key => true

    property :action, String, :format => /^[a-zA-Z0-9_.]*$/, :key => true

    has n, :roles, :model => "::Ixtlan::Role"

    alias :to_x :to_xml_document
    def to_xml_document(opts, doc = nil)
      opts.merge!({:methods => [:roles]})
      to_x(opts, doc)
    end
  end
end
