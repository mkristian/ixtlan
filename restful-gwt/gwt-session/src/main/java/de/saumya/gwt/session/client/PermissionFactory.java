package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.AnonymousResourceFactory;
import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class PermissionFactory extends AnonymousResourceFactory<Permission> {

    private final RoleFactory roleFactory;

    public PermissionFactory(final Repository repository,
            final ResourceNotifications notifications,
            final RoleFactory roleFactory) {
        super(repository, notifications);
        this.roleFactory = roleFactory;
    }

    @Override
    public String storageName() {
        return "permission";
    }

    @Override
    public Permission newResource() {
        return new Permission(this.repository, this, this.roleFactory);
    }
}
