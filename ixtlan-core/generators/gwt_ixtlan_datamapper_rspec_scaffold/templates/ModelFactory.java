/**
 *
 */
package <%= package %>.models;


import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

<% unless options[:skip_modified_by] -%>
import de.saumya.gwt.session.client.models.UserFactory;
<% end -%>

public class <%= class_name %>Factory extends ResourceFactory<<%= class_name %>> {

<% unless options[:skip_modified_by] -%>
    private final UserFactory userFactory;

<% end -%>
    public <%= class_name %>Factory(final Repository repository,
            final ResourceNotifications notifications<% unless options[:skip_modified_by] -%>,
            final UserFactory userFactory<% end -%>) {
        super(repository, notifications);
<% unless options[:skip_modified_by] -%>
        this.userFactory = userFactory;
<% end -%>
    }

    @Override
    public <%= class_name %> newResource(final int id) {
        return new <%= class_name %>(this.repository, this<% unless options[:skip_modified_by] -%>, this.userFactory<% end -%>, id);
    }

    @Override
    public String storageName() {
        return "<%= singular_name %>";
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}
