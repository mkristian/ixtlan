/**
 * 
 */
package de.saumya.gwt.session.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotification;

public class DomainFactory extends ResourceFactory<Domain> {

    public DomainFactory(final Repository repository,
            final ResourceNotification notification) {
        super(repository, notification);
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

}