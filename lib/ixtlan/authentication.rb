module Ixtlan
  class Authentication 
    include DataMapper::Resource
    
    property :login, String,:format => /^[a-zA-Z0-9\-!=+$%^&*\(\){}|\[\]<>_.]*$/, :key => true
    
    property :password, String,:format => /^[a-zA-Z0-9_.]*$/
    
    attr_accessor :token
    
    belongs_to :user  
    
    alias :to_x :to_xml_document
    def to_xml_document(opts, doc = nil)
      opts.merge!({:element_name => 'authentication', :user => {:element_name => 'user'}, :exclude => [:password,:user_id], :methods => [:token, :user]})
      to_x(opts, doc)
    end
  end
end
