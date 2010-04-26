/**
 *
 */
package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.SingletonResourceFactory;

public class ConfigurationFactory extends
        SingletonResourceFactory<Configuration> {

    private final UserFactory   userFactory;
    private final LocaleFactory localeFactory;

    public ConfigurationFactory(final Repository repository,
            final ResourceNotifications notifications,
            final UserFactory userFactory, final LocaleFactory localeFactory) {
        super(repository, notifications);
        this.userFactory = userFactory;
        this.localeFactory = localeFactory;
    }

    @Override
    public String storageName() {
        return "configuration";
    }

    @Override
    public Configuration newResource() {
        return new Configuration(this.repository,
                this,
                this.userFactory,
                this.localeFactory);
    }
}
