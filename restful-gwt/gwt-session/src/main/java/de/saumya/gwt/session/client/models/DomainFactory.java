/**
 * 
 */
package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactoryWithID;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class DomainFactory extends ResourceFactoryWithID<Domain> {

    public DomainFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
    }

    @Override
    public String storageName() {
        return "domain";
    }

    @Override
    public Domain newResource(final int id) {
        return new Domain(this.repository, this, id);
    }

    @Override
    public String defaultSearchParameterName() {
        return "name";
    }

}