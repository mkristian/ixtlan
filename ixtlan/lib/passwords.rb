class Passwords
  def self.generate(length=64)
    # A-Z starting with 97 and having 26 characters
    # a-z starting with 65 and having 26 characters
    # 0-9 starting with 48 and having 10 characters
    offset=[[26,97], [26,65], [10,48]]
    
    # collect 8 random characters from the either of the above ranges
    begin
      #TODO use secure random somehow !!!
      bytes = ActiveSupport::SecureRandom.random_bytes(length * 2)
      
      pwd = (0..(length - 1)).collect do |idx| 
        j= bytes[idx * 2] % 3
        i = bytes[idx * 2 + 1] % offset[j][0]
        i += offset[j][1]
        i.chr
      end.join
    end while !((pwd =~ /[a-z]/) && (pwd =~ /[A-Z]/) && (pwd =~ /[0-9]/))
    pwd
  end
end
