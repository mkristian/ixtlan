/**
 *
 */
package de.saumya.gwt.session.client.models;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.saumya.gwt.persistence.client.AbstractResourceFactory;
import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;

public class LocaleFactory extends ResourceFactory<Locale> {

    private final LocaleCollection realLocales = new LocaleCollection(this);

    private static class LocaleCollection extends ResourceCollection<Locale> {

        private static final long serialVersionUID = 1L;

        public LocaleCollection(final AbstractResourceFactory<Locale> factory) {
            super(factory);
        }

        private void fireEvents() {
            fireResourcesLoadedEvents();
        }
    }

    private final ResourcesChangeListener<Locale> realLocalesLoaded;

    private Locale                                allLocale;
    private Locale                                defaultLocale;

    private UserFactory                           userFactory;

    public LocaleFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
        this.realLocalesLoaded = new ResourcesChangeListener<Locale>() {

            @Override
            public void onLoaded(final ResourceCollection<Locale> resources) {
                resetRealLocales();
                LocaleFactory.this.realLocales.fireEvents();
            }
        };
    }

    void setUserFactory(final UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public String storageName() {
        return "locale";
    }

    @Override
    public Locale newResource(final int id) {
        return new Locale(this.repository, this, id, this.userFactory);
    }

    public Locale first(final String code) {
        if (this.all != null) {
            for (final Locale locale : this.all) {
                if (locale.code.equals(code)) {
                    return locale;
                }
            }
        }
        final Locale locale = newResource(0);
        locale.code = code;
        final Map<String, String> q = new HashMap<String, String>();
        q.put("code", locale.code);
        all(q, new ResourcesChangeListener<Locale>() {

            @Override
            public void onLoaded(final ResourceCollection<Locale> resources) {
                final Iterator<Locale> iter = resources.iterator();
                if (iter.hasNext()) {
                    final Locale l = iter.next();
                    locale.id = l.id;
                    locale.code = l.code;
                    locale.createdAt = l.createdAt;
                    // TODO maybe fire resource change
                }
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

    public ResourceCollection<Locale> realLocales() {
        return realLocales(null);
    }

    public ResourceCollection<Locale> realLocales(
            final ResourcesChangeListener<Locale> listener) {
        if (listener != null) {
            this.realLocales.addResourcesChangeListener(listener);
        }
        all(this.realLocalesLoaded);
        resetRealLocales();
        return this.realLocales;
    }

    private void resetRealLocales() {
        this.realLocales.clear();
        this.realLocales.addAll(this.all);
        switch (this.realLocales.size()) {
        default:
            this.realLocales.remove(0);
        case 1:
            this.realLocales.remove(0);
        case 0:
        }
    }
}
