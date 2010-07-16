require 'rubygems'
lib_path = (Pathname(__FILE__).dirname.parent.expand_path + 'lib').to_s
$LOAD_PATH.unshift lib_path unless $LOAD_PATH.include?(lib_path)

require 'ixtlan/passwords'

describe Ixtlan::Passwords do

  it 'should create a password of given length' do
    pwd = Ixtlan::Passwords.generate(123)
    ((pwd =~ /[a-z]/) && (pwd =~ /[A-Z]/) && (pwd =~ /[!-;]/)).should be_true
    pwd.size.should == 123
  end

  it 'should create a numeric password of given length' do
    pwd = Ixtlan::Passwords.generate_numeric(321)
    pwd.should =~ /^[0-9]*$/
    pwd.size.should == 321
  end

end
