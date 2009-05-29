class Views::CourseTypeLocations::CourseTypeLocationWidget < ErectorWidgets::EntityWidget

  def entity_symbol 
    :course_type_location
  end
 
  def title
    if @course_type_location.new_record?
      t('course_type_locations.new_course_type_location')
    else
      t('course_type_locations.course_type_location') + " #{@course_type_location.location_id}"
    end
  end

  def render_navigation(disabled)
    super
    if disabled and not @course_type_location.new_record?
      if allowed(:course_type_locations, :new)
        div :class => :nav_buttons do
          button_to t('widget.new'), new_course_type_location_path, :method => :get, :class => :button
        end
      end
      if allowed(:course_type_locations, :edit)
        div :class => :nav_buttons do
          button_to t('widget.edit'), edit_course_type_location_path(@course_type_location.id), :method => :get, :class => :button
        end
      end
    end
  end

  def render_entity(disabled)
    args = 
      if @course_type_location.new_record?
        {:url => course_type_locations_path.to_s, :html => {:method => :post}}
      elsif disabled
        {:url => edit_course_type_location_path(@course_type_location.id).to_s, :html => {:method => :get} }
      else
        {:url => course_type_location_path(@course_type_location.id).to_s, :html => {:method => :put} }
      end
    
    form_for(:course_type_location, args) do |f|
      div :class => :scrollable do
        div :class => :second do
          b t('course_type_locations.location_id')
          br
          rawtext (f.text_field(:location_id, :disabled => disabled))
        end

        div :class => :first do
          b t('course_type_locations.course_type_id')
          br
          rawtext (f.text_field(:course_type_id, :disabled => disabled))
        end

        div :class => :second do
          b t('course_type_locations.confirm_required_flag')
          br
          rawtext (f.check_box(:confirm_required_flag, :disabled => disabled))
        end

        div :class => :first do
          b t('course_type_locations.confirm_days_out_start')
          br
          rawtext (f.text_field(:confirm_days_out_start, :disabled => disabled))
        end

        div :class => :second do
          b t('course_type_locations.confirm_days_out_end')
          br
          rawtext (f.text_field(:confirm_days_out_end, :disabled => disabled))
        end

        div :class => :first do
          b t('course_type_locations.confirmation_reply')
          br
          rawtext (f.text_field(:confirmation_reply, :disabled => disabled))
        end

        div :class => :second do
          b t('course_type_locations.reminder_flag')
          br
          rawtext (f.check_box(:reminder_flag, :disabled => disabled))
        end

        div :class => :first do
          b t('course_type_locations.reminder_days_out')
          br
          rawtext (f.text_field(:reminder_days_out, :disabled => disabled))
        end

        div :class => :second do
          b t('course_type_locations.bump_flag')
          br
          rawtext (f.check_box(:bump_flag, :disabled => disabled))
        end

        div :class => :first do
          b t('course_type_locations.bump_days_out')
          br
          rawtext (f.text_field(:bump_days_out, :disabled => disabled))
        end

        div :class => :second do
          b t('course_type_locations.bump_notification')
          br
          rawtext (f.check_box(:bump_notification, :disabled => disabled))
        end

      end
      unless disabled
        div :class => :action_button do
          if @course_type_location.new_record?
            rawtext(f.submit(t('widget.create')))
          else
            rawtext(f.submit(t('widget.update')))
          end
        end
      end
    end
  end
end
