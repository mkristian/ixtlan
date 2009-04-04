class Views::Profiles::ProfileWidget < ErectorWidgets::EntityWidget

  def entity_symbol 
    :user
  end
  
  def title
    "profile of #{@user.login}"
  end

  def rernder_errors
    div :class => :errors do
      error_messages_for :user
    end
  end

  def render_entity(disabled)
    args = 
      {:url => profile_path.to_s, :html => {:method => :put}}
    
    form_for(:user, args) do |f|
      div :class => :first do
        b "Name"
        br
        rawtext (f.text_field(:name, :disabled => disabled))
      end
      
      div :class => :second do
        b "Email"
        br
        rawtext (f.text_field(:email, :disabled => disabled))
      end

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

      if @groups.size > 0
        div :class => :first do
          b "Old Password"
          br
          input :disabled => disabled, :type => :password, :name => :old_password
        end
        
      end

      unless disabled
        div :class => :action_button do
          rawtext(f.submit("Update"))
        end
      end

      if @groups.size > 0
        div :class => :second do
          b "roles"
          br
          select :name=>"groups[]", :multiple=>"true", :disabled => true do
            options_for_select(@groups.collect{|r| [r.name, r.id] }, @user.groups.collect{|group| group.id })
          end
        end 
      end
    end
  end
end
