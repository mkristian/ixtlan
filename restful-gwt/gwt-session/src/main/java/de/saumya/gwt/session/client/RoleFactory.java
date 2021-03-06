package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.AnonymousResourceFactory;
import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class RoleFactory extends AnonymousResourceFactory<Role> {

    public RoleFactory(final Repository repository,
            final ResourceNotifications notification) {
        super(repository, notification);
    }

    @Override
    public String storageName() {
        return "role";
    }

    @Override
    public Role newResource() {
        return new Role(this.repository, this);
    }
}
