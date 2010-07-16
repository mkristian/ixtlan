module Ixtlan
  module Controllers
    module PhrasesController

      def self.included(base)
        base.cache_headers :protected
      end

      private

      LOCALE = Object.full_const_get(::Ixtlan::Models::LOCALE)
      TEXT = Object.full_const_get(::Ixtlan::Models::TEXT)

      public

      def index
        version = params[:version]

        locale = if params[:locale]
                   LOCALE.get!(params[:locale])
                 else
                   LOCALE.default
                 end

        phraseMap = {}
        Ixtlan::Models::Word.not_approved(:locale => Locale.default).each do |word|
          phraseMap[word.code] = word_to_phrase(word, :text)
        end
        Ixtlan::Models::Word.latest_approved(:locale => Locale.default).each do |word|
          phrase = phraseMap[word.code]
          if phrase
            merge_word_into_phrase(word, phrase)
          else
            phraseMap[word.code] = word_to_phrase(word, :current_text)
          end
        end
        phrases = ::DataMapper::Collection.new(::DataMapper::Query.new(repository, ::Ixtlan::Models::Phrase), [])
        phraseMap.each { |k,v| phrases << v }
        render :xml => phrases.to_xml
      end


      def show
        word = Ixtlan::Models::Word.not_approved(:locale => Locale.default, :code => params[:id]).first
        if word
          phrase = word_to_phrase(word, :text)
          word = Ixtlan::Models::Word.latest_approved(:locale => Locale.default, :code => params[:id]).first
          merge_word_into_phrase(word, phrase) if word
        else
          word = Ixtlan::Models::Word.latest_approved(:locale => Locale.default, :code => params[:id]).first
          phrase = word_to_phrase(word, :current_text)
        end
        render :xml => phrase.to_xml
      end

      def create
        phrase = params[:phrase]

        # load the locale and delete the locale parameter array
        locale = LOCALE.get!(phrase.delete(:locale)[:code])

        if(TEXT.count(:version => nil, :code => phrase[:code], :locale => locale) == 1)
          logger.warn "precondition failed: " + phrase.inspect
          # mimic created action by just loading it
          render :xml => TEXT.first(:version => nil, :code => phrase[:code], :locale => locale).to_xml, :status => :created
          return
        end

        phrase[:text] ||= phrase.delete(:current_text)

        @text = TEXT.new(phrase)
        if(TEXT.count(:code => phrase[:code], :locale => locale) == 0)
          approve_it = true
        end

        # set the missing attributes
        @text.locale = locale
        @text.current_user = current_user

        respond_to do |format|
          success = @text.save
          if success && approve_it
            @text.current_user = current_user
            success = @text.approve
          end
          if success
            flash[:notice] = 'Text was successfully created.'
            format.html { redirect_to(text_url(@text.id)) }
            format.xml  { render :xml => @text, :status => :created }
          else
            format.html { render :action => "new" }
            format.xml  { render :xml => @text.errors, :status => :unprocessable_entity }
          end
        end
      end

      def update
        new_text = params[:phrase][:text]
        word = TEXT.not_approved(:locale => Locale.default, :code => params[:id]).first
        if word
          phrase = word_to_phrase(word, :text)
          word_approved = Ixtlan::Models::Word.latest_approved(:locale => Locale.default, :code => params[:id]).first
          merge_word_into_phrase(word_approved, phrase) if word_approved
        else
          # take the latest approved text for the given code and create a new
          # text without version (!= not_approved text) for the given code
          word = TEXT.latest_approved(:locale => Locale.default, :code => params[:id]).first
          phrase = word_to_phrase(word, :current_text)
          word = TEXT.new(:code => params[:id], :locale => Locale.default)
        end

        phrase.text = new_text

        word.text = new_text
        word.current_user = current_user
        if word.save
          render :xml => phrase, :status => :ok
        else
          render :xml => word.errors, :status => :unprocessable_entity
        end

      end

      def approve
        word = TEXT.not_approved(:locale => Locale.default, :code => params[:id]).first
        if word
          word.current_user = current_user
          if word.approve
            word = nil
          else
            render :xml => word.errors, :status => :unprocessable_entity
          end
        end

        # if there was no unapproved word or approval succeeded render
        # the latest approved word
        unless word
          word = Ixtlan::Models::Word.latest_approved(:locale => Locale.default, :code => params[:id]).first
          phrase = word_to_phrase(word, :current_text)
          render :xml => phrase.to_xml, :status => :ok
        end
      end

      private

      def merge_word_into_phrase(word, phrase)
        phrase.current_text = word.text
        #    phrase.approved_by = word.updated_by
        #    phrase.approved_at = word.updated_at
      end

      def word_to_phrase(word, text_field)
        Ixtlan::Models::Phrase.new(:code => word.code, text_field => word.text, :updated_by => word.updated_by, :updated_at => word.updated_at, :locale => LOCALE.default)
      end
    end
  end
end
