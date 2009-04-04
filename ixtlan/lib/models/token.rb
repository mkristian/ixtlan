class Token
  include DataMapper::Resource

  def self.name
    "single_sign_on"
  end

  property :token, String, :key => true, :format => /^[a-zA-Z0-9]*$/, :length => 64, :auto_validation => false

  property :one_time, String, :nullable => true , :format => /^[a-zA-Z0-9]*$/, :length => 64

  belongs_to :user, :class_name => Login

  before :valid?, :new_token

  def new_token
    if new_record?
      attribute_set(:token, ::Passwords.generate) unless attribute_get(:token)
    else
      attribute_set(:one_time, ::Passwords.generate) unless attribute_get(:one_time)
    end
  end
end
