class Views::CourseTypeLocations::Edit < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @course_type_location_widget = CourseTypeLocationWidget.new(view, assigns, stream)
  end

  def title_text
    text t('course_type_location.edit_course_type_location') 
    text " > "
    text t('course_type_location.location')
    text ": "
    text @location.name
  end

  def render_body    
    @course_type_location_widget.render_to(self)
  end
end
