module Ixtlan
  module Models
    class Text
      include DataMapper::Resource

      def self.default_storage_name
        "Text"
      end

      property :id, Serial
      
      property :code, String, :nullable => false, :length => 64
      
      property :text, String, :nullable => false, :length => 256
      
      property :version, Integer, :nullable => true
      
      property :current, Boolean, :nullable => false, :auto_validation => false

      property :previous, Boolean, :nullable => false, :auto_validation => false

      property :updated_at, DateTime, :nullable => false, :auto_validation => false
      
      belongs_to :updated_by, :model => Models::USER
      
      property :approved_at, DateTime, :nullable => true
      
      belongs_to :approved_by, :model => Models::USER, :nullable => true
      
      belongs_to :locale, :model => Models::LOCALE

      validates_with_method :invariant

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

      validates_with_method :cascade

      def cascade
        size = 
          case locale.code.size
          when 2
            self.model.latest_approved(:code => code, :locale => Locale.default).size 
          when 5
            self.model.latest_approved(:code => code, :locale => Locale.get(locale.code[0,1])).size
          else
            1
          end
        if(size == 0)
          [false, "parent text for '#{code}' does not exists"]
        else
          [true]
        end
      end

      before :save do
        if(new? or attribute_get(:version).nil?)
          attribute_set(:version, nil)
          attribute_set(:current, false)
          attribute_set(:previous, false)
          attribute_set(:approved_at, nil)
          approved_by = nil
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
        
        p = (previous.nil? ? true : previous.update(:previous => false,
                                                    :current_user => params[:current_user] || current_user))
        l = (latest.nil? ? true : latest.update(:current => false, 
                                                :previous => true,
                                                :current_user => params[:current_user] || current_user))
        u = update(params)
        
        u && l && p
      end
      
      def approved?
        !attribute_get(:version).nil?
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
    end
  end
end

