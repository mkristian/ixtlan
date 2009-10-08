class Views::Groups::Edit < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @group_widget = GroupWidget.new(view, assigns, stream)
  end

  def title_text
    @group_widget.title
  end

  def render_body    
    @group_widget.render_to(self)
  end
end
