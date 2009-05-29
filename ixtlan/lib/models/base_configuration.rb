class BaseConfiguration
  include DataMapper::Resource

  def self.name
    "configuration"
  end

  property :id, Serial

  property :session_idle_timeout, Integer, :nullable => false 

  property :keep_audit_logs, Integer, :nullable => false 

  timestamps :at

  def self.instance
    @instance ||= get!(1)
  end

  def to_s
    "Configuration()"
  end
end
