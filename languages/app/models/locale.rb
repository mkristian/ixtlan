class Locale
  include DataMapper::Resource

  property :id, Serial
  property :language, String, :nullable => false
  property :country, String, :nullable => false
  timestamps :at
end
