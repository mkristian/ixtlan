class Views::CourseTypeLocations::Edit < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @course_type_location_widget = CourseTypeLocationWidget.new(view, assigns, stream)
  end

  def title_text
    @course_type_location_widget.title
  end

  def render_body    
    @course_type_location_widget.render_to(self)
  end
end
