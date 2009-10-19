require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'

require 'optimistic_persistence'

class Name
  include DataMapper::Resource

  property :name, String, :length => 2..255, :key =>  true

  timestamps :updated_at
end
  
class Number
  include DataMapper::Resource

  property :id, Serial
  property :number, Integer
end
  
describe "Ixtlan::OptimisticPersistence" do

  before :each do
    @name = Name.create(:name => "frodo")
    @second = Name.first
    @number = Number.first_or_create(:number => 123)
    @other = Number.first
  end

  it 'should save' do
    @name.name = "gandalf"
    @name.save.should be_true
  end

  it 'should fail' do
    @name.name = "gandalf"
    @name.save.should be_true
    @second.name = "saroman"
    lambda { @second.save }.should raise_error(DataMapper::StaleResourceError)
  end

  it 'should treat non optimistic resources as usual' do
    @number.number = 345
    @number.save.should be_true
    @other.number = 987
    @other.save.should be_true
  end
end
