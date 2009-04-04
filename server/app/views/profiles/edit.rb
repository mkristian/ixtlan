class Views::Profiles::Edit < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    assigns[:groups] = @user.groups
    @profile_widget = ProfileWidget.new(view, assigns, stream)
  end

  def title_text
    @profile_widget.title
  end

  def render_body    
    @profile_widget.render_to(self)
  end
end
