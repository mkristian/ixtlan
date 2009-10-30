module Ixtlan
  class GroupLocaleUser

    include DataMapper::Resource
    
    def self.default_storage_name
      "GroupLocaleUser"
    end

    property :group_id, Integer, :key => true
    
    property :user_id, Integer, :key => true
    
    property :locale_code, String, :key => true

    belongs_to :group, :model => "::Ixtlan::Group"
    belongs_to :user, :model => "::Ixtlan::User"
    belongs_to :locale, :model => "::Ixtlan::Locale"
  end
end
