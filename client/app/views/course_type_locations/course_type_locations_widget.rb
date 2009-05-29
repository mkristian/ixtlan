class Views::CourseTypeLocations::CourseTypeLocationsWidget < ErectorWidgets::EntitiesWidget

  def entities_symbol
    :course_type_locations
  end

  def title
    t("course_type_locations.list")
  end
  
  def render_navigation
    if allowed(:course_type_locations, :new)
      div :class => :nav_buttons do
        button_to t('widget.new'), new_course_type_location_path, :method => :get
      end
    end
  end

  def render_table_header
    th t('course_type_locations.location_id')
    th t('course_type_locations.course_type_id')
    th t('course_type_locations.confirm_required_flag')
    th t('course_type_locations.confirm_days_out_start')
    th t('course_type_locations.confirm_days_out_end')
    th t('course_type_locations.confirmation_reply')
    th t('course_type_locations.reminder_flag')
    th t('course_type_locations.reminder_days_out')
    th t('course_type_locations.bump_flag')
    th t('course_type_locations.bump_days_out')
    th t('course_type_locations.bump_notification')
  end

  def render_table_row(course_type_location)
    td do
      args = {}
      if allowed(:course_type_locations, :edit)
        args[:href] = edit_course_type_location_path(course_type_location.id)
      elsif allowed(:course_type_locations, :show)
        args[:href] = course_type_location_path(course_type_location.id)
      end
      a course_type_location.location_id, args
    end
    td course_type_location.course_type_id
    td course_type_location.confirm_required_flag
    td course_type_location.confirm_days_out_start
    td course_type_location.confirm_days_out_end
    td course_type_location.confirmation_reply
    td course_type_location.reminder_flag
    td course_type_location.reminder_days_out
    td course_type_location.bump_flag
    td course_type_location.bump_days_out
    td course_type_location.bump_notification

    td :class => :cell_buttons do
      if allowed(:course_type_locations, :destroy)
        form_for(:course_type_location, 
                 :url => course_type_location_path(course_type_location.id),
                 :html => { :method => :delete , #:confirm => 'Are you sure?'
                          }) do |f|
          rawtext(f.submit(t('widget.delete')))
        end
      end
    end
  end
end
