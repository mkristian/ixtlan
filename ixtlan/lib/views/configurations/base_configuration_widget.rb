class Views::Configurations::BaseConfigurationWidget < ErectorWidgets::EntityWidget

  def entity_symbol 
    :configuration
  end
 
  def title
    "configuration"
  end

  def render_entity(disabled)
    args = 
      if disabled
        {:url => edit_configuration_path.to_s, :html => {:method => :get} }
      else
        {:url => configuration_path.to_s, :html => {:method => :put} }
      end
    
    form_for(:configuration, args) do |form|
      div :class => [:first, error_class(@configuration, :password_length)] do
        b "Session idle timeout"
        br
        rawtext (form.text_field(:session_idle_timeout, :disabled => disabled))
      end
      
      div :class => [:second, error_class(@configuration, :keep_audit_logs)] do
        b "Keep audit logs (weeks)"
        br
        rawtext (form.text_field(:keep_audit_logs, :disabled => disabled))
      end

      render_form_fields(form, disabled)

      unless disabled
        div :class => :action_button do
          rawtext(form.submit("Update"))
        end
      end
    end
  end
end
