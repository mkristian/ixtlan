/**
 * 
 */
package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class DomainFactory extends ResourceFactory<Domain> {

    public DomainFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
    }

    @Override
    public String storageName() {
        return "domain";
    }

    @Override
    public String keyName() {
        return "id";
    }

    @Override
    public Domain newResource() {
        return new Domain(this.repository, this);
    }

    @Override
    public String defaultSearchParameterName() {
        return keyName();
    }

}