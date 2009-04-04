class Views::Errors::General < Views::Layouts::Page
  def title_text
    "general error"
  end

  def render_body
    fieldset :class => :entity do
      legend "general error"
      div :class => :message do
        text flash[:notice]
        flash.clear
      end
    end
  end
end
