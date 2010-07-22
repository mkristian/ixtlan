/**
 *
 */
package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class AuditFactory extends ResourceFactory<Audit> {

    public AuditFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
    }

    @Override
    public Audit newResource(final int id) {
        return new Audit(this.repository, this, id);
    }

    @Override
    public String storageName() {
        return "audit";
    }

    @Override
    public boolean isImmutable() {
        return true;
    }
}
