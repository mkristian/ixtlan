class Views::CourseTypes::Index < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @course_types_widget = CourseTypesWidget.new(view, assigns, stream)
  end

  def title_text
    @course_types_widget.title
  end

  def render_body
    @course_types_widget.render_to(self)
  end
end
