package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class GroupFactory extends ResourceFactory<Group> {

    private final UserGroupFactory userGroupFactory;

    public GroupFactory(final Repository repository,
            final ResourceNotifications notifications,
            final UserGroupFactory userGroupFactory) {
        super(repository, notifications);
        this.userGroupFactory = userGroupFactory;
    }

    @Override
    public String storageName() {
        return "group";
    }

    @Override
    public Group newResource(final int id) {
        return new Group(this.repository, this, id);
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    protected void putIntoCache(final Group resource) {
        super.putIntoCache(resource);
        // keep "all" cache up to date
        this.userGroupFactory.putIntoCache(resource);
    }

    @Override
    protected void removeFromCache(final Group resource) {
        super.removeFromCache(resource);
        // keep "all" cache up to date
        this.userGroupFactory.removeFromCache(resource);
    }
}
