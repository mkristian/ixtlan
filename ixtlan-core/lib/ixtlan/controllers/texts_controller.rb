module Ixtlan
  module Controllers
    module TextsController

      LOCALE = Object.full_const_get(::Ixtlan::Models::LOCALE)
      TEXT = Object.full_const_get(::Ixtlan::Models::TEXT)
      
      private
      
      def locale_guard

      end

      public

      def index
        version = params[:version]

        @texts = TEXT.all(:locale => LOCALE.get!(params[:locale]))
      end

      def create
        phrase = params[:phrase]

        locale = LOCALE.get!(phrase.delete(:locale))

        if(TEXT.count(:version => nil, :code => phrase[:code], :locale => locale) == 1)
          raise "TODO precondition failed"
        end

        phrase[:text] ||= phrase.delete(:current_text)
        phrase[:locale] = locale

        @text = TEXT.new(phrase)

        respond_to do |format|
          if @text.save
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
