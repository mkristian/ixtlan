module Ixtlan
  class Authentication 
    include DataMapper::Resource

    def self.name
      "Authentication"
    end

    property :login, String,:format => /^[a-zA-Z0-9\-!=+$%^&*\(\){}|\[\]<>_.]*$/, :key => true
    
    property :password, String,:format => /^[a-zA-Z0-9_.]*$/
    
    attr_accessor :token
    
    belongs_to :user, :model => "::Ixtlan::User"
    
    alias :to_x :to_xml_document
    def to_xml_document(opts, doc = nil)
      opts.merge!({:exclude => [:password,:user_id], :methods => [:token, :user]})
      to_x(opts, doc)
    end
  end
end
