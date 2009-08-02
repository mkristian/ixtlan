package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.datamapper.client.Resources;

public class PermissionFactory extends ResourceFactory<Permission> {

    private final RoleFactory roleFactory;

    public PermissionFactory(final Repository repository,
            final RoleFactory roleFactory) {
        super(repository);
        this.roleFactory = roleFactory;
    }

    @Override
    public String storageName() {
        return "permission";
    }

    @Override
    public Permission newResource() {
        return new Permission(this.repository, this);
    }

    public Resources<Role> newRoleResources() {
        return this.roleFactory.newResources();
    }

}
