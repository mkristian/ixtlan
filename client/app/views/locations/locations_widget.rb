class Views::Locations::LocationsWidget < ErectorWidgets::EntitiesWidget

  def entities_symbol
    :locations
  end

  def title
    t("locations.list")
  end
  
  def render_navigation
    if allowed(:locations, :new)
      div :class => :nav_buttons do
        button_to t('widget.new'), new_location_path, :method => :get
      end
    end
  end

  def render_table_header
    th t('locations.name')
    th t('locations.next_add_student_id')
    th t('locations.default_state')
    th t('locations.default_country')
    th t('locations.default_native_language')
    th t('locations.default_news_language')
    th t('locations.default_app_origin')
    th t('locations.accept_confirm_weeks')
    th t('locations.course_loc_id')
    th t('locations.rides_rpt_name')
    th t('locations.ignore_accents')
    th t('locations.registrar_email_address')
    th t('locations.from_name')
    th t('locations.bounce_to_email_address')
    th t('locations.enable_confirm_module')
  end

  def render_table_row(location)
    td do
      args = {}
      if allowed(:locations, :edit)
        args[:href] = edit_location_path(location.id)
      elsif allowed(:locations, :show)
        args[:href] = location_path(location.id)
      end
      a location.name, args
    end
        td location.next_add_student_id
    td location.default_state
    td location.default_country
    td location.default_native_language
    td location.default_news_language
    td location.default_app_origin
    td location.accept_confirm_weeks
    td location.course_loc_id
    td location.rides_rpt_name
    td location.ignore_accents
    td location.registrar_email_address
    td location.from_name
        td location.bounce_to_email_address
    td location.enable_confirm_module

    td :class => :cell_buttons do
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
end
