package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactoryWithIdGenerator;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class RoleFactory extends ResourceFactoryWithIdGenerator<Role> {

    public RoleFactory(final Repository repository,
            final ResourceNotifications notification) {
        super(repository, notification);
    }

    @Override
    public String storageName() {
        return "role";
    }

    @Override
    public Role newResource(final int id) {
        return new Role(this.repository, this, id);
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}
