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
    
    form_for(:configuration, args) do |f|
      render_form_fields(f, disabled)

      unless disabled
        div :class => :action_button do
          rawtext(f.submit("Update"))
        end
      end
    end
  end
end
