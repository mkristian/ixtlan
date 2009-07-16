class Word
  include DataMapper::Resource

  property :id, Serial
  property :code, String, :nullable => false
  property :text, String, :nullable => false
  timestamps :at

end
