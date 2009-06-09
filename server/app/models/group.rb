class Group
  include DataMapper::Resource
  property :id, Serial, :field => "gidnumber"

  property :name, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 32, :field => "cn"
  property :description, Text, :nullable => false , :format => /^[^<'&">]*$/, :length => 32

  dn_prefix { |group| "cn=#{group.name}" }
  
  treebase "ou=groups"
  
  ldap_properties {{ :objectclass => "posixGroup"}}

  def users
    users = GroupUser.all(:gidnumber => id).collect{ |gu| gu.user }
    def users.group=(group)
      @group = group
    end
    users.group = self
    def users.<<(user)
      unless member? user
        GroupUser.create(:memberuid => user.login, :gidnumber => @group.id)
        super
      end
      self
    end
    def users.delete(user)
      gu = GroupUser.first(:memberuid => user.login, :gidnumber => @group.id)
      if gu
        gu.destroy
        super
      end
    end
    users
  end

  alias :to_x :to_xml_document
  def to_xml_document(opts = {}, doc = nil)
    to_x({:exclude => [:description]}, doc)
  end

  def to_s
    "Group(#{attribute_get(:id)})"
  end
end
