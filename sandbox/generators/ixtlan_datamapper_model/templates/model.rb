class <%= class_name %>
  include DataMapper::Resource

  property :id, Serial
<% Array(attributes).each do |attribute| -%>
  property :<%= attribute.name %>, <%= attribute.type.to_s.capitalize %>, :nullable => false <% if attribute.type == :string or attribute.type == :text or attribute.type == :slug -%>, :format => /^[^<'&">]*$/<% if attribute.type == :string or attribute.type == :slug %>, :length => 255<% end -%><% end -%>

<% end -%>
<% unless options[:skip_timestamps] -%>
  timestamps :at
<% end -%>

<% unless options[:skip_modified_by] -%>
  modified_by "Ixtlan::User"
<% end -%>

end
