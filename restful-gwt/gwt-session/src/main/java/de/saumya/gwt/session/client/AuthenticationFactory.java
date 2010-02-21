package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.models.UserFactory;

public class AuthenticationFactory extends ResourceFactory<Authentication> {

    private final UserFactory userFactory;

    public AuthenticationFactory(final Repository repository,
            final ResourceNotifications notification,
            final UserFactory userFactory) {
        super(repository, notification);
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

    @Override
    public Authentication newResource(final String key) {
        return new Authentication(this.repository, this, this.userFactory);
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}
