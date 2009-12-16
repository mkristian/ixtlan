package de.saumya.gwt.session.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotification;

public class UserFactory extends ResourceFactory<User> {

    private final LocaleFactory localeFactory;
    private final GroupFactory  groupFactory;

    public UserFactory(final Repository repository,
            final ResourceNotification notification,
            final LocaleFactory localeFactory, final GroupFactory groupFactory) {
        super(repository, notification);
        this.localeFactory = localeFactory;
        this.groupFactory = groupFactory;
    }

    @Override
    public String storageName() {
        return "user";
    }

    @Override
    public String keyName() {
        return "login";
    }

    @Override
    public User newResource() {
        return new User(this.repository,
                this,
                this.localeFactory,
                this.groupFactory);
    }

}
