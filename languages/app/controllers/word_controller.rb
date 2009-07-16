class WordController < ApplicationController
  # GET /words
  # GET /words.xml
  def index
    @words = Word.all()

    respond_to do |format|
      format.html
      format.xml  { render :xml => @words }
    end
  end

  # GET /words/1
  # GET /words/1.xml
  def show
    @word = Word.get!(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @word }
    end
  end

  # GET /words/new
  # GET /words/new.xml
  def new
    @word = Word.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @word }
    end
  end

  # GET /words/1/edit
  def edit
    @word = Word.get!(params[:id])
  end

  # POST /words
  # POST /words.xml
  def create
    @word = Word.new(params[:word])

    respond_to do |format|
      if @word.save
        flash[:notice] = 'Word was successfully created.'
        format.html { redirect_to(word_url(@word.id)) }
        format.xml  { render :xml => @word, :status => :created }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @word.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /words/1
  # PUT /words/1.xml
  def update
    @word = Word.get!(params[:id])

    respond_to do |format|
      if @word.update_attributes(params[:word]) or not @word.dirty?
        flash[:notice] = 'Word was successfully updated.'
        format.html { redirect_to(word_url(@word.id)) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @word.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /words/1
  # DELETE /words/1.xml
  def destroy
    @word = Word.get(params[:id])
    @word.destroy if @word

    respond_to do |format|
      flash[:notice] = 'Word was successfully deleted.'
      format.html { redirect_to(words_url) }
      format.xml  { head :ok }
    end
  end
end
