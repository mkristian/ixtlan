/**
 * 
 */
package de.saumya.gwt.session.client.models;

import java.util.HashMap;
import java.util.Map;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceFactoryWithID;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;

public class LocaleFactory extends ResourceFactoryWithID<Locale> {

    private Locale allLocale;
    private Locale defaultLocale;

    public LocaleFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
    }

    @Override
    public String storageName() {
        return "locale";
    }

    @Override
    public Locale newResource(final int id) {
        return new Locale(this.repository, this, id);
    }

    public Locale first(final String code) {
        final Locale locale = newResource();
        locale.code = code;
        final Map<String, String> q = new HashMap<String, String>();
        q.put("code", locale.code);
        all(q, new ResourcesChangeListener<Locale>() {

            @Override
            public void onLoaded(final ResourceCollection<Locale> resources) {
                final Locale l = resources.get(0);
                locale.id = l.id;
                locale.code = l.code;
                locale.createdAt = l.createdAt;
                // TODO maybe fire resource change
            }
        });
        return locale;
    }

    public Locale defaultLocale() {
        if (this.defaultLocale == null) {
            this.defaultLocale = first("DEFAULT");
        }
        return this.defaultLocale;
    }

    public Locale allLocale() {
        if (this.allLocale == null) {
            this.allLocale = first("ALL");
        }
        return this.allLocale;
    }

    @Override
    public String defaultSearchParameterName() {
        return "code";
    }
}