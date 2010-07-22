/**
 *
 */
package de.saumya.gwt.session.client.models;

import java.util.HashMap;
import java.util.Map;

import de.saumya.gwt.persistence.client.AbstractResourceFactory;
import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;

public class DomainFactory extends ResourceFactory<Domain> {

    private final DomainCollection realDomains = new DomainCollection(this);

    private static class DomainCollection extends ResourceCollection<Domain> {

        private static final long serialVersionUID = 1L;

        public DomainCollection(final AbstractResourceFactory<Domain> factory) {
            super(factory);
        }

        private void fireEvents() {
            fireResourcesLoadedEvents();
        }
    }

    private final ResourcesChangeListener<Domain> realDomainsLoaded;

    private Domain                                allDomain;

    public DomainFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
        this.realDomainsLoaded = new ResourcesChangeListener<Domain>() {

            @Override
            public void onLoaded(final ResourceCollection<Domain> resources) {
                resetRealLocales();
                DomainFactory.this.realDomains.fireEvents();
            }
        };
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public String storageName() {
        return "domain";
    }

    @Override
    public Domain newResource(final int id) {
        return new Domain(this.repository, this, id);
    }

    public Domain first(final String name) {
        final Domain domain = newResource(0);
        domain.name = name;
        final Map<String, String> q = new HashMap<String, String>();
        q.put("name", domain.name);
        all(q, new ResourcesChangeListener<Domain>() {

            @Override
            public void onLoaded(final ResourceCollection<Domain> resources) {
                final Domain l = resources.iterator().next();
                domain.id = l.id;
                domain.name = l.name;
                domain.createdAt = l.createdAt;
                // TODO maybe fire resource change
            }
        });
        return domain;
    }

    public Domain allDomain() {
        if (this.allDomain == null) {
            this.allDomain = first("ALL");
        }
        return this.allDomain;
    }

    public ResourceCollection<Domain> realDomains() {
        return realDomains(null);
    }

    public ResourceCollection<Domain> realDomains(
            final ResourcesChangeListener<Domain> listener) {
        if (listener != null) {
            this.realDomains.addResourcesChangeListener(listener);
        }
        all(this.realDomainsLoaded);
        resetRealLocales();
        return this.realDomains;
    }

    private void resetRealLocales() {
        this.realDomains.clear();
        this.realDomains.addAll(this.all);
        if (this.realDomains.size() > 0) {
            this.realDomains.remove(0);
        }
    }

}
