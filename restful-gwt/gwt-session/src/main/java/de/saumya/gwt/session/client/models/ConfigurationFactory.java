/**
 * 
 */
package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class ConfigurationFactory extends ResourceFactory<Configuration> {

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
    public String keyName() {
        return null;
    }

    @Override
    public Configuration newResource() {
        return new Configuration(this.repository,
                this,
                this.userFactory,
                this.localeFactory);
    }

    @Override
    public Configuration newResource(final String key) {
        return newResource();
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}