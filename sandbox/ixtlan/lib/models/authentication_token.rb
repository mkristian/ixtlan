require Pathname(__FILE__).dirname + 'token.rb'
class AuthenticationToken < Token

  property :login, String

  property :password, String

  def new_token
  end
end
