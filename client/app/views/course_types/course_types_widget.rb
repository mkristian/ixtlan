class Views::CourseTypes::CourseTypesWidget < ErectorWidgets::EntitiesWidget

  def initialize(view, assigns, io, *args)
    super(view, assigns, io, *args)
    @table_widget = CourseTypesSortableTableWidget.new(view, assigns, io, :course_types, @__entities, [:course_description,:old_or_special,:at_course_type,:default_accept_file,:increment_courses,:course_stat_level,:confirm_required_flag,:confirm_days_out_start,:confirm_days_out_end,:confirmation_reply,:reminder_flag,:reminder_days_out,:bump_flag,:bump_days_out,:bump_notification,], @field, @direction)
  end

  def entities_symbol
    :course_types
  end

  def title
    t("course_types.list")
  end
  
  def render_navigation
    if allowed(:course_types, :new)
      div :class => :nav_buttons do
        button_to t('widget.new'), new_course_type_path, :method => :get
      end
    end
  end

  def render_table
    @table_widget.render_to(self)
  end
end

class CourseTypesSortableTableWidget < ErectorWidgets::SortableTableWidget

  def link_args(course_type)
    args = {}
    if allowed(:course_types, :edit)
      args[:href] = edit_course_type_path(course_type.id)
    elsif allowed(:course_types, :show)
      args[:href] = course_type_path(course_type.id)
    end
    args
  end
  
  def delete_form(course_type)
    form_for(:course_type, 
             :url => course_type_path(course_type.id),
             :html => { :method => :delete , #:confirm => 'Are you sure?'
             }) do |f|
      rawtext(f.submit(t('widget.delete')))
    end
  end

end
