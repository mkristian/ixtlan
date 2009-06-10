class Views::Locations::New < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @location_widget = ::Views::Locations::LocationWidget.new(view, assigns, stream)
  end

  def title_text
    @location_widget.title
  end

  def render_body
    @location_widget.render_to(self)
  end
end
