/**
 *
 */
package de.saumya.gwt.session.client.models;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;

public class UserGroup extends Resource<UserGroup> {

    private final LocaleFactory localeFactory;
    private final DomainFactory domainFactory;

    protected UserGroup(final Repository repository,
            final UserGroupFactory factory, final LocaleFactory localeFactory,
            final DomainFactory domainFactory, final int id) {
        super(repository, factory, id);
        this.localeFactory = localeFactory;
        this.domainFactory = domainFactory;
    }

    public String                     name;

    public Timestamp                  createdAt;

    public ResourceCollection<Domain> domains;
    public ResourceCollection<Locale> locales;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "name", this.name);
        appendXml(buf, "locales", this.locales);
        appendXml(buf, "domains", this.domains);
        appendXml(buf, "created_at", this.createdAt);
    }

    @Override
    protected void fromElement(final Element root) {
        this.name = getString(root, "name");
        this.locales = this.localeFactory.getChildResourceCollection(root,
                                                                     "locales");
        this.domains = this.domainFactory.getChildResourceCollection(root,
                                                                     "domains");
        this.createdAt = getTimestamp(root, "created_at");
    }

    @Override
    public void toString(final StringBuilder buf) {
        toString(buf, "name", this.name);
        toString(buf, "domains", this.domains);
        toString(buf, "locales", this.locales);
        toString(buf, "created_at", this.createdAt);
    }

    @Override
    public String display() {
        return this.name;
    }

    public boolean isRoot() {
        return "root".equals(this.name);
    }

    public boolean isLocalesAdmin() {
        return "locales".equals(this.name);
    }

    public boolean isDomainsAdmin() {
        return "domains".equals(this.name);
    }
}