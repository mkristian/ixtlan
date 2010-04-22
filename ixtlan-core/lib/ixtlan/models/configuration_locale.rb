module Ixtlan
  module Models
    class ConfigurationLocale

      include DataMapper::Resource

      def self.default_storage_name
        "ConfigurationLocale"
      end

      property :configuration_id, Integer, :key => true

      property :locale_id, Integer, :key => true

      belongs_to :configuration, :model => Models::CONFIGURATION
      belongs_to :locale, :model => Models::LOCALE
    end
  end
end
