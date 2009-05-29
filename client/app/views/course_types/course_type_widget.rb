class Views::CourseTypes::CourseTypeWidget < ErectorWidgets::EntityWidget

  def entity_symbol 
    :course_type
  end
 
  def title
    if @course_type.new_record?
      t('course_types.new_course_type')
    else
      t('course_types.course_type') + " #{@course_type.course_description}"
    end
  end

  def render_navigation(disabled)
    super
    if disabled and not @course_type.new_record?
      if allowed(:course_types, :new)
        div :class => :nav_buttons do
          button_to t('widget.new'), new_course_type_path, :method => :get, :class => :button
        end
      end
      if allowed(:course_types, :edit)
        div :class => :nav_buttons do
          button_to t('widget.edit'), edit_course_type_path(@course_type.id), :method => :get, :class => :button
        end
      end
    end
  end

  def render_entity(disabled)
    args = 
      if @course_type.new_record?
        {:url => course_types_path.to_s, :html => {:method => :post}}
      elsif disabled
        {:url => edit_course_type_path(@course_type.id).to_s, :html => {:method => :get} }
      else
        {:url => course_type_path(@course_type.id).to_s, :html => {:method => :put} }
      end
    
    form_for(:course_type, args) do |f|
      div :class => :scrollable do
        div :class => [:second, error_class(@course_type, :course_description)] do
          b t('course_types.course_description')
          br
          rawtext (f.text_field(:course_description, :disabled => disabled))
        end

        div :class => [:first, error_class(@course_type, :old_or_special)] do
          b t('course_types.old_or_special')
          br
          rawtext (f.check_box(:old_or_special, :disabled => disabled))
        end

        div :class => [:second, error_class(@course_type, :at_course_type)] do
          b t('course_types.at_course_type')
          br
          rawtext (f.text_field(:at_course_type, :disabled => disabled))
        end

        div :class => [:first, error_class(@course_type, :default_accept_file)] do
          b t('course_types.default_accept_file')
          br
          rawtext (f.text_field(:default_accept_file, :disabled => disabled))
        end

        div :class => [:second, error_class(@course_type, :increment_courses)] do
          b t('course_types.increment_courses')
          br
          rawtext (f.check_box(:increment_courses, :disabled => disabled))
        end

        div :class => [:first, error_class(@course_type, :course_stat_level)] do
          b t('course_types.course_stat_level')
          br
          rawtext (f.text_field(:course_stat_level, :disabled => disabled))
        end

        div :class => [:second, error_class(@course_type, :confirm_required_flag)] do
          b t('course_types.confirm_required_flag')
          br
          rawtext (f.check_box(:confirm_required_flag, :disabled => disabled))
        end

        div :class => [:first, error_class(@course_type, :confirm_days_out_start)] do
          b t('course_types.confirm_days_out_start')
          br
          rawtext (f.text_field(:confirm_days_out_start, :disabled => disabled))
        end

        div :class => [:second, error_class(@course_type, :confirm_days_out_end)] do
          b t('course_types.confirm_days_out_end')
          br
          rawtext (f.text_field(:confirm_days_out_end, :disabled => disabled))
        end

        div :class => [:first, error_class(@course_type, :confirmation_reply)] do
          b t('course_types.confirmation_reply')
          br
          rawtext (f.text_field(:confirmation_reply, :disabled => disabled))
        end

        div :class => [:second, error_class(@course_type, :reminder_flag)] do
          b t('course_types.reminder_flag')
          br
          rawtext (f.check_box(:reminder_flag, :disabled => disabled))
        end

        div :class => [:first, error_class(@course_type, :reminder_days_out)] do
          b t('course_types.reminder_days_out')
          br
          rawtext (f.text_field(:reminder_days_out, :disabled => disabled))
        end

        div :class => [:second, error_class(@course_type, :bump_flag)] do
          b t('course_types.bump_flag')
          br
          rawtext (f.check_box(:bump_flag, :disabled => disabled))
        end

        div :class => [:first, error_class(@course_type, :bump_days_out)] do
          b t('course_types.bump_days_out')
          br
          rawtext (f.text_field(:bump_days_out, :disabled => disabled))
        end

        div :class => [:second, error_class(@course_type, :bump_notification)] do
          b t('course_types.bump_notification')
          br
          rawtext (f.check_box(:bump_notification, :disabled => disabled))
        end

      end
      unless disabled
        div :class => :action_button do
          if @course_type.new_record?
            rawtext(f.submit(t('widget.create')))
          else
            rawtext(f.submit(t('widget.update')))
          end
        end
      end
    end
  end
end
