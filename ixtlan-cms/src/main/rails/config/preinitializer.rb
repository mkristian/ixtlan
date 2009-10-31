require 'yaml'
require 'erb'
module Ixtlan
  class Configurator

    def self.symbolize_keys(h, compact)
      result = {}
      
      h.each do |k, v|
        v = ' ' if v.nil?
        if v.is_a?(Hash)
          result[k.to_sym] = symbolize_keys(v, compact) unless compact and v.size == 0
        else
          result[k.to_sym] = v unless compact and k.to_sym == v.to_sym
        end
      end
      
      result
    end
    
    def self.load(dir, file, compact = true)
      symbolize_keys(YAML::load(ERB.new(IO.read(File.join(dir, file))).result), compact)
    end
  end
end

CONFIG = Ixtlan::Configurator.load(File.dirname(__FILE__), 'global.yml')
