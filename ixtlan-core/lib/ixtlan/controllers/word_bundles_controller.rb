module Ixtlan
  module Controllers
    module WordBundlesController

      def show
        locale = params[:id]
        # TODO load in following order and allow to replace findings in the
        # intermediate result set
        # * DEFAULT not_approved
        # * DEFAULT latest_approved
        # * locale-parent latest_approved
        # * locale latest_approved
        l = Locale.first(:code => locale) || Locale.get!(locale)
        wordMap = {}
        Ixtlan::Models::Word.not_approved(:locale => Locale.default).each do |word|
          wordMap[word.code] = word
        end
        Ixtlan::Models::Word.approved(:locale => Locale.default).each do |word|
          wordMap[word.code] = word
        end

        render :xml => "<word_bundle><locale>#{locale}</locale><words>" + wordMap.values.collect { |w| w.to_xml }.join + "</words></word_bundle>"
      end
    end
  end
end
