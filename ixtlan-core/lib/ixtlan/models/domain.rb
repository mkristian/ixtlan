require 'dm-serializer'
module Ixtlan
  module Models
    module Domain

      unless const_defined? "ALL"
        ALL = "ALL"
      end

      def self.included(model)
        model.send(:include, DataMapper::Resource)

        model.property :id, ::DataMapper::Types::Serial

        model.property :name, String, :required => true , :format => /^[a-z]+$|^#{ALL}$/, :length => 32, :unique_index => true

        model.timestamps :created_at

        model.modified_by Models::USER, :created_by

        model.class_eval <<-EOS, __FILE__, __LINE__
      def self.every
        first(:name => ALL)
      end

      def self.first_or_get!(id_or_name)
        first(:name => id_or_name) || get!(id_or_name)
      end

      def self.first_or_get(id_or_name)
        first(:name => id_or_name) || get(id_or_name)
      end
EOS
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
