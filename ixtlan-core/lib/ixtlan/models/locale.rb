require 'dm-serializer'
module Ixtlan
  module Models
    class Locale
      include DataMapper::Resource

      def self.default_storage_name
        "Locale"
      end

      unless const_defined? "DEFAULT"
        DEFAULT = "DEFAULT"
        ALL = "ALL"
      end

      property :id, Serial
      property :code, String, :required => true , :format => /^[a-z][a-z](_[A-Z][A-Z])?$|^#{DEFAULT}$|^#{ALL}$/, :length => 7, :unique_index => true
      
      timestamps :created_at

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

      def self.default
        first_or_create(:code => DEFAULT)
      end

      def self.every
        first_or_create(:code => ALL)
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
