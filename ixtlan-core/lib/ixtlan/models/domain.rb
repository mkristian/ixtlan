require 'dm-serializer'
module Ixtlan
  module Models
    class Domain
      include DataMapper::Resource

      def self.default_storage_name
        "Domain"
      end

      unless const_defined? "ALL"
        ALL = "ALL"
      end

      property :id, Serial
      property :name, String, :required => true , :format => /^[a-z]+$/, :length => 32, :unique_index => true

      timestamps :created_at

      modified_by Models::USER

      def self.every
        first(:name => ALL)
      end

      def self.first_or_get!(id_or_name)
        first(:name => id_or_name) || get!(id_or_name)
      end

      def self.first_or_get(id_or_name)
        first(:name => id_or_name) || get(id_or_name)
      end

      def hash
        attribute_get(:name).hash
      end

      alias :eql? :==
        def ==(other)
          attribute_get(:name).eql?(other.attribute_get(:name))
        end
      end
    end
end
