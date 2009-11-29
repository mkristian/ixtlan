module Ixtlan
  module Models
    class GroupLocaleUser

      include DataMapper::Resource
      
      def self.default_storage_name
        "GroupLocaleUser"
      end

      property :group_id, Integer, :key => true
      
      property :user_id, Integer, :key => true
      
      property :locale_code, String, :key => true

      belongs_to :group, :model => Group.to_s
      belongs_to :user, :model => User.to_s
      belongs_to :locale, :model => Locale.to_s
    end
  end
end
