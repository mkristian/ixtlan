module Ixtlan
  module Controllers
    module LocalesController

      def self.included(base)
        base.send(:include, Ixtlan::Controllers::SearchQuery)
        base.skip_before_filter :authenticate, :guard, :only => [:index,:show]
        base.cache_headers :public, true # no_store == true
      end

      private

      LOCALE = Object.full_const_get(::Ixtlan::Models::LOCALE)

      public

      # GET /locales
      # GET /locales.xml
      def index
        @locales = query(LOCALE, :code)

        respond_to do |format|
          format.html
          format.xml  { render :xml => @locales }
        end
      end

      # GET /locales/1
      # GET /locales/1.xml
      def show
        @locale = LOCALE.first_or_get!(params[:id])

        respond_to do |format|
          format.html # show.html.erb
          format.xml  { render :xml => @locale }
        end
      end

      # GET /locales/new
      # GET /locales/new.xml
      def new
        @locale = LOCALE.new

        respond_to do |format|
          format.html # new.html.erb
          format.xml  { render :xml => @locale }
        end
      end

      # GET /locales/1/edit
      def edit
        @locale = LOCALE.first_or_get!(params[:id])
      end

      # POST /locales
      # POST /locales.xml
      def create
        @locale = LOCALE.new(params[:locale])
        @locale.current_user = current_user

        respond_to do |format|
          if @locale.save
            flash[:notice] = 'Locale was successfully created.'
            format.html { redirect_to(locale_url(@locale.id)) }
            format.xml  { render :xml => @locale, :status => :created, :location => locale_url(@locale.id) + ".xml" }
          else
            format.html { render :action => "new" }
            format.xml  { render :xml => @locale.errors, :status => :unprocessable_entity }
          end
        end
      end

      # PUT /locales/1
      # PUT /locales/1.xml
      def update
        @locale = LOCALE.first_or_get!(params[:id])
        @locale.current_user = current_user

        respond_to do |format|
          if @locale.update(params[:locale]) or not @locale.dirty?
            flash[:notice] = 'Locale was successfully updated.'
            format.html { redirect_to(locale_url(@locale.id)) }
            format.xml  { render :xml => @locale }
          else
            format.html { render :action => "edit" }
            format.xml  { render :xml => @locale.errors, :status => :unprocessable_entity }
          end
        end
      end

      # DELETE /locales/1
      # DELETE /locales/1.xml
      def destroy
        @locale = LOCALE.first_or_get(params[:id])
        @locale.destroy if @locale

        respond_to do |format|
          flash[:notice] = 'Locale was successfully deleted.'
          format.html { redirect_to(locales_url) }
          format.xml  { head :ok }
        end
      end
    end
  end
end
