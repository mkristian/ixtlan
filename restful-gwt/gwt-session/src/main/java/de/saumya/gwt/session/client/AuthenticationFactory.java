package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactoryWithID;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.models.UserFactory;

public class AuthenticationFactory extends
        ResourceFactoryWithID<Authentication> {

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
    public Authentication newResource(final int key) {
        return new Authentication(this.repository, this, this.userFactory, key);
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}
