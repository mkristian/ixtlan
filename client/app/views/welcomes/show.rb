class Views::Welcomes::Show < Views::Layouts::Page

  def title_text
    "welcome"
  end

  def render_body
    fieldset do
      h3 "welcome"
    end
  end
end
