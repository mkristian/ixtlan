class Views::ResetPasswords::New < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @reset_password_widget = ResetPasswordWidget.new(view, assigns, stream)
  end

  def title_text
    @reset_password_widget.title
  end

  def render_body
    @reset_password_widget.render_to(self)
  end
end
