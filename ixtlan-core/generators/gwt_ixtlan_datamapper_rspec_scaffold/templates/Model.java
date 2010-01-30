/**
 * 
 */
package <%= package %>.models;

<% if Array(attributes).find { |attr| attr.type == :date } -%>
import java.sql.Date;
<% end -%>
<% if Array(attributes).find { |attr| attr.type == :timestamp } || ! options[:skip_timestamps] -%>
import java.sql.Timestamp;
<% end -%>

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceWithID;
<% unless options[:skip_modified_by] -%>
import de.saumya.gwt.session.client.model.User;
import de.saumya.gwt.session.client.model.UserFactory;
<% end -%>

public class <%= class_name %> extends ResourceWithID<<%= class_name %>> {

<% unless options[:skip_modified_by] -%>
    private final UserFactory userFactory;

<% end -%>
    protected <%= class_name %>(final Repository repository, final <%= class_name %>Factory factory<% unless options[:skip_modified_by] -%>,
            final UserFactory userFactory<% end -%>) {
        super(repository, factory);
<% unless options[:skip_modified_by] -%>
        this.userFactory = userFactory;
<% end -%>
    }

<% Array(attributes).each do |attribute| -%>
    public <% if attribute.type == :date %>Date     <% elsif attribute.type == :integer %>int      <% elsif attribute.type == :boolean %>boolean  <% else %>String   <% end -%> <%= attribute.name.javanize %>;
<% end -%>
<% unless options[:skip_timestamps] -%>
    public Timestamp createdAt;
    public Timestamp updatedAt;
<% end -%>
<% unless options[:skip_modified_by] -%>
    public User      createdBy;
    public User      updatedBy;
<% end -%>

    @Override
    protected void appendXml(final StringBuilder buf) {
        super.appendXml(buf);
<% Array(attributes).each do |attribute| -%>
        appendXml(buf, "<%= attribute.name %>", this.<%= attribute.name.javanize %>);
<% end -%>
<% unless options[:skip_timestamps] -%>
        appendXml(buf, "created_at", this.createdAt);
        appendXml(buf, "updated_at", this.updatedAt);
<% end -%>
<% unless options[:skip_modified_by] -%>
        appendXml(buf, "created_by", this.createdBy);
        appendXml(buf, "updated_by", this.updatedBy);
<% end -%>
    }

    @Override
    protected void fromXml(final Element root) {
        super.fromXml(root);
<% Array(attributes).each do |attribute| -%>
        this.<%= attribute.name.javanize %> = get<% if attribute.type == :date %>Date<% elsif attribute.type == :integer %>Int<% elsif attribute.type == :boolean %>Boolean<% else %>String<% end -%>(root, "<%= attribute.name %>");
<% end -%>
<% unless options[:skip_timestamps] -%>
        this.createdAt = getTimestamp(root, "created_at");
        this.updatedAt = getTimestamp(root, "updated_at");
<% end -%>
<% unless options[:skip_modified_by] -%>
        this.createdBy = this.userFactory.getChildResource(root, "created_by");
        this.updatedBy = this.userFactory.getChildResource(root, "updated_by");
<% end -%>
    }

    @Override
    public void toString(final StringBuilder buf) {
        super.toString(buf);
<% Array(attributes).each do |attribute| -%>
        toString(buf, "<%= attribute.name %>", this.<%= attribute.name.javanize %>);
<% end -%>
<% unless options[:skip_timestamps] -%>
        toString(buf, "created_at", this.createdAt);
        toString(buf, "updated_at", this.updatedAt);
<% end -%>
<% unless options[:skip_modified_by] -%>
        toString(buf, "created_by", this.createdBy);
        toString(buf, "updated_by", this.updatedBy);
<% end -%>
    }

    @Override
    public String display() {
        final StringBuilder builder = new StringBuilder("<%= class_name %>");
        builder.append("(").append(this.id).append(")");
        return builder.toString();
    }

}