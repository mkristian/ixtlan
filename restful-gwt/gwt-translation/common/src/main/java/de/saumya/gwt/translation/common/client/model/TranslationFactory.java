/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.models.UserFactory;

public class TranslationFactory extends ResourceFactory<Translation> {

    private final UserFactory userFactory;

    public TranslationFactory(final Repository repository,
            final ResourceNotifications notification,
            final UserFactory userFactory) {
        super(repository, notification);
        this.userFactory = userFactory;
    }

    @Override
    public Translation newResource(final int id) {
        return new Translation(this.repository, this, this.userFactory, id);
    }

    @Override
    public String storageName() {
        return "translation";
    }

}