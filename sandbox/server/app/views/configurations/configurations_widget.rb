class Views::Configurations::ConfigurationsWidget < ErectorWidgets::EntitiesWidget

  def entities_symbol
    :configurations
  end

  def title
    "configurationlist"
  end
  
  def render_navigation
      div :class => :nav_buttons do
        button_to 'new', new_configuration_path, :method => :get
      end
  end

  def render_table_header
    th "Session idle timeout"
    th "Password length"
    th "Keep audit logs"
  end

  def render_table_row(configuration)
    td do
      a configuration.session_idle_timeout, :href => edit_configuration_path(configuration.id)

    end
    td configuration.password_length
    td configuration.keep_audit_logs

    td :class => :cell_buttons do
        form_for(:configuration, 
                 :url => configuration_path(configuration.id),
                 :html => { :method => :delete ,
                 :confirm => 'Are you sure?'}) do |f|
          rawtext(f.submit("delete"))
        end
    end
  end
end
