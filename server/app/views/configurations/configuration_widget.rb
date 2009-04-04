class Views::Configurations::ConfigurationWidget < Views::Configurations::BaseConfigurationWidget 
  def render_form_fields(form, disabled)
    div :class => :second do
      b "Password length"
      br
      rawtext (form.text_field(:password_length, :disabled => disabled))
    end
    
    div :class => :first do
      b "Session idle timeout"
      br
      rawtext (form.text_field(:session_idle_timeout, :disabled => disabled))
    end
  end
end
