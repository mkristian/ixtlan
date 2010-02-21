package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactoryWithID;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class GroupFactory extends ResourceFactoryWithID<Group> {

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
    public Group newResource(final int id) {
        return new Group(this.repository,
                this,
                this.localeFactory,
                this.domainFactory,
                id);
    }

    public boolean isImmutable() {
        return true;
    }

    @Override
    public String defaultSearchParameterName() {
        return "name";
    }

}
