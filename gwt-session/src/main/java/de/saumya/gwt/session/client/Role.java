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
    
    private final RoleFactory factory;
    
    protected Role(Repository repository, RoleFactory factory) {
        super(repository, factory);
        this.factory = factory;
    }

    public String name;
    
    public Timestamp createdAt;

    public Resources<Venue> venues;
    public Resources<Locale> locales;
    
    @Override
    protected String key() {
        return name;
    }

    protected void appendXml(StringBuffer buf) {
        append(buf, "name", name);
        if (locales != null)
            locales.toXml(buf);
        if (venues != null)
            venues.toXml(buf);
        append(buf, "created_at", createdAt);
    }

    protected void fromXml(Element root) {
        name = getString(root, "name");
        Element child = getChildElement(root, "locales");
        if (child != null){
            locales = factory.newLocaleResources();
            locales.fromXml(child);
        }
        child = getChildElement(root, "venues");
        if (child != null){
            venues = factory.newVenueResources();
            venues.fromXml(child);
        }
        createdAt = getTimestamp(root, "created_at");
    }

    public void toString(StringBuffer buf) {
        buf.append(":name => ").append(name);
        if (venues != null)
            buf.append(", :venues => ").append(venues);
        if (locales != null)
            buf.append(", :locales => ").append(locales);            
        buf.append(", :created_at => ").append(createdAt);
    }
}