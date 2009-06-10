require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'
require 'passwords'
require 'active_support'

describe Passwords do

  it 'should generate a valid password' do
    pwd = Passwords.generate
    pwd.size.should == 64
    ((pwd =~ /[a-z]/) && (pwd =~ /[A-Z]/) && (pwd =~ /[0-9]/)).should be_true

    pwd = Passwords.generate(32)
    pwd.size.should == 32
    ((pwd =~ /[a-z]/) && (pwd =~ /[A-Z]/) && (pwd =~ /[0-9]/)).should be_true
  end
end
