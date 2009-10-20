/**
 * 
 */
package de.saumya.gwt.session.client.model;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class ConfigurationFactory extends ResourceFactory<Configuration> {

    private final UserFactory userFactory;

    public ConfigurationFactory(final Repository repository,
            final UserFactory userFactory) {
        super(repository);
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

}