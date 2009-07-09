class LocaleController < ApplicationController
  # GET /locales
  # GET /locales.xml
  def index
    @locales = Locale.all()

    respond_to do |format|
      format.html
      format.xml  { render :xml => @locales }
    end
  end

  # GET /locales/1
  # GET /locales/1.xml
  def show
    @locale = Locale.get!(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @locale }
    end
  end

  # GET /locales/new
  # GET /locales/new.xml
  def new
    @locale = Locale.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @locale }
    end
  end

  # GET /locales/1/edit
  def edit
    @locale = Locale.get!(params[:id])
  end

  # POST /locales
  # POST /locales.xml
  def create
    @locale = Locale.new(params[:locale])

    respond_to do |format|
      if @locale.save
        flash[:notice] = 'Locale was successfully created.'
        puts "asd"
        format.html { redirect_to(locale_url(@locale.id)) }
        puts "das"
        format.xml  { render :xml => @locale, :status => :created }#, :location => @locale }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @locale.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /locales/1
  # PUT /locales/1.xml
  def update
    @locale = Locale.get!(params[:id])

    respond_to do |format|
      if @locale.update_attributes(params[:locale]) or not @locale.dirty?
        flash[:notice] = 'Locale was successfully updated.'
        format.html { redirect_to(locale_url(@locale.id)) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @locale.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /locales/1
  # DELETE /locales/1.xml
  def destroy
    @locale = Locale.get(params[:id])
    @locale.destroy if @locale

    respond_to do |format|
      flash[:notice] = 'Locale was successfully deleted.'
      format.html { redirect_to(locales_url) }
      format.xml  { head :ok }
    end
  end
end
