package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.model.UserFactory;

public class AuthenticationFactory extends ResourceFactory<Authentication> {

    private final UserFactory userFactory;

    public AuthenticationFactory(final Repository repository,
            final UserFactory userFactory) {
        super(repository);
        this.userFactory = userFactory;
    }

    @Override
    public String storageName() {
        return "authentication";
    }

    @Override
    public String keyName() {
        return "token";
    }

    @Override
    public Authentication newResource() {
        return new Authentication(this.repository, this, this.userFactory);
    }

}
