module Ixtlan
  module Controllers
    module WordBundlesController

      def self.included(base)
        # no guard since everyone needs to load the bundles
        base.skip_before_filter :guard

        base.cache_headers :protected
      end

      private

      LOCALE = Object.full_const_get(::Ixtlan::Models::LOCALE)

      public

      def index
        locale = params[:code]
        # TODO load in following order and allow to replace findings in the
        # intermediate result set
        # * DEFAULT not_approved
        # * DEFAULT latest_approved
        # * locale-parent latest_approved
        # * locale latest_approved
        l = LOCALE.first(:code => locale) || LOCALE.get!(locale)
        word_bundle = {}
        Ixtlan::Models::Word.not_approved(:locale => LOCALE.default).each do |word|
          word_bundle[word.code] = word
        end
        Ixtlan::Models::Word.approved(:locale => LOCALE.default).each do |word|
          word_bundle[word.code] = word
        end

        render :xml => "<word_bundle><locale>#{locale}</locale><words>" + word_bundle.values.collect { |w| w.to_xml }.join + "</words></word_bundle>" 
      end
    end
  end
end
