package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class UserFactory extends ResourceFactory<User> {

    private final LocaleFactory localeFactory;
    private final RoleFactory   roleFactory;

    public UserFactory(final Repository repository,
            final LocaleFactory localeFactory, final RoleFactory roleFactory) {
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
