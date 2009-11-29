module Ixtlan
  module Models
    class ConfigurationLocale

      include DataMapper::Resource
      
      def self.default_storage_name
        "ConfigurationLocale"
      end

      property :configuration_id, Integer, :key => true
      
      property :locale_code, String, :key => true

      belongs_to :configuration, :model => Configuration.to_s
      belongs_to :locale, :model => Locale.to_s
    end
  end
end
