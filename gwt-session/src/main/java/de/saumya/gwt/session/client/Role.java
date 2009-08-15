/**
 * 
 */
package de.saumya.gwt.session.client;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

class Role extends Resource<Role> {

    private final LocaleFactory localeFactory;
    private final VenueFactory  venueFactory;

    protected Role(final Repository repository, final RoleFactory factory,
            final LocaleFactory localeFactory, final VenueFactory venueFactory) {
        super(repository, factory);
        this.localeFactory = localeFactory;
        this.venueFactory = venueFactory;
    }

    public String            name;

    public Timestamp         createdAt;

    public Resources<Venue>  venues;
    public Resources<Locale> locales;

    @Override
    public String key() {
        return this.name;
    }

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "name", this.name);
        append(buf, "locales", this.locales);
        append(buf, "venues", this.venues);
        append(buf, "created_at", this.createdAt);
    }

    @Override
    protected void fromXml(final Element root) {
        this.name = getString(root, "name");
        this.locales = this.localeFactory.getChildResources(root, "locales");
        this.venues = this.venueFactory.getChildResources(root, "venues");
        this.createdAt = getTimestamp(root, "created_at");
    }

    @Override
    public void toString(final StringBuffer buf) {
        buf.append(":name => ").append(this.name);
        if (this.venues != null) {
            buf.append(", :venues => ").append(this.venues);
        }
        if (this.locales != null) {
            buf.append(", :locales => ").append(this.locales);
        }
        buf.append(", :created_at => ").append(this.createdAt);
    }
}