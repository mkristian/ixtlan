class <%= class_name %>
  include DataMapper::Resource

  property :id, Serial
<% Array(attributes).each do |attribute| -%>
  property :<%= attribute.name %>, <%= attribute.type.to_s.capitalize %>, :nullable => false
<% end -%>
<% unless options[:skip_timestamps] -%>
  timestamps :at
<% end -%>

end
