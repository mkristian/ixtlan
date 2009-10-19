module Ixtlan
  class Locale
    include DataMapper::Resource


    def self.name
      "Locale"
    end

    def self.to_s
      "Locale"
    end

    def self.default_storage_name
      "Locale"
    end

    property :code, String, :nullable => false , :format => /^[a-z][a-z](_[A-Z][A-Z])?$|^DEFAULT$/, :length => 7, :key => true
    
    timestamps :created_at

    def hash
      attribute_get(:code).hash
    end
    
    alias :eql? :==
    def ==(other)
      attribute_get(:code).eql?(other.attribute_get(:code))
    end

    alias :to_x :to_xml_document
    def to_xml_document(opts, doc = nil)
      opts.merge!({:exclude => [:group_id]})
      to_x(opts, doc)
    end
  end
end
