package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class PermissionFactory extends ResourceFactory<Permission> {

    private final GroupFactory groupFactory;

    public PermissionFactory(final Repository repository,
            final GroupFactory groupFactory) {
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
