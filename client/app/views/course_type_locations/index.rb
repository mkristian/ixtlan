class Views::CourseTypeLocations::Index < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @course_type_locations_widget = CourseTypeLocationsWidget.new(view, assigns, stream)
  end

  def title_text
    @course_type_locations_widget.title
  end

  def render_body
    @course_type_locations_widget.render_to(self)
  end
end
