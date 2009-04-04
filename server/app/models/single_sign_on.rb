class SingleSignOn < Token
#  include DataMapper::Resource

#  property :token, String, :key => true, :format => /^[a-zA-Z0-9]*$/, :length => 64

  property :ip, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 32
  property :expired_at, DateTime, :nullable => false 

#  property :one_time, String, :nullable => true , :format => /^[a-zA-Z0-9]*$/, :length => 64

  property :created_at, DateTime, :nullable => true
  property :updated_at, DateTime, :nullable => true
 
  belongs_to :user, :class_name => User

  alias :create_token :new_token
  def new_token
    attribute_set(:expired_at, 1.day.from_now) unless attribute_get(:expired_at)
    create_token
  end
end
