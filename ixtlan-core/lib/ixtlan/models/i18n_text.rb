require 'dm-serializer'
module Ixtlan
  module Models
    module I18nText

      unless const_defined? "TEXT_LOCALE"
        TEXT_LOCALE = Object.full_const_get(Models::LOCALE)
      end

      def invariant
        no_version = original_attributes[:version].nil? && attribute_get(:version).nil?
        if no_version
          if (!attribute_get(:version).nil? && attribute_dirty?(:version) && attribute_dirty?(:text))
            [false, 'can not approve and change text at the same time']
          elsif new?
            [true]
          elsif attribute_dirty?(:code)
            [false, 'code is invariant']
          else
            [true]
          end
        else
          if (!attribute_get(:version).nil? && attribute_dirty?(:version))
            if (attribute_dirty?(:text))
              [false, 'can not approve and change text at the same time']
            elsif !original_attributes[properties[:version]].nil?
              [false, 'can not change version']
            else
              [true]
            end
          elsif attribute_dirty?(:code)
            [false, 'can not change code']
          elsif attribute_dirty?(:text)
            [false, 'can not change text']
          else
            [true]
          end
        end
      end

      def cascade
        size =
          case locale.code.size
          when 2
            self.model.latest_approved(:code => code, :locale => TEXT_LOCALE.default).size
          when 5
            self.model.latest_approved(:code => code, :locale => TEXT_LOCALE.first(:code => locale.code[0,1])).size
          else
            1
          end
        if(size == 0)
          [false, "parent text for '#{code}' does not exists"]
        else
          [true]
        end
      end

      def approve(params = {})
        latest = self.class.latest_approved(:code => attribute_get(:code),
                                            :locale => locale).first
        previous = self.class.second_latest_approved(:code => attribute_get(:code),
                                                     :locale => locale).first
        params[:version] = latest.nil? ? 1 : latest.version + 1
        params[:current] = true
        # TODO approved is not need since after approval the resource is inmutable !!!
        params[:approved_at] = attribute_get(:updated_at)
        params[:approved_by] = params[:current_user] || current_user

        prev = (previous.nil? ? true : previous.update(:previous => false,
                                                    :current_user => params[:current_user] || current_user))
        lat = (latest.nil? ? true : latest.update(:current => false,
                                                :previous => true,
                                                :current_user => params[:current_user] || current_user))
        upd = update(params)

        upd && lat && prev
      end

      def approved?
        !attribute_get(:version).nil?
      end

      def self.included(model)
        model.send(:include, DataMapper::Resource)
        model.send(:include, UpdateChildren)

        model.property :id, ::DataMapper::Types::Serial

        model.property :code, String, :required => true, :length => 64

        model.property :text, String, :required => true, :length => 256

        model.property :version, Integer, :required => false

        model.property :current, ::DataMapper::Types::Boolean, :required => true, :auto_validation => false

        model.property :previous, ::DataMapper::Types::Boolean, :required => true, :auto_validation => false

        model.property :updated_at, DateTime, :required => true, :auto_validation => false

        model.belongs_to :updated_by, :model => Models::USER

        model.property :approved_at, DateTime, :required => false

        model.belongs_to :approved_by, :model => Models::USER, :required => false

        model.belongs_to :locale, :model => Models::LOCALE

        model.validates_with_method :invariant

        model.validates_with_method :cascade
        model.before :save do
          if(new? or attribute_get(:version).nil?)
            attribute_set(:version, nil)
            attribute_set(:current, false)
            attribute_set(:previous, false)
            attribute_set(:approved_at, nil)
            approved_by = nil
          end
        end

        model.class_eval <<-EOS, __FILE__, __LINE__
        def self.default_storage_name
          "Text"
        end

        def self.latest_approved(args = {})
          args[:current] = true
          all(args)
        end

        def self.second_latest_approved(args = {})
          args[:previous] = true
          all(args)
        end

        def self.approved(args = {})
          args[:version.not] = nil
          all(args)
        end

        def self.not_approved(args = {})
          args[:version] = nil
          all(args)
        end
EOS
      end
    end
  end
end
