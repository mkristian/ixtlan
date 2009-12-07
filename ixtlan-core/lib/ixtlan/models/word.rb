module Ixtlan
  module Models
    class Word < Text

      alias :to_x :to_xml_document
      def to_xml_document(opts, doc = nil)
        opts.merge!({:element_name => "word", :exclude => [:id, :locale_code, :current, :previous, :updated_at, :approved_at, :updated_by_id, :approved_by_id, :version]})
        to_x(opts, doc)
      end
    end
  end
end