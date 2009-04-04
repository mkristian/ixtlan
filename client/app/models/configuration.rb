class Configuration
  include DataMapper::Resource

  property :id, Serial

  property :session_idle_timeout, Integer, :nullable => false 

  timestamps :at

  def self.instance
    get!(1)
  end
end
