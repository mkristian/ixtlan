/**
 * 
 */
package de.saumya.gwt.session.client.model;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceWithID;
import de.saumya.gwt.persistence.client.Resources;

public class Group extends ResourceWithID<Group> {

    private final LocaleFactory localeFactory;
    private final DomainFactory  venueFactory;

    protected Group(final Repository repository, final GroupFactory factory,
            final LocaleFactory localeFactory, final DomainFactory venueFactory) {
        super(repository, factory, null);
        this.localeFactory = localeFactory;
        this.venueFactory = venueFactory;
    }

    public String            name;

    public Timestamp         createdAt;

    public Resources<Domain>  venues;
    public Resources<Locale> locales;

    @Override
    protected void appendXml(final StringBuffer buf) {
        super.appendXml(buf);
        append(buf, "name", this.name);
        append(buf, "locales", this.locales);
        append(buf, "venues", this.venues);
        append(buf, "created_at", this.createdAt);
    }

    @Override
    protected void fromXml(final Element root) {
        super.fromXml(root);
        this.name = getString(root, "name");
        this.locales = this.localeFactory.getChildResources(root, "locales");
        this.venues = this.venueFactory.getChildResources(root, "venues");
        this.createdAt = getTimestamp(root, "created_at");
    }

    @Override
    public void toString(final StringBuffer buf) {
        super.toString(buf);
        buf.append(":name => ").append(this.name);
        if (this.venues != null) {
            buf.append(", :venues => ").append(this.venues);
        }
        if (this.locales != null) {
            buf.append(", :locales => ").append(this.locales);
        }
        buf.append(", :created_at => ").append(this.createdAt);
    }

    @Override
    public String display() {
        return this.name;
    }
}