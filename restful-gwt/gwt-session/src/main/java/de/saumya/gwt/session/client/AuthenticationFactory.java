package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.SingletonResourceFactory;
import de.saumya.gwt.session.client.models.UserFactory;

public class AuthenticationFactory extends
        SingletonResourceFactory<Authentication> {

    private final UserFactory       userFactory;
    private final PermissionFactory permissionFactory;

    public AuthenticationFactory(final Repository repository,
            final ResourceNotifications notification,
            final PermissionFactory permissionFactory,
            final UserFactory userFactory) {
        super(repository, notification);
        this.userFactory = userFactory;
        this.permissionFactory = permissionFactory;
    }

    @Override
    public String storageName() {
        return "authentication";
    }

    @Override
    public Authentication newResource() {
        return new Authentication(this.repository,
                this,
                this.permissionFactory,
                this.userFactory);
    }
}
