class Views::Configurations::Show < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @configuration_widget = Views::Configurations::ConfigurationWidget.new(view, assigns, stream)
#    @configuration_widget.disabled = true
  end

  def title_text
    @configuration_widget.title
  end

  def render_body   
    @configuration_widget.render_to(self)
  end
end
