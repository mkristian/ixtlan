/**
 * 
 */
package de.saumya.gwt.session.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotification;

public class LocaleFactory extends ResourceFactory<Locale> {

    public LocaleFactory(final Repository repository,
            final ResourceNotification notification) {
        super(repository, notification);
    }

    @Override
    public String storageName() {
        return "locale";
    }

    @Override
    public String keyName() {
        return "code";
    }

    @Override
    public Locale newResource() {
        return new Locale(this.repository, this);
    }

    Locale defaultLocale() {
        return get("DEFAULT", null);
    }

    Locale allLocale() {
        return get("*", null);
    }
}