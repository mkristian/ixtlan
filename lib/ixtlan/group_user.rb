module Ixtlan
  class GroupUser
    include DataMapper::Resource
    #  include Slf4r::Logger
 
    # dn_prefix { |group_user| "cn=#{group_user.group.name}" }
    
    #treebase "ou=groups"
    
    # multivalue_field :memberuid
    
    #ldap_properties do |group_user|
    #   {:cn=>"#{group_user.group.name}",  :objectclass => "posixGroup"}
    # end
    property :memberuid, String, :key => true
    property :gidnumber, Integer, :key => true
    
    def group
      Ixtlan::Group.get!(gidnumber)
    end
    
    def group=(group)
      gidnumber = group.id
    end
    
    def user
      Ixtlan::User.first(:login => memberuid)
    end
    
    def user=(user)
      memberuid = user.login
    end
  end
end
