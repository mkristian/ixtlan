package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class UserGroupFactory extends ResourceFactory<UserGroup> {

    private final LocaleFactory localeFactory;
    private final DomainFactory domainFactory;

    public UserGroupFactory(final Repository repository,
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
    public UserGroup newResource(final int id) {
        return new UserGroup(this.repository,
                this,
                this.localeFactory,
                this.domainFactory,
                id);
    }

    public boolean isImmutable() {
        return true;
    }

    @Override
    protected UserGroup getResource(final int key) {
        // no cache since the resource gets used in many context carry different
        // locales, i.e. each user has their own sets of locales for the same
        // group
        return newResource(key);
    }

}
