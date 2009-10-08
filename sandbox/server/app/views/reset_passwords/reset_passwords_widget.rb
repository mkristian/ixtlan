class Views::ResetPasswords::ResetPasswordsWidget < ErectorWidgets::EntitiesWidget

  def entities_symbol
    :reset_passwords
  end

  def title
    "reset_passwordlist"
  end
  
  def render_navigation
      div :class => :nav_buttons do
        button_to 'new', new_reset_password_path, :method => :get
      end
  end

  def render_table_header
    th "Token"
    th "Success url"
    th "Expired at"
  end

  def render_table_row(reset_password)
    td do
      a reset_password.token, :href => edit_reset_password_path(reset_password.id)

    end
    td reset_password.success_url
    td reset_password.expired_at

    td :class => :cell_buttons do
        form_for(:reset_password, 
                 :url => reset_password_path(reset_password.id),
                 :html => { :method => :delete ,
                 :confirm => 'Are you sure?'}) do |f|
          rawtext(f.submit("delete"))
        end
    end
  end
end
