#require 'dm-core'
module Ixtlan

  module ModifiedBy
    extend ::DataMapper::Chainable

    MODIFIED_BY_PROPERTIES = {
      :updated_by => lambda {|r, u| r.updated_by = u},
      :created_by => lambda {|r, u| r.created_by = u if r.new? }
    }.freeze

    def self.included(model)
      model.after :save do @current_user = nil; end
      model.extend(ClassMethods)
    end

    def current_user=(user)
      @current_user = user
    end

    private

    def current_user
      raise ::DataMapper::MissingCurrentUserError.new("current_user not set. it can be set like any other property") unless @current_user

      @current_user
    end

    chainable do
      def new(attributes = {}, &block)
        current_user= attributes.delete(:current_user)
        super(&block)
      end
    end

    chainable do
      def save(*args)
        set_modified_by if dirty?
        super()
      end
    end

    chainable do
      def update!(*args)
        set_modified_by if dirty?
        super()
      end
    end

    def set_modified_by
      MODIFIED_BY_PROPERTIES.each do |name, setter|
        if respond_to? name
          setter.call(self, current_user)
        end
      end
    end

    module ClassMethods
      def modified_by(type, names = nil, options = {})
        if(names.nil?)
          modified_by(type, [:created_by, :updated_by], options)
        else
          names = [names] unless names.is_a?(Enumerable)
          names.each do |name|
            case name
            when *MODIFIED_BY_PROPERTIES.keys
              belongs_to name, options.merge!({:model => type.to_s})
            else
              raise ::DataMapper::InvalidModifiedByName, "Invalid 'modified by' name '#{name}'"
            end
          end
        end
      end
    end

    class ::DataMapper::InvalidModifiedByName < RuntimeError; end
    class ::DataMapper::MissingCurrentUserError < RuntimeError; end

    ::DataMapper::Model.append_inclusions self
  end
end
