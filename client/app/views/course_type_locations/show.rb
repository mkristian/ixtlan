class Views::CourseTypeLocations::Show < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @course_type_location_widget = ::Views::CourseTypeLocations::CourseTypeLocationWidget.new(view, assigns, stream)
    @course_type_location_widget.disabled = true
  end

  def title_text
    text t('course_type_location.show_course_type_location') 
    text " > "
    text t('course_type_location.location')
    text ": "
    text @location.name
  end

  def render_body   
    @course_type_location_widget.render_to(self)
  end
end
