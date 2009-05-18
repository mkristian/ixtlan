class Views::Locations::Edit < Views::Layouts::Page

  def initialize(view, assigns, stream)
    super(view, assigns, stream)
    @location_widget = LocationWidget.new(view, assigns, stream)
  end

  def title_text
    @location_widget.title
  end

  def render_body    
    @location_widget.render_to(self)
  end
end
