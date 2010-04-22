if RUBY_PLATFORM =~ /java/
  require 'zlib'
  class Zlib::GzipWriter
    def <<(arg)
      write(arg)
    end
  end
end
# fix with rails development mode and class reloading
# not sure where the exact problem is :-(
module Extlib
  module Assertions
    def assert_kind_of(name, value, *klasses)
      # be less strict and allow matching class names to OK as well
      klasses.each { |k| return if value.kind_of?(k) or value.class.name == k.name }
      raise ArgumentError, "+\#{name}+ should be \#{klasses.map { |k| k.name } * ' or '}, but was \#{value.class.name}", caller(2)
    end
  end
end
if RUBY_PLATFORM =~ /java/
  module DataMapper
    module Validate
      class NumericValidator

        def validate_with_comparison(value, cmp, expected, error_message_name, errors, negated = false)
          return if expected.nil?
          if cmp == :=~
              return value =~ expected
          end
          comparison = value.send(cmp, expected)
          return if negated ? !comparison : comparison

          errors << ValidationErrors.default_error_message(error_message_name, field_name, expected)
        end
      end
    end
  end
end
