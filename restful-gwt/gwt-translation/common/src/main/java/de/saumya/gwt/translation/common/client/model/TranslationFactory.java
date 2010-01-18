/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.model.UserFactory;

public class TranslationFactory extends ResourceFactory<Translation> {

    private final UserFactory userFactory;

    public TranslationFactory(final Repository repository,
            final ResourceNotifications notification,
            final UserFactory userFactory) {
        super(repository, notification);
        this.userFactory = userFactory;
    }

    @Override
    public Translation newResource() {
        return new Translation(this.repository, this, this.userFactory);
    }

    @Override
    public String storageName() {
        return "translation";
    }

    @Override
    public String keyName() {
        return "id";
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}