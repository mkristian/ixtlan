require 'dm-serializer'
require 'ixtlan/guard'
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

            def permissions
              ::Ixtlan::Guard.permissions(user)
            end

            alias :to_x :to_xml_document
            def to_xml_document(opts, doc = nil)
              opts.merge!({
                            :skip_types => true,
                            :skip_empty_tags => true,
                            :exclude => [:password, :user_id, :id], 
                            :methods => [:user, :permissions]
                          })
              to_x(opts, doc)
            end
          end
EOS
      end
    end
  end
end
