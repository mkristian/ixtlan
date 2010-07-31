package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class UserFactory extends ResourceFactory<User> {

    private final LocaleFactory    localeFactory;
    private final UserGroupFactory userGroupFactory;

    public UserFactory(final Repository repository,
            final ResourceNotifications notifications,
            final GroupFactory groupFactory) {
        super(repository, notifications);
        this.localeFactory = groupFactory.userGroupFactory.localeFactory;
        this.userGroupFactory = groupFactory.userGroupFactory;
        groupFactory.setUserFactory(this);
    }

    @Override
    public String storageName() {
        return "user";
    }

    @Override
    public User newResource(final int key) {
        return new User(this.repository,
                this,
                this.localeFactory,
                this.userGroupFactory,
                key);
    }
}
