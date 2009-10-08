class Views::ResetPasswords::ResetPasswordWidget < ErectorWidgets::EntityWidget

  def entity_symbol 
    :user #reset_password
  end
  
  def title
    "reset password for #{@reset_password.user.login}"
  end
  
  def render_entity(disabled)
    args = 
      {:url => reset_password_path(@reset_password.token).to_s, :html => {:method => :put}}
    
    form_for(:user, args) do |f|
      div :class => :first do
        b "New Password"
        br
        rawtext (f.password_field(:password, :disabled => disabled))
      end

      div :class => :second do
        b "New Password confirmed"
        br
        rawtext (f.password_field(:password_confirmation, :disabled => disabled))
      end

      unless disabled
        div :class => :action_button do
          rawtext(f.submit("Update"))
        end
      end
    end
  end
end
