class Views::Users::UserWidget < ErectorWidgets::EntityWidget

  def entity_symbol 
    :user
  end
  
  def title
    if @user.new_record?
      "new user"
    else
      "user #{@user.login}"
    end
  end

  def render_navigation(disabled)
    super
    if disabled and not @user.new_record?
      if allowed(:users, :edit)
        div :class => :nav_buttons do
          button_to 'edit', edit_user_path(@user.id), :method => :get, :class => :button
        end
      end
      if allowed(:users, :new)
        div :class => :nav_buttons do
          button_to 'new', new_user_path, :method => :get, :class => :button
        end
      end
    end
  end

  def render_entity(disabled)
    args = 
      if @user.new_record?
        {:url => users_path.to_s, :html => {:method => :post}}
      elsif disabled
        {:url => edit_user_path(@user.id).to_s, :html => {:method => :get} }
      else
        {:url => user_path(@user.id).to_s, :html => {:method => :put} }
      end
    
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
        b "Login"
        br
        rawtext (f.text_field(:login, :disabled => disabled))
      end
      
      div :class => :second do
        b "roles"
        br
        select :name=>"groups[]", :multiple=>"true", :disabled => disabled do
          options_for_select(@groups.collect{|r| [r.name, r.id] }, @user.groups.collect{|group| group.id })
        end
      end 

      div :class => :first do
        b "Preferred Language"
        br
        select :name=>"user[language]" do
          options_for_select(['en', 'de'], [@user.language])
        end
      end

      
      unless disabled
        div :class => :action_button do
          if @user.new_record?
            rawtext(f.submit("Create"))
          else
            rawtext(f.submit("Update"))
          end
        end
      end
    end
  end
end
