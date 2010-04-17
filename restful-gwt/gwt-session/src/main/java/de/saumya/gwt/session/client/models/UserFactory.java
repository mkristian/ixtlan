package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactoryWithID;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class UserFactory extends ResourceFactoryWithID<User> {

    private final LocaleFactory localeFactory;
    private final GroupFactory  groupFactory;

    public UserFactory(final Repository repository,
            final ResourceNotifications notifications,
            final LocaleFactory localeFactory, final GroupFactory groupFactory) {
        super(repository, notifications);
        this.localeFactory = localeFactory;
        this.groupFactory = groupFactory;
    }

    @Override
    public String storageName() {
        return "user";
    }

    @Override
    public String keyName() {
        return "id";
    }

    @Override
    public User newResource(final int key) {
        return new User(this.repository,
                this,
                this.localeFactory,
                this.groupFactory,
                key);
    }

    @Override
    public String defaultSearchParameterName() {
        return "name";
    }

}
