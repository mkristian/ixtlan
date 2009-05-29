class Views::Locations::LocationWidget < ErectorWidgets::EntityWidget

  def entity_symbol 
    :location
  end
 
  def title
    if @location.new_record?
      t('locations.new_location')
    else
      t('locations.location') + " #{@location.name}"
    end
  end

  def render_navigation(disabled)
    super
    if disabled and not @location.new_record?
      if allowed(:locations, :new)
        div :class => :nav_buttons do
          button_to t('widget.new'), new_location_path, :method => :get, :class => :button
        end
      end
      if allowed(:locations, :edit)
        div :class => :nav_buttons do
          button_to t('widget.edit'), edit_location_path(@location.id), :method => :get, :class => :button
        end
      end
    end
  end

  def render_entity(disabled)
    args = 
      if @location.new_record?
        {:url => locations_path.to_s, :html => {:method => :post}}
      elsif disabled
        {:url => edit_location_path(@location.id).to_s, :html => {:method => :get} }
      else
        {:url => location_path(@location.id).to_s, :html => {:method => :put} }
      end
    
    form_for(:location, args) do |f|
      div :class => :scrollable do
        div :class => [:second, error_class(@location, :name)] do
          b t('locations.name')
          br
          rawtext (f.text_field(:name, :disabled => disabled))
        end

        div :class => [:first, error_class(@location, :signature)] do
          b t('locations.signature')
          br
          rawtext (f.text_area(:signature, :disabled => disabled))
        end

        div :class => [:second, error_class(@location, :next_add_student_id)] do
          b t('locations.next_add_student_id')
          br
          rawtext (f.text_field(:next_add_student_id, :disabled => disabled))
        end

        div :class => [:first, error_class(@location, :default_state)] do
          b t('locations.default_state')
          br
          rawtext (f.text_field(:default_state, :disabled => disabled))
        end

        div :class => [:second, error_class(@location, :default_country)] do
          b t('locations.default_country')
          br
          rawtext (f.text_field(:default_country, :disabled => disabled))
        end

        div :class => [:first, error_class(@location, :default_native_language)] do
          b t('locations.default_native_language')
          br
          rawtext (f.text_field(:default_native_language, :disabled => disabled))
        end

        div :class => [:second, error_class(@location, :default_news_language)] do
          b t('locations.default_news_language')
          br
          rawtext (f.text_field(:default_news_language, :disabled => disabled))
        end

        div :class => [:first, error_class(@location, :default_app_origin)] do
          b t('locations.default_app_origin')
          br
          rawtext (f.text_field(:default_app_origin, :disabled => disabled))
        end

        div :class => [:second, error_class(@location, :accept_confirm_weeks)] do
          b t('locations.accept_confirm_weeks')
          br
          rawtext (f.text_field(:accept_confirm_weeks, :disabled => disabled))
        end

        div :class => [:first, error_class(@location, :course_loc_id)] do
          b t('locations.course_loc_id')
          br
          rawtext (f.text_field(:course_loc_id, :disabled => disabled))
        end

        div :class => [:second, error_class(@location, :rides_rpt_name)] do
          b t('locations.rides_rpt_name')
          br
          rawtext (f.text_field(:rides_rpt_name, :disabled => disabled))
        end

        div :class => [:first, error_class(@location, :ignore_accents)] do
          b t('locations.ignore_accents')
          br
          rawtext (f.check_box(:ignore_accents, :disabled => disabled))
        end

        div :class => [:second, error_class(@location, :registrar_email_address)] do
          b t('locations.registrar_email_address')
          br
          rawtext (f.text_field(:registrar_email_address, :disabled => disabled))
        end

        div :class => [:first, error_class(@location, :from_name)] do
          b t('locations.from_name')
          br
          rawtext (f.text_field(:from_name, :disabled => disabled))
        end

        div :class => [:second, error_class(@location, :email_signature)] do
          b t('locations.email_signature')
          br
          rawtext (f.text_area(:email_signature, :disabled => disabled))
        end

        div :class => [:first, error_class(@location, :bounce_to_email_address)] do
          b t('locations.bounce_to_email_address')
          br
          rawtext (f.text_field(:bounce_to_email_address, :disabled => disabled))
        end

        div :class => [:second, error_class(@location, :enable_confirm_module)] do
          b t('locations.enable_confirm_module')
          br
          rawtext (f.check_box(:enable_confirm_module, :disabled => disabled))
        end

      end
      unless disabled
        div :class => :action_button do
          if @location.new_record?
            rawtext(f.submit(t('widget.create')))
          else
            rawtext(f.submit(t('widget.update')))
          end
        end
      end
    end
  end
end
