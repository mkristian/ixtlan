module Ixtlan
  class Locale
    include DataMapper::Resource

    property :code, String, :nullable => false , :format => /^[a-z][a-z](_[A-Z][A-Z])?$|^DEFAULT$/, :length => 7, :key => true
    
    property :created_at, DateTime, :nullable => false, :auto_validation => false

    def hash
      attribute_get(:code).hash
    end
    
    alias :eql? :==
    def ==(other)
      attribute_get(:code).eql?(other.attribute_get(:code))
    end

    alias :to_x :to_xml_document
    def to_xml_document(opts, doc = nil)
      opts.merge!({:element_name => 'locale', :exclude => [:group_id]})
      to_x(opts, doc)
    end
  end
end