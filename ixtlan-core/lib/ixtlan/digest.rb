module Ixtlan
  class Digest
    # method from openldap faq which produces the userPassword attribute
    # for the ldap
    # @param secret String the password
    # @param salt String the salt for the password digester
    # @return the encoded password/salt
    def self.ssha(secret, salt)
      require 'sha1'
      require 'base64'
      (salt.empty? ? "{SHA}": "{SSHA}") +
        Base64.encode64(::Digest::SHA1.digest(secret + salt) + salt).gsub(/\n/, '')
    end
  end
end
