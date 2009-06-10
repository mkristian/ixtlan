class Views::CourseTypes::Show < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @course_type_widget = ::Views::CourseTypes::CourseTypeWidget.new(view, assigns, stream)
    @course_type_widget.disabled = true
  end

  def title_text
    @course_type_widget.title
  end

  def render_body   
    @course_type_widget.render_to(self)
  end
end
