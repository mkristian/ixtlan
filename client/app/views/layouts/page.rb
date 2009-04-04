class Views::Layouts::Page < Erector::Widget

  def title_text
    "users - #{self.class.name}"
  end
  
  def render
    instruct
    html :xmlns => "http://www.w3.org/1999/xhtml" do
      head do
        title title_text
        css "/widgets.css"
        css "/header.css"
        css "/client.css"
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
      rawtext form_tag dispatches_path
      rawtext helpers.hidden_field_tag(:application, :users)
      submit_tag :users
      rawtext "</form>"
    end
    if allowed(:configurations, :edit)
      div :class => :nav do
        a "config", :href => "/configuration"
      end
    end
    div :class => :nav do
      rawtext form_tag dispatches_path
      rawtext helpers.hidden_field_tag(:application, :profile)
      submit_tag :profile
      rawtext "</form>"
    end

    div :class => :nav do
      rawtext form_tag helpers.request.path, :method => :delete
      submit_tag :logout
      rawtext "</form>"
    end
  end

  def render_body 
    text "This page intentionally left blank."
  end

  def render_footer
    text "Copyright (c) 2009"
  end
end

require 'erector_widgets/entity_widget'
class ErectorWidgets::EntityWidget

  def options_for_select(*args)
    rawtext helpers.options_for_select(*args)
  end

end
