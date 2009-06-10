class Views::Locations::Index < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @locations_widget = ::Views::Locations::LocationsWidget.new(view, assigns, stream)
  end

  def title_text
    @locations_widget.title
  end

  def render_body
    @locations_widget.render_to(self)
  end
end
