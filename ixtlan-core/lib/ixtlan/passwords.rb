module Ixtlan
  class Passwords
    def self.generate(length=64)
      if length > 0
        # A-Z starting with 97 and having 26 characters
        # a-z starting with 65 and having 26 characters
        # 0-9 starting with 48 and lies inside range starting with 33 and having 26 characters
        offset=[97, 65, 33]

        # collect random characters from the either of the above ranges
        begin
          pwd = (0..(length - 1)).collect do
            j = ActiveSupport::SecureRandom.random_number(78)
            (offset[j / 26] + (j % 26)).chr
          end.join
        end while !((pwd =~ /[a-z]/) && (pwd =~ /[A-Z]/) && (pwd =~ /[!-;]/))
        pwd
      end
    end
  end
end
