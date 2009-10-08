class Views::Groups::GroupsWidget < ErectorWidgets::EntitiesWidget

  def entities_symbol
    :groups
  end

  def title
    "grouplist"
  end
  
  def render_navigation
    if allowed(:groups, :new)
      div :class => :nav_buttons do
        button_to 'new', new_group_path, :method => :get
      end
    end
  end

  def render_table_header
    th "Name"
  end

  def render_table_row(group)
puts groups.inspect
    td do
      args = {}
      if allowed(:groups, :edit)
        args[:href] = edit_group_path(group.id)
      elsif allowed(:groups, :show)
        args[:href] = group_path(group.id)
      end
      a group.name, args
    end
    
    td :class => :cell_buttons do
      if allowed(:groups, :destroy)
        form_for(:group, 
                 :url => group_path(group.id),
                 :html => { :method => :delete ,
                 :confirm => 'Are you sure?'}) do |f|
          rawtext(f.submit("delete"))
        end
      end
    end
  end
end
