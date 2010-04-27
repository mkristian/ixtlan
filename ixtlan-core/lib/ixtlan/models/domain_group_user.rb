module Ixtlan
  module Models
    class DomainGroupUser

      include DataMapper::Resource

      def self.default_storage_name
        "DomainGroupUser"
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
