module Ixtlan
  module Controllers
    module TextsController

      def self.included(base)
        base.cache_headers :protected
      end

      private

      LOCALE = Object.full_const_get(::Ixtlan::Models::LOCALE)
      TEXT = Object.full_const_get(::Ixtlan::Models::TEXT)

      def locale_guard
        # TODO
      end

      public

      def index
        version = params[:version]

        @texts = TEXT.all(:locale => LOCALE.get!(params[:locale]))
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
        phrase = params[:phrase]
        phrase[:text] ||= phrase.delete(:current_text)

        respond_to do |format|
          if @text.update(phrase)
            flash[:notice] = phrase[:text].nil? ? 'Text was successfully approved.' : 'Text was successfully updated.'
            format.html { redirect_to(text_url(@text.id)) }
            format.xml  { render :xml => @text, :status => :ok }
          else
            format.html { render :action => "edit" }
            format.xml  { render :xml => @text.errors, :status => :unprocessable_entity }
          end
        end
      end
    end
  end
end
