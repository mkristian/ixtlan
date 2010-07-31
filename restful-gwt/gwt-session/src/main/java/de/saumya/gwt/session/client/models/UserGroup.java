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
    private final UserFactory   userFactory;

    protected UserGroup(final Repository repository,
            final UserGroupFactory factory, final LocaleFactory localeFactory,
            final DomainFactory domainFactory, final int id,
            final UserFactory userFactory) {
        super(repository, factory, id);
        this.localeFactory = localeFactory;
        this.domainFactory = domainFactory;
        this.userFactory = userFactory;
        this.domains = new ResourceCollection<Domain>(this.domainFactory);
        this.locales = new ResourceCollection<Locale>(this.localeFactory);
    }

    public String                     name;

    public Timestamp                  createdAt;

    public User                       createdBy;

    public ResourceCollection<Domain> domains;
    public ResourceCollection<Locale> locales;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "name", this.name);
        appendXml(buf, "locales", this.locales);
        appendXml(buf, "domains", this.domains);
        appendXml(buf, "created_at", this.createdAt);
        appendXml(buf, "created_by", this.createdBy);
    }

    @Override
    protected void fromElement(final Element root) {
        this.name = getString(root, "name");
        this.locales = this.localeFactory.getChildResourceCollection(root,
                                                                     "locales");
        this.domains = this.domainFactory.getChildResourceCollection(root,
                                                                     "domains");
        this.createdAt = getTimestamp(root, "created_at");
        this.createdBy = this.userFactory.getChildResource(root,
                                                           "created_by",
                                                           this.createdBy);
    }

    @Override
    public void toString(final String indent, final StringBuilder buf) {
        toString(indent, buf, "name", this.name);
        toString(indent, buf, "domains", this.domains);
        toString(indent, buf, "locales", this.locales);
        toString(indent, buf, "created_at", this.createdAt);
        toString(indent, buf, "created_by", this.createdBy);
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
