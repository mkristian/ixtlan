class Views::Users::UsersWidget < ErectorWidgets::EntitiesWidget

  def entities_symbol
    :users
  end

  def title
    "userlist"
  end
  
  def render_navigation
    if allowed(:users, :new)
      div :class => :nav_buttons do
        button_to 'new', new_user_path, :method => :get
      end
    end
  end

  def render_table_header
    th "Login"
    th "Name"
    th "Email"
    th "Created at"
    th "Updated at"
  end

  def render_table_row(user)
    td do
      args = {}
      if allowed(:users, :edit)
        args[:href] = edit_user_path(user.id)
      elsif allowed(:users, :show)
        args[:href] = user_path(user.id)
      end
      a user.login, args
    end
    td user.name
    td user.email
    td user.created_at.strftime("%h %e %Y"), :title => user.created_at.asctime
    td user.updated_at.strftime("%h %e %Y"), :title => user.updated_at.asctime

    td :class => :cell_buttons do
      if allowed(:users, :destroy)
        form_for(:user, 
                 :url => user_path(user.id),
                 :html => { :method => :delete ,
                 :confirm => 'Are you sure?'}) do |f|
          rawtext(f.submit("delete"))
        end
      end
    end
  end
end
