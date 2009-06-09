class CourseTypeLocationsController < ApplicationController

  before_filter :location

  def location 
    @location = Location.get!(params[:location_id])
  end

  # GET /course_type_locations
  # GET /course_type_locations.xml
  def index
    @course_type_locations = CourseTypeLocation.all(@find_all_args)

    respond_to do |format|
      format.html # index.html.erb
      format.xml  { render :xml => @course_type_locations }
    end
  end

  # GET /course_type_locations/1
  # GET /course_type_locations/1.xml
  def show
    raise "not found" unless @course_type_location = CourseTypeLocation.first(:location_id => @location.id, :course_type_id => params[:id])
    @course_types = CourseType.all

    respond_to do |format|
      format.html # show.html.erb
      format.xml  { render :xml => @course_type_location }
    end
  end

  # GET /course_type_locations/new
  # GET /course_type_locations/new.xml
  def new
    @course_type_location = CourseTypeLocation.new
    @course_types = CourseType.all

    respond_to do |format|
      format.html # new.html.erb
      format.xml  { render :xml => @course_type_location }
    end
  end

  # GET /course_type_locations/1/edit
  def edit
    @course_type_location = CourseTypeLocation.first(:location_id => @location.id, :course_type_id => params[:id])
    @course_types = CourseType.all
  end

  # POST /course_type_locations
  # POST /course_type_locations.xml
  def create
    @course_type_location = CourseTypeLocation.new(params[:course_type_location])
    @course_type_location.location_id = @location.id

    respond_to do |format|
      if @course_type_location.save
        flash[:notice] = t('course_type_locations.course_type_location_created')
        format.html { redirect_to(location_course_type_location_url(@location.id, @course_type_location.id)) }
        format.xml  { render :xml => @course_type_location, :status => :created, :location => @course_type_location }
      else
        @course_types = CourseType.all
        format.html { render :action => "new" }
        format.xml  { render :xml => @course_type_location.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /course_type_locations/1
  # PUT /course_type_locations/1.xml
  def update
    @course_type_location = CourseTypeLocation.first(:location_id => @location.id, :course_type_id => params[:id])

    respond_to do |format|
      if @course_type_location.update_attributes(params[:course_type_location]) or not @course_type_location.dirty?
        flash[:notice] = t('course_type_locations.course_type_location_updated')
        format.html { redirect_to(location_course_type_location_url(@location.id, @course_type_location.id)) }
        format.xml  { head :ok }
      else
        @course_types = CourseType.all
        format.html { render :action => "edit" }
        format.xml  { render :xml => @course_type_location.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /course_type_locations/1
  # DELETE /course_type_locations/1.xml
  def destroy
    @course_type_location = CourseTypeLocation.first(:location_id => @location.id, :course_type_id => params[:id])
    @course_type_location.destroy if @course_type_location

    respond_to do |format|
      flash[:notice] = t('course_type_locations.course_type_location_deleted')
      format.html { redirect_to(location_course_type_locations_url(@location.id)) }
      format.xml  { head :ok }
    end
  end

  private

  def audit
    if @course_type_location
      @course_type_location.to_s
    elsif @course_type_locations
      "CourseTypeLocations[#{@course_type_locations.size};#{@field}:#{@direction}]"
    else
      ""
    end
  end
end
