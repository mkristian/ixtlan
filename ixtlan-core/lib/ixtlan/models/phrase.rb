require 'dm-serializer'
module Ixtlan
  module Models
    class Phrase
      include DataMapper::Resource

      def self.default_storage_name
        "Phrase"
      end

      property :id, Serial

      property :code, String, :nullable => false, :length => 64
      
      property :text, String, :nullable => false, :length => 256
      
      property :current_text, String, :nullable => true, :length => 256
      
      property :updated_at, DateTime, :nullable => false, :auto_validation => false
      
      belongs_to :updated_by, :model => Models::USER
      
      belongs_to :locale, :model => Models::LOCALE
      
      belongs_to :default_translation, :model => Models::TRANSLATION, :nullable => true

      belongs_to :parent_translation, :model => Models::TRANSLATION, :nullable => true

      alias :to_x :to_xml_document
      def to_xml_document(opts = {}, doc = nil)
        opts.merge!({:exclude => [:id, :parent_translation_id, :default_translation_id, :locale_code], :element_name => 'phrase', :methods => [:default_translation, :parent_translation, :locale]})
        to_x(opts, doc)
      end

      def self.all(args = {})
        phrases = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Ixtlan::Models::Phrase), [])
        map = {}
        locale = (args[:locale] ||= Locale.default)
        I18nText.not_approved(args.dup).each do |text|
          phrase = Phrase.new(:code => text.code, :text => text.text, :current_text => text.text, :locale => locale, :updated_at => text.updated_at, :updated_by => text.updated_by)
          map[phrase.code] = phrase
        end
        I18nText.latest_approved(args.dup).each do |text|
          if (phrase = map[text.code])
            phrase.current_text = text.text
          else
            phrase = Phrase.new(:code => text.code, :text => text.text, :current_text => text.text, :locale => locale, :updated_at => text.updated_at, :updated_by => text.updated_by)
            map[phrase.code] = phrase
          end
        end
        case locale.code.size
        when 2
          params = args.dup
          params[:locale] = Locale.default
          Translation.map_for(params).each do |code, trans|
            ph = map[code]
            if(ph.nil?)
              map[code] = Phrase.new(:code => code, :text => trans.text, :current_text => trans.text, :locale => locale, :updated_at => trans.approved_at, :updated_by => trans.approved_by, :default_translation => trans)
            else  
              ph.default_translation = trans
            end
          end
        when 5
          raise "not implemented yet"
        end
        map.values.each { |ph| phrases << ph}
        phrases
      end
    end
  end
end

