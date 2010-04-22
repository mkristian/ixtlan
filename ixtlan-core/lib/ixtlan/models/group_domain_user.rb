module Ixtlan
  module Models
    class GroupDomainUser

      include DataMapper::Resource

      def self.default_storage_name
        "GroupDomainUser"
      end

      property :group_id, Integer, :key => true

      property :user_id, Integer, :key => true

      property :domain_id, Integer, :key => true

      belongs_to :group, :model => Models::GROUP
      belongs_to :user, :model => Models::USER
      belongs_to :domain, :model => Models::DOMAIN
    end
  end
end
