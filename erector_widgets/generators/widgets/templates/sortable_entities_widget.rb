class Views::<%= plural_name.camelize %>::<%= plural_name.camelize %>Widget < ErectorWidgets::EntitiesWidget

  def initialize(view, assigns, io, *args)
    super(view, assigns, io, *args)
    @table_widget = <%= plural_name.camelize %>SortableTableWidget.new(view, assigns, io, assigns["#{entities_symbol}"], @__entities, [<% for attribute in attributes -%>
<% if attribute.field_type.to_s != 'text_area' -%>:<%= attribute.column.name %>,<% end -%><% end -%>], @field, @direction)
  end

  def entities_symbol
    :<%= plural_name %>
  end

  def title
    <% if options[:i18n] -%>t("<%= plural_name %>.list")<% else -%>"<%= singular_name %>list"<% end -%>

  end
  
  def render_navigation
<% if options[:add_guard] -%>
    if allowed(:<%= plural_name %>, :new)
<% end -%>
      div :class => :nav_buttons do
        button_to <% if options[:i18n] -%>t('widget.new')<% else -%>'new'<% end-%>, new_<%= singular_name %>_path, :method => :get
      end
<% if options[:add_guard] %>    end
<% end -%>
  end

  def render_table
    @table_widget.render_to(self)
  end
end

class <%= plural_name.camelize %>SortableTableWidget < ErectorWidgets::SortableTableWidget

  def link_args(<%= singular_name %>)
<% if options[:add_guard] -%>
    args = {}
    if allowed(:<%= plural_name %>, :edit)
      args[:href] = edit_<%= singular_name %>_path(<%= singular_name %>.id)
    elsif allowed(:<%= plural_name %>, :show)
      args[:href] = <%= singular_name %>_path(<%= singular_name %>.id)
    end
    args
<% else -%>
    {:href => edit_<%= singular_name %>_path(<%= singular_name %>.id)}
<% end -%>
  end
  
  def delete_form(<%= singular_name %>)
    form_for(:<%= singular_name %>, 
             :url => <%= singular_name %>_path(<%= singular_name %>.id),
             :html => { :method => :delete , #:confirm => 'Are you sure?'
             }) do |f|
      rawtext(f.submit(<% if options[:i18n] -%>t('widget.delete')<% else -%>"delete"<% end -%>))
    end
  end

end
