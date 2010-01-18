/**
 * 
 */
package de.saumya.gwt.session.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class ConfigurationFactory extends ResourceFactory<Configuration> {

    private final UserFactory userFactory;

    public ConfigurationFactory(final Repository repository,
            final ResourceNotifications notification,
            final UserFactory userFactory) {
        super(repository, notification);
        this.userFactory = userFactory;
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
        return new Configuration(this.repository, this, this.userFactory);
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}