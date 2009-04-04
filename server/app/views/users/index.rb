class Views::Users::Index < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @users_widget = UsersWidget.new(view, assigns, stream)
  end

  def title_text
    @users_widget.title
  end

  def render_body
    @users_widget.render_to(self)
  end
end
