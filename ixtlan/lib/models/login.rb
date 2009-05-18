class Login
  include DataMapper::Resource

  def self.name
    "user"
  end

  property :id, Serial, :field => "uidnumber"

  property :login, String, :nullable => false , :length => 4..32, :index => :unique_index, :format => /^[a-zA-z0-9]*$/, :writer => :private, :field => "uid"
  property :name, String, :nullable => false, :format => /^[^<">]*$/, :length => 2..64, :field => "cn"
  property :email, String, :nullable => false, :format => :email_address, :nullable => false, :length => 8..64, :index => :unique_index, :field => "mail"
  property :language, String, :nullable => false, :format => /[a-z][a-z]/, :length => 2, :field => "preferredlanguage"
  
  # needed for guard
#  def roles   
#    @roles ||= ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, ::Role, {}))
#    roles = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, ::Role, {})) do |c|
#       c.replace(groups)
#     end
#     roles
#    @roles ||= []
#  end
  #def groups
  #  @groups ||= ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, ::Role, {}))
#    @groups ||= []
  #end
  def roles 
    @roles ||= [] #groups
  end
  
  #   groups = GroupUser.all(:memberuid => login).collect{ |gu| gu.group } || []
#     def groups.user=(user)
#       @user = user
#     end
#     groups.user = self
#     def groups.<<(group)
#       unless member? group
#         GroupUser.create(:memberuid => @user.login, :gidnumber => group.id)
#         super
#       end
#       self
#     end
#     def groups.delete(group)
#       gu = GroupUser.first(:memberuid => @user.login, :gidnumber => group.id)
#       if gu
#         gu.destroy
#         super
#       end
#     end
#     ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Group, {})) do |c|
#       c.replace(groups)
#     end
#   end

  # make sure login is immutable
  def login=(new_login)
    attribute_set(:login, new_login) if (login.nil? or login == "'NULL'" or login == "NULL")
  end

  alias :to_x :to_xml_document
  def to_xml_document
    to_x(:methods => [:roles])
  end
end

# class GroupUser
#   include DataMapper::Resource
#   include Slf4r::Logger
 
#   dn_prefix { |group_user| "cn=#{group_user.group.name}" }
  
#   treebase "ou=groups"
  
#   multivalue_field :memberuid
  
#   ldap_properties do |group_user|
#     {:cn=>"#{group_user.group.name}",  :objectclass => "posixGroup"}
#   end
#   property :memberuid, String, :key => true
#   property :gidnumber, Integer, :key => true

#   def group
#     Group.get!(gidnumber)
#   end

#   def group=(group)
#     gidnumber = group.id
#   end

#   def user
#     User.first(:login => memberuid)
#   end

#   def user=(user)
#     memberuid = user.login
#   end

# end

