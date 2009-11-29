module Ixtlan
  module Models
    class GroupUser
      include DataMapper::Resource

      def self.default_storage_name
        "GroupUser"
      end

      # dn_prefix { |group_user| "cn=#{group_user.group.name}" }
      
      #treebase "ou=groups"
      
      # multivalue_field :memberuid
      
      #ldap_properties do |group_user|
      #   {:cn=>"#{group_user.group.name}",  :objectclass => "posixGroup"}
      # end
      property :memberuid, String, :key => true
      property :gidnumber, Integer, :key => true
      
      def group
        Group.get!(gidnumber)
      end
      
      def group=(group)
        gidnumber = group.id
      end
      
      def user
        User.first(:login => memberuid)
      end
      
      def user=(user)
        memberuid = user.login
      end
    end
  end
end
