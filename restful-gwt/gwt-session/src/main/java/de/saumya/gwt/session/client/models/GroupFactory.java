package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class GroupFactory extends ResourceFactory<Group> {

    public GroupFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
    }

    @Override
    public String storageName() {
        return "group";
    }

    @Override
    public Group newResource(final int id) {
        return new Group(this.repository, this, id);
    }

    public boolean isImmutable() {
        return true;
    }

    @Override
    public String defaultSearchParameterName() {
        return "name";
    }
}
