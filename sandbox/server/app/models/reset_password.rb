class ResetPassword
  include DataMapper::Resource
  property :token, String, :key => true, :format => /^[a-zA-Z0-9]*$/, :length => 64, :auto_validation => false

  property :success_url, String, :nullable => false

  property :ip, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 32
  
  property :expired_at, DateTime, :nullable => false, :auto_validation => false

  belongs_to :user

  before :save, :new_token

  def new_token
    attribute_set(:token, Passwords.generate) unless attribute_get(:token)
    attribute_set(:expired_at, 1.day.from_now)
  end
end
