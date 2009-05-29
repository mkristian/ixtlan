class Views::Configurations::ConfigurationWidget < Views::Configurations::BaseConfigurationWidget 
  def render_form_fields(form, disabled)
    
    div :class => [:first, error_class(@configuration, :password_length)] do
      b "Password length"
      br
      rawtext (form.text_field(:password_length, :disabled => disabled))
    end
   
  end
end
