class User  < Login
 # include DataMapper::Resource
 # property :id, Serial, :field => "uidnumber"

 # property :login, String, :nullable => false , :length => 4..32, :index => :unique_index, :format => /^[a-zA-z0-9]*$/, :writer => :private, :field => "uid"
 # property :name, String, :nullable => false, :format => /^[^<">]*$/, :length => 2..64, :field => "cn"
 # property :email, String, :nullable => false, :format => :email_address, :nullable => false, :length => 8..64, :index => :unique_index, :field => "mail"
  property :hashed_password, String,# :nullable => false, 
  :size => 128, :accessor => :private, :field => "userpassword"

  property :created_at, DateTime#, :nullable => false 
  property :updated_at, DateTime#, :nullable => false 

  # Virtual attribute for the plaintext password
  attr_accessor :password, :password_confirmation

  validates_is_unique  :login
  validates_is_unique  :email

  before :save, :check_password

  def check_password
    unless (password.blank? and password_confirmation.blank?)
      errors.clear
      errors.add(:password, "#{password} password must not be blank") if password.blank?      
      errors.add(:password, "password must have a small letter") unless password =~ /[a-z]/
      errors.add(:password, "password must have a capital letter") unless password =~ /[A-Z]/
      errors.add(:password, "password must have a number") unless password =~ /[0-9]/
      errors.add(:password, "password and password confirmation do not match") if password != password_confirmation
      throw :halt if errors.size > 0
    end
  end

  # # needed for guard
  def roles
     roles = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Role))
    groups.each do |group|
      roles << Role.new(:id => group.id, :name => group.name)
    end
    roles
  end

  def groups
    groups = ::GroupUser.all(:memberuid => login).collect{ |gu| gu.group } || []
    

    #puts "groups"
    #p groups
    
    # p ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Group)) do |c|
#       c.replace(groups)
#     end
#     puts

    #p groups
    def groups.user=(user)
      @user = user
    end
    groups.user = self
    def groups.<<(group)
      unless member? group
        GroupUser.create(:memberuid => @user.login, :gidnumber => group.id)
        super
      end

      self
    end
    def groups.delete(group)
# puts 
# p group
# p @user
      gu = GroupUser.first(:memberuid => @user.login, :gidnumber => group.id)
#p gu
      if gu
        gu.destroy
        super
      end
    end
    groups
  end

  dn_prefix { |user| "uid=#{user.login}"}

  treebase "ou=people"

  ldap_properties do |user|
    properties = { :objectclass => ["inetOrgPerson", "posixAccount", "shadowAccount"], :gidnumber => "10000" }
    properties[:sn] = "#{user.name.sub(/.*\ /, "")}"
    properties[:givenname] = "#{user.name.sub(/\ .*/, "")}"
    properties[:homedirectory] = "/home/#{user.login}"
    properties
  end

  # make sure login is immutable
 # def login=(new_login)
 #   attribute_set(:login, new_login) if (login.nil? or login == "'NULL'" or login == "NULL")
 # end

  def password=(password)
    @password = password
    attribute_set(:hashed_password, Ldap::Digest.ssha(password, "--#{Time.now}--#{login}--")) if password == password_confirmation and not password.blank?
  end
  
  def password_confirmation=(password_confirmation)
    @password_confirmation = password_confirmation
    attribute_set(:hashed_password, Ldap::Digest.ssha(password, "--#{Time.now}--#{login}--")) if password == password_confirmation and not password.blank?
  end

  def reset_password(len = 12)
    pwd = Passwords.generate(len)
    self.password = pwd
    self.password_confirmation = pwd
  end

  def self.authenticate(login, password)
    u = first(:login => login) # need to get the salt
    if u
      digest = Base64.decode64(u.inspect.sub(/.*hashed_password=..SSHA./, "").sub(/\".*/,''))
      salt = digest[20,147]
      u if ::Digest::SHA1.digest("#{password}" + salt) == digest[0,20]
    end
  end

#  alias :to_x :to_xml_document
  def to_xml_document
    to_x(:exclude => [:hashed_password, :created_at, :updated_at], :methods => [:roles])
  end
end


