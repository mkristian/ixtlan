class Views::Layouts::Page < Erector::Widget

  def title_text
    "users - #{self.class.name}"
  end
  
  def render
    instruct
    html :xmlns => "http://www.w3.org/1999/xhtml" do
      head do
        title do
          title_text
        end
        css "/widgets.css"
        css "/header.css"
        css "/client.css"
        # to avoid trying to use a stale form just force a reload 
        # when the session got a timeout
        meta :"http-equiv" => "refresh", :content => "#{Configuration.instance.session_idle_timeout * 60}"
      end
      body do
        div :id => 'header' do
          render_header
        end
        div :id => 'body' do
          render_body
        end
        div :id => 'footer' do
          render_footer
        end
      end
    end
  end

  def render_header
    div :class => :application do
      form_tag dispatches_path do
        rawtext helpers.hidden_field_tag(:application, :users)
        submit_tag :users
      end
    end
    if allowed(:course_types, :index)
      div :class => :nav do
        # TODO wrong plural in inflection code from extlib : typese from type
        a "course types", :href => course_types_path.to_s
      end
    end
    if allowed(:locations, :index)
      div :class => :nav do
        a "locations", :href => locations_path.to_s
      end
    end
    if allowed(:configurations, :edit)
      div :class => :nav do
        a "config", :href => configuration_path.to_s
      end
    end
    div :class => :nav do
      form_tag dispatches_path do
        rawtext helpers.hidden_field_tag(:application, :profile)
        submit_tag :profile
      end
    end

    div :class => :nav do
      form_tag helpers.request.path, :method => :delete do
        submit_tag :logout
      end
    end
  end

  def render_body 
    text "This page intentionally left blank."
  end

  def render_footer
    text "Copyright (c) 2009"
  end

  private 
  
  def t(text)
    I18n.translate(text)
  end
end

require 'erector_widgets/entity_widget'
class ErectorWidgets::EntityWidget

  def options_for_select(*args)
    rawtext helpers.options_for_select(*args)
  end

end
