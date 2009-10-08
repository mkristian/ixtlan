class Views::ResetPasswords::Index < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @reset_passwords_widget = ResetPasswordsWidget.new(view, assigns, stream)
  end

  def title_text
    @reset_passwords_widget.title
  end

  def render_body
    @reset_passwords_widget.render_to(self)
  end
end
