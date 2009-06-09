en:
  <%= plural_name %>:
    <%= singular_name %>_created: <%= class_name %> was successfully created.
    <%= singular_name %>_updated: <%= class_name %> was successfully updated.
    <%= singular_name %>_deleted: <%= class_name %> was successfully deleted.
    new_<%= singular_name %>: new <%= singular_name %>
    <%= singular_name %>: <%= singular_name %>
    list: <%= singular_name %>list
<% for attribute in attributes -%>
    <%= attribute.column.name %>: <%= attribute.column.human_name %>
<% end -%>     
