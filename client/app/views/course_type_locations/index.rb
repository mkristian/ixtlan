class Views::CourseTypeLocations::Index < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @course_type_locations_widget = CourseTypeLocationsWidget.new(view, assigns, stream)
  end

  def title_text
    text t("course_type_locations.list") 
    text " > "
    text t('course_type_location.location')
    text ": "
    text @location.name
  end

  def render_body
    @course_type_locations_widget.render_to(self)
  end
end
