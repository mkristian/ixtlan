module Ixtlan
  class Group

    # hack to get xml serializing working
    def self.name
      "Group"
    end
    def self.to_s
      "Group"
    end

    include DataMapper::Resource
    
    def self.default_storage_name
      "Group"
    end

    property :id, Serial, :field => "gidnumber"
    
    property :name, String, :nullable => false , :format => /^[^<'&">]*$/, :length => 32, :field => "cn", :unique_index => true
    
    timestamps :created_at
    
    modified_by "::Ixtlan::User"

    def locales
      # TODO spec the empty array to make sure new relations are stored
      # in the database or the locales collection is empty before filling it
      _locales = ::DataMapper::Collection.new(::DataMapper::Query.new(self.repository, Ixtlan::Locale), [])
      Ixtlan::GroupLocale.all(:group_id => id).each{ |gl| _locales << gl.locale }
      def _locales.group=(group)
        @group = group
      end
      _locales.group = self
      def _locales.<<(locale)
        unless member? locale
          gl = Ixtlan::GroupLocale.create(:group_id => @group.id, :locale_code => locale.code)
          super
        end
        
        self
      end
      
      def _locales.delete(locale) 
        gl = Ixtlan::GroupLocale.first(:group_id => @group.id, :locale_code => locale.code)
        if gl
          gl.destroy
        end
        super
      end
      _locales
    end

    alias :to_x :to_xml_document
    def to_xml_document(opts, doc = nil)
      opts.merge!({:methods => [:locales]})
      to_x(opts, doc)
    end
  end
end
