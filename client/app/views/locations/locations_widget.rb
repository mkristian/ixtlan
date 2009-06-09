class Views::Locations::LocationsWidget < ErectorWidgets::EntitiesWidget

  def initialize(view, assigns, io, *args)
    super(view, assigns, io, *args)
    @table_widget = LocationsSortableTableWidget.new(view, assigns, io, :locations, @__entities, [:name,:next_add_student_id,:default_state,:default_country,:default_native_language,:default_news_language,:default_app_origin,:accept_confirm_weeks,:course_loc_id,:rides_rpt_name,:ignore_accents,:registrar_email_address,:from_name,:bounce_to_email_address,:enable_confirm_module,], @field, @direction)
  end

  def entities_symbol
    :locations
  end

  def title
    text t("locations.list")
  end
  
  def render_navigation
    if allowed(:locations, :new)
      div :class => :nav_buttons do
        button_to t('widget.new'), new_location_path, :method => :get
      end
    end
  end

  def render_table
    @table_widget.render_to(self)
  end
end

class LocationsSortableTableWidget < ErectorWidgets::SortableTableWidget

  def link_args(location)
    args = {}
    if allowed(:locations, :edit)
      args[:href] = edit_location_path(location.id)
    elsif allowed(:locations, :show)
      args[:href] = location_path(location.id)
    end
    args
  end
  
  def delete_form(location)
    if allowed(:locations, :destroy)
      form_for(:location, 
               :url => location_path(location.id),
               :html => { :method => :delete , #:confirm => 'Are you sure?'
               }) do |f|
        rawtext(f.submit(t('widget.delete')))
      end
    end
  end

end
