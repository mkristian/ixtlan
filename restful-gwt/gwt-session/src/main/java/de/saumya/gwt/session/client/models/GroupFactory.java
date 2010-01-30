package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class GroupFactory extends ResourceFactory<Group> {

    private final LocaleFactory localeFactory;
    private final DomainFactory domainFactory;

    public GroupFactory(final Repository repository,
            final ResourceNotifications notifications,
            final LocaleFactory localeFactory, final DomainFactory domainFactory) {
        super(repository, notifications);
        this.localeFactory = localeFactory;
        this.domainFactory = domainFactory;
    }

    @Override
    public String storageName() {
        return "group";
    }

    @Override
    public String keyName() {
        return "id";
    }

    @Override
    public Group newResource() {
        return new Group(this.repository,
                this,
                this.localeFactory,
                this.domainFactory);
    }

    @Override
    public String defaultSearchParameterName() {
        return "name";
    }

}
