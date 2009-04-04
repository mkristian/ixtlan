class Views::Users::Edit < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @user_widget = UserWidget.new(view, assigns, stream)
  end

  def title_text
    @user_widget.title
  end

  def render_body    
    @user_widget.render_to(self)
  end
end
