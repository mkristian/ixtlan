require 'dm-serializer'
module Ixtlan
  module Models
    class Role

      include DataMapper::Resource

      def self.default_storage_name
        "Role"
      end

      property :name, String, :required => true , :format => /^[a-zA-Z0-9\-_.]*$/, :length => 32, :key => true

      def hash
        attribute_get(:name).hash
      end

      alias :eql? :==
      def ==(other)
        attribute_get(:name).eql?(other.attribute_get(:name))
      end

      if protected_instance_methods.find {|m| m == 'to_x'}.nil?

        protected

        alias :to_x :to_xml_document
        def to_xml_document(opts, doc = nil)
          opts.merge!({:exclude => [:permission_resource,:permission_action]})
          to_x(opts, doc)
        end
      end
    end
  end
end
