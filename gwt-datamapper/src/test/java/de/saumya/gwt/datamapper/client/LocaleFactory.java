/**
 * 
 */
package de.saumya.gwt.datamapper.client;

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