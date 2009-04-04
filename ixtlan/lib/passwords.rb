class Passwords
  def self.generate(length=64)
    # A-Z starting with 97 and having 26 characters
    # a-z starting with 65 and having 26 characters
    # 0-9 starting with 48 and having 10 characters
    offset=[[26,97], [26,65], [10,48]]
    
    # collect 8 random characters from the either of the above ranges
    begin
      #TODO use secure random somehow !!!
      pwd = (1..length).collect { (j= Kernel.rand(3); i = Kernel.rand(offset[j][0]); i += offset[j][1]).chr }.join
    end while !((pwd =~ /[a-z]/) && (pwd =~ /[A-Z]/) && (pwd =~ /[0-9]/))
    pwd
  end
end
