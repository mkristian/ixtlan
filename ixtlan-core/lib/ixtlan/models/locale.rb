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

      property :code, String, :nullable => false , :format => /^[a-z][a-z](_[A-Z][A-Z])?$|^#{DEFAULT}$|^#{ALL}$/, :length => 7, :key => true
      
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
        get(DEFAULT) || create(:code => DEFAULT)
      end

      def self.every
        get(ALL) || create(:code => ALL)
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
