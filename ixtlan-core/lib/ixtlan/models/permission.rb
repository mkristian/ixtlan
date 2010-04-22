require 'dm-serializer'
module Ixtlan
  module Models
    class Permission

      include DataMapper::Resource

      def self.default_storage_name
        "Permission"
      end

      property :resource, String,:format => /^[a-zA-Z0-9_.]*$/, :key => true

      property :action, String, :format => /^[a-zA-Z0-9_.]*$/, :key => true

      has n, :roles, :model => Models::ROLE

      if protected_instance_methods.find {|m| m == 'to_x'}.nil?

        protected

        alias :to_x :to_xml_document
        def to_xml_document(opts, doc = nil)
          opts.merge!({:methods => [:roles]})
          to_x(opts, doc)
        end
      end
    end
  end
end
