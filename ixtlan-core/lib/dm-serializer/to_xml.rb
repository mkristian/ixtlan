require 'dm-serializer/common'
require 'dm-serializer/xml_serializers'
require 'rexml/document'

module DataMapper
  module Serialize
    # Serialize a Resource to XML
    #
    # @return <REXML::Document> an XML representation of this Resource
    def to_xml(opts = {})
      xml = XMLSerializers::SERIALIZER
      xml.output(to_xml_document(opts)).to_s
    end

    protected
    # This method requires certain methods to be implemented in the individual
    # serializer library subclasses:
    # new_document
    # root_node
    # add_property_node
    # add_node
    def to_xml_document(opts={}, doc = nil)
      xml = XMLSerializers::SERIALIZER
      doc ||= xml.new_document
      default_xml_element_name = lambda { Extlib::Inflection.underscore(model.storage_name.singular).tr("/", "-") }
      root = xml.root_node(doc, opts[:element_name] || default_xml_element_name[])
      properties_to_serialize(opts).each do |property|
        value = __send__(property.name)
        attrs = opts[:skip_types] ? {} : (property.class == DataMapper::Property::String) ? {} : {'type' => property.class.to_s.downcase}
        value = value.to_s(:xml) if property.class == DataMapper::Property::DateTime rescue value
        xml.add_node(root, property.name.to_s, value.frozen? ? value.to_s.dup: value, attrs) unless value.blank? && opts[:skip_empty_tags]
      end

      (opts[:methods] || []).each do |meth|
        if self.respond_to?(meth)
          xml_name = meth.to_s.gsub(/[^a-z0-9_]/, '')
          value = __send__(meth)
          unless value.nil?
            if value.respond_to?(:to_xml_document)
              options = value.is_a?(DataMapper::Collection) ? {:collection_element_name => xml_name} : {:element_name => xml_name}
              options[:skip_types] = opts[:skip_types]
              options[:skip_empty_tags] = opts[:skip_empty_tags]
              options.merge!(opts[meth] || {})
              xml.add_xml(root, value.__send__(:to_xml_document, options)) unless value.is_a?(DataMapper::Collection) && opts[:skip_empty_tags] == true && value.size == 0 
            else
              xml.add_node(root, xml_name, value.to_s)
            end
          end
        end
      end
      doc
    end
  end

  class Collection
    def to_xml(opts = {})
      to_xml_document(opts).to_s
    end

    protected

    def to_xml_document(opts = {})
      xml = DataMapper::Serialize::XMLSerializers::SERIALIZER
      doc = xml.new_document
      default_collection_element_name = lambda {Extlib::Inflection.underscore(self.model.storage_name).tr("/", "-")}
      root = xml.root_node(doc, opts[:collection_element_name] || default_collection_element_name[], opts[:skip_types] ? {} : {'type' => 'array'})
      items = []
      self.each do |item|
        items << item
      end
      items.each do |item|
        item.__send__(:to_xml_document, opts, doc)
      end
      doc
    end
  end

  if Serialize::Support.dm_validations_loaded?

    module Validate
      class ValidationErrors
        def to_xml(opts = {})
          to_xml_document(opts).to_s
        end

        protected

        def to_xml_document(opts = {})
          xml = DataMapper::Serialize::XMLSerializers::SERIALIZER
          doc = xml.new_document
          root = xml.root_node(doc, "errors", {'type' => 'hash'})

          errors.each do |key, value|
            property = xml.add_node(root, key.to_s, nil, {'type' => 'array'})
            property.attributes["type"] = 'array'
            value.each do |error|
              xml.add_node(property, "error", error)
            end
          end

          doc
        end
      end
    end

  end
end
