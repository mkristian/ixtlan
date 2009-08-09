/**
 * 
 */
package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class LocaleFactory extends ResourceFactory<Locale> {

    public LocaleFactory(final Repository repository) {
        super(repository);
    }

    @Override
    public String storageName() {
        return "locale";
    }

    @Override
    public Locale newResource() {
        return new Locale(this.repository, this);
    }

}