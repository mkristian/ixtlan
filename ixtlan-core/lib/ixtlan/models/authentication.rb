require 'dm-serializer'
module Ixtlan
  module Models
    class Authentication 
      include DataMapper::Resource

      def self.name
        "Authentication"
      end

      property :id, Serial, :default => 1

      property :login, String,:format => /^[a-zA-Z0-9\-!=+$%^&*\(\){}|\[\]<>_.]*$/
      
      property :password, String,:format => /^[a-zA-Z0-9_.]*$/
      
      attr_accessor :token
      
      belongs_to :user, :model => ::Ixtlan::Models::USER

      if protected_instance_methods.find {|m| m == 'to_x'}.nil?
       
        protected

        alias :to_x :to_xml_document
        def to_xml_document(opts, doc = nil)
          opts.merge!({:exclude => [:password,:user_id], :methods => [:token, :user]})
          to_x(opts, doc)
        end
      end
    end
  end
end
