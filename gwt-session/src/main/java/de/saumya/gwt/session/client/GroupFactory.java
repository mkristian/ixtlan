package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class GroupFactory extends ResourceFactory<Group> {

    public GroupFactory(final Repository repository) {
        super(repository);
    }

    @Override
    public String storageName() {
        return "group";
    }

    @Override
    public String keyName() {
        return "name";
    }

    @Override
    public Group newResource() {
        return new Group(this.repository, this);
    }

}
