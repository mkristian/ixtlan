module Ixtlan
  module Models
    class GroupLocaleUser

      include DataMapper::Resource

      def self.default_storage_name
        "GroupLocaleUser"
      end

      property :group_id, Integer, :key => true

      property :user_id, Integer, :key => true

      property :locale_id, Integer, :key => true

      belongs_to :group, :model => Models::GROUP
      belongs_to :user, :model => Models::USER
      belongs_to :locale, :model => Models::LOCALE
    end
  end
end
