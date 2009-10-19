module Ixtlan
  class GroupLocale

    include DataMapper::Resource
    
    def self.default_storage_name
      "GroupLocale"
    end

    property :group_id, Integer, :key => true
    
    property :locale_code, String, :key => true

    belongs_to :group, :model => "::Ixtlan::Group"
    belongs_to :locale, :model => "::Ixtlan::Locale"
  end
end
