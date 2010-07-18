require 'yaml'
require 'erb'
module Ixtlan
  class Configurator

    def self.symbolize_keys(h)
      result = {}

      h.each do |k, v|
        v = ' ' if v.nil?
        if v.is_a?(Hash)
          result[k.to_sym] = symbolize_keys(v) unless v.size == 0
        else
          result[k.to_sym] = v unless k.to_sym == v.to_sym
        end
      end

      result
    end

    def self.load(file)
      symbolize_keys(YAML::load(ERB.new(IO.read(file)).result)) if File.exists?(file)
    end
  end
end
