module Ixtlan
  module Controllers
    module TextsController

      private
      
      def locale_guard

      end

      public

      def index
        version = params[:version]

        @texts = Text.all(:locale => Ixtlan::Locale.get!(params[:locale]))
      end

      def create
        phrase = params[:phrase]

        locale = Ixtlan::Locale.get!(phrase.delete(:locale))

        if(Text.count(:version => nil, :code => phrase[:code], :locale => locale) == 1)
          raise "TODO precondition failed"
        end

        phrase[:text] ||= phrase.delete(:current_text)
        phrase[:locale] = locale

        @text = Text.new(phrase)

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
