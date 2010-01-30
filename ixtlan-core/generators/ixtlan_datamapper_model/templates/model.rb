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
  modified_by "Ixtlan::Models::User"

  require 'dm-serializer'
  alias :to_x :to_xml_document
  def to_xml_document(opts = {}, doc = nil)
    unless(opts[:methods])
      opts.merge!({:methods => [:updated_by], :updated_by => {:methods => [], :exclude => [:created_at, :updated_at]}})
    end
    to_x(opts, doc)
  end
<% end -%>

end
