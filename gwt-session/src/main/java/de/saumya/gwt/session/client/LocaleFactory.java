/**
 * 
 */
package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class LocaleFactory extends ResourceFactory<Locale> {

    public LocaleFactory(Repository repository) {
        super(repository);
    }

    public String storageName() {
        return "locale";
    }

    protected Locale newResource() {
        return new Locale(repository, this);
    }

}