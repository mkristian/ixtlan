class Views::Configurations::Edit < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @configuration_widget = ConfigurationWidget.new(view, assigns, stream)
  end

  def title_text
    @configuration_widget.title
  end

  def render_body    
    @configuration_widget.render_to(self)
  end
end
