package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;

public class PermissionFactory extends ResourceFactory<Permission> {

    private final RoleFactory groupFactory;

    public PermissionFactory(final Repository repository,
            final RoleFactory groupFactory) {
        super(repository);
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
    public Permission newResource() {
        return new Permission(this.repository, this, this.groupFactory);
    }

}
