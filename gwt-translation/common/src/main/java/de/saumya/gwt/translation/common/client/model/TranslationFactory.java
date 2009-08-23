/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.model.UserFactory;

public class TranslationFactory extends ResourceFactory<Translation> {

    private final UserFactory userFactory;

    public TranslationFactory(final Repository repository,
            final UserFactory userFactory) {
        super(repository);
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

}