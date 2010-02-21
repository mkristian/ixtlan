/**
 * 
 */
package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class LocaleFactory extends ResourceFactory<Locale> {

    public LocaleFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
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

    public Locale defaultLocale() {
        final Locale locale = get("DEFAULT");
        // if (locale.code == null) {
        // locale.code = "DEFAULT";
        // }
        return locale;
    }

    public Locale allLocale() {
        final Locale locale = get("ALL");
        // if (locale.code == null) {
        // locale.code = "ALL";
        // }
        return locale;
    }

    @Override
    public String defaultSearchParameterName() {
        return "code";
    }

    public Locale newResource(final String code) {
        final Locale locale = new Locale(this.repository, this, code);
        return locale;
    }

    @Override
    protected Locale getResource(final String key) {
        if (key == null) {
            return newResource();
        }
        else {
            Locale result = this.cache.get(key);
            if (result == null) {
                result = newResource(key);
                this.cache.put(key, result);
            }
            return result;
        }
    }
}