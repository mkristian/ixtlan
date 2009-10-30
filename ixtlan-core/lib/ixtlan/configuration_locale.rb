module Ixtlan
  class ConfigurationLocale

    include DataMapper::Resource
    
    def self.default_storage_name
      "ConfigurationLocale"
    end

    property :configuration_id, Integer, :key => true
    
    property :locale_code, String, :key => true

    belongs_to :configuration, :model => "::Ixtlan::Configuration"
    belongs_to :locale, :model => "::Ixtlan::Locale"
  end
end
