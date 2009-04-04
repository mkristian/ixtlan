class Views::Users::Show < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @user_widget = UserWidget.new(view, assigns, stream)
    @user_widget.disabled = true
  end

  def title_text
    @user_widget.title
  end

  def render_body   
    @user_widget.render_to(self)
  end
end
