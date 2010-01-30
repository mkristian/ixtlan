module Ixtlan
  module Models
    class Translation
      include DataMapper::Resource

      def self.default_storage_name
        "Translation"
      end
      
      property :id, Serial

      property :text, String, :nullable => false, :length => 256
      
      property :previous_text, String, :nullable => true, :length => 256
      
      property :approved_at, DateTime, :nullable => false, :auto_validation => false
      
      belongs_to :approved_by, :model => Models::USER
      
      def self.map_for(args = {})
        map = {}
        I18nText.latest_approved(args.dup).each do |text|
          map[text.code] = Translation.create(:text => text.text, :approved_at => text.approved_at, :approved_by => text.approved_by)
        end
        I18nText.second_latest_approved(args.dup).each do |text|
          translation = map[text.code]
          translation.previous_text = text.text
        end
        map
      end

      alias :to_x :to_xml_document
      def to_xml_document(opts = {}, doc = nil)
        opts.merge!({:exclude => [:id]})
        to_x(opts, doc)
      end
    end
  end
end

