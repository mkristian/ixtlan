begin
  require 'securerandom'
rescue LoadError
end

module Ixtlan
  class Passwords
    SecureRandom =
      if defined?(::SecureRandom)
        # Use Ruby's SecureRandom library if available.
        ::SecureRandom
      else
        begin
          # try if there is active support around ;-)
          require 'active_supportt'
          ::ActiveSupport::SecureRandom
        rescue LoadError
          warn 'could not find secure random implementation, fall back to rand()'
          class Random
            def self.random_number(n)
              (rand * n).to_i
            end
          end
          Random
        end
      end

    def self.generate(length=64)
      if length > 0
        # A-Z starting with 97 and having 26 characters
        # a-z starting with 65 and having 26 characters
        # 0-9 starting with 48 and lies inside range starting with 33 and having 26 characters
        offset=[97, 65, 33]
        begin
          # collect random characters from the either of the above ranges
          pwd = password(length, offset, 26)
        end while !((pwd =~ /[a-z]/) && (pwd =~ /[A-Z]/) && (pwd =~ /[!-;]/))
        pwd
      end
    end

    def self.generate_numeric(length=64)
      if length > 0
        # 0-9 starting with 48 with range of 10 characters
        offset=[48]
        # collect random characters from the either of the above ranges
        password(length, offset, 10)
      end
    end

    private

    def self.password(length, offset, max_range)
      pwd = (0..(length - 1)).collect do
        j = SecureRandom.random_number(offset.size * max_range)
        (offset[j / max_range] + (j % max_range)).chr
      end.join
    end
  end
end
