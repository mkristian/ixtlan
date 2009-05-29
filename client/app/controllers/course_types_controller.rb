class CourseTypesController < ApplicationController
  # GET /course_types
  # GET /course_types.xml
  def index
    @field = params[:field].to_sym if params[:field]
    @direction = params[:direction] == "up" ? :up : :down
    args = {:order => [@direction == :down ? @field.asc : @field.desc]}
    args.clear unless args[:order][0]

    @course_types = CourseType.all(args)

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @course_types }
    end
  end

  # GET /course_types/1
  # GET /course_types/1.xml
  def show
    @course_type = CourseType.get!(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @course_type }
    end
  end

  # GET /course_types/new
  # GET /course_types/new.xml
  def new
    @course_type = CourseType.new

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @course_type }
    end
  end

  # GET /course_types/1/edit
  def edit
    @course_type = CourseType.get!(params[:id])
  end

  # POST /course_types
  # POST /course_types.xml
  def create
    @course_type = CourseType.new(params[:course_type])

    respond_to do |format|
      if @course_type.save
        flash[:notice] = t('course_types.course_type_created')
        format.html { redirect_to(course_type_url(@course_type.id)) }
        format.xml  { render :xml => @course_type, :status => :created, :location => @course_type }
      else
        format.html { render :action => "new" }
        format.xml  { render :xml => @course_type.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /course_types/1
  # PUT /course_types/1.xml
  def update
    @course_type = CourseType.get!(params[:id])

    respond_to do |format|
      if @course_type.update_attributes(params[:course_type]) or not @course_type.dirty?
        flash[:notice] = t('course_types.course_type_updated')
        format.html { redirect_to(course_type_url(@course_type.id)) }
        format.xml  { head :ok }
      else
        format.html { render :action => "edit" }
        format.xml  { render :xml => @course_type.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /course_types/1
  # DELETE /course_types/1.xml
  def destroy
    @course_type = CourseType.get(params[:id])
    @course_type.destroy if @course_type

    respond_to do |format|
      flash[:notice] = t('course_types.course_type_deleted')
      format.html { redirect_to(course_types_url) }
      format.xml  { head :ok }
    end
  end

  private

  def audit
    if @course_type
      @course_type.to_s
    elsif @course_types
      "CourseTypes[#{@course_types.size};#{@field}:#{@direction}]"
    else
      ""
    end
  end
end
