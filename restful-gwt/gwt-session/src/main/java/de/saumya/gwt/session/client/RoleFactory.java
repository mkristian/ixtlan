package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotification;

public class RoleFactory extends ResourceFactory<Role> {

    public RoleFactory(final Repository repository,
            final ResourceNotification notification) {
        super(repository, notification);
    }

    @Override
    public String storageName() {
        return "role";
    }

    @Override
    public String keyName() {
        return "name";
    }

    @Override
    public Role newResource() {
        return new Role(this.repository, this);
    }

}
