class Views::Groups::Index < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @groups_widget = GroupsWidget.new(view, assigns, stream)
  end

  def title_text
    @groups_widget.title
  end

  def render_body
    @groups_widget.render_to(self)
  end
end
