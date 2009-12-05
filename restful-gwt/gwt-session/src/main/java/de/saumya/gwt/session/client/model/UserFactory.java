package de.saumya.gwt.session.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;

public class UserFactory extends ResourceFactory<User> {

    private final LocaleFactory localeFactory;
    private final GroupFactory   roleFactory;

    public UserFactory(final Repository repository,
            final LocaleFactory localeFactory, final GroupFactory roleFactory) {
        super(repository);
        this.localeFactory = localeFactory;
        this.roleFactory = roleFactory;
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
                this.roleFactory);
    }

}
