require 'dm-serializer'
module Ixtlan
  module Models
    module Authentication

      def self.included(model)
        model.send(:include, DataMapper::Resource)

        model.property :id, ::DataMapper::Types::Serial, :default => 1

        model.property :login, String,:format => /^[a-zA-Z0-9\-!=+$%^&*\(\){}|\[\]<>_.]*$/

        model.property :password, String,:format => /^[a-zA-Z0-9_.]*$/

        model.belongs_to :user, :model => ::Ixtlan::Models::USER

        model.class_eval <<-EOS, __FILE__, __LINE__
          if protected_instance_methods.find {|m| m == 'to_x'}.nil?

            protected

            alias :to_x :to_xml_document
            def to_xml_document(opts, doc = nil)
              opts.merge!({:exclude => [:password,:user_id], :methods => [:user]})
              to_x(opts, doc)
            end
          end
EOS
      end
    end
  end
end
