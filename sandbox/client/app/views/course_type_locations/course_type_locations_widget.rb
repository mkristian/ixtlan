class Views::CourseTypeLocations::CourseTypeLocationsWidget < ErectorWidgets::EntitiesWidget

  def initialize(view, assigns, io, *args)
    super(view, assigns, io, *args)
    @table_widget = CourseTypeLocationsSortableTableWidget.new(view, assigns, io, :course_type_locations, @__entities, [:location_id,:course_type_id,:confirm_required_flag,:confirm_days_out_start,:confirm_days_out_end,:confirmation_reply,:reminder_flag,:reminder_days_out,:bump_flag,:bump_days_out,:bump_notification,], @field, @direction)
  end

  def entities_symbol
    :course_type_locations
  end

  def title
    text t("course_type_locations.list")
  end
  
  def render_navigation
    if allowed(:course_type_locations, :new)
      div :class => :nav_buttons do
        button_to t('widget.new'), new_course_type_location_path, :method => :get
      end
    end
  end

  def render_table
    @table_widget.render_to(self)
  end
end

class CourseTypeLocationsSortableTableWidget < ErectorWidgets::SortableTableWidget

  def link_args(course_type_location)
    args = {}
    if allowed(:course_type_locations, :edit)
      args[:href] = edit_course_type_location_path(course_type_location.id)
    elsif allowed(:course_type_locations, :show)
      args[:href] = course_type_location_path(course_type_location.id)
    end
    args
  end
  
  def delete_form(course_type_location)
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
