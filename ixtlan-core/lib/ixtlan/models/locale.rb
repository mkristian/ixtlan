require 'dm-serializer'
module Ixtlan
  module Models
    module Locale

      unless const_defined? "DEFAULT"
        DEFAULT = "DEFAULT"
        ALL = "ALL"
      end

      def self.included(model)
        model.send(:include, DataMapper::Resource)

        model.property :id, ::DataMapper::Types::Serial

        model.property :code, String, :required => true , :format => /^[a-z][a-z](_[A-Z][A-Z])?$|^#{DEFAULT}$|^#{ALL}$/, :length => 7, :unique_index => true

        model.timestamps :created_at

        model.modified_by Models::USER, :created_by

        model.class_eval <<-EOS, __FILE__, __LINE__
      def self.default
        first(:code => DEFAULT)
      end

      def self.every
        first(:code => ALL)
      end

      def self.first_or_get!(id_or_code)
        first(:code => id_or_code) || get!(id_or_code)
      end

      def self.first_or_get(id_or_code)
        first(:code => id_or_code) || get(id_or_code)
      end
EOS
    end

    def parent
      c = attribute_get(:code)
      case c.size
      when 2
        self.model.default
      when 5
        self.model.get!(code[0,2])
      else
        nil
      end
    end

    def hash
      attribute_get(:code).hash
    end

    alias :eql? :==
      def ==(other)
        attribute_get(:code).eql?(other.attribute_get(:code))
      end
    end
  end
end
