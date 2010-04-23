package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactoryWithIdGenerator;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class PermissionFactory extends
        ResourceFactoryWithIdGenerator<Permission> {

    private final RoleFactory groupFactory;

    public PermissionFactory(final Repository repository,
            final ResourceNotifications notification,
            final RoleFactory groupFactory) {
        super(repository, notification);
        this.groupFactory = groupFactory;
    }

    @Override
    public String storageName() {
        return "permission";
    }

    @Override
    public String keyName() {
        return null;
    }

    @Override
    public Permission newResource(final int id) {
        return new Permission(this.repository, this, this.groupFactory, id);
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}
