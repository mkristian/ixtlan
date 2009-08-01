/**
 * 
 */
package de.saumya.gwt.courses.client;

import java.sql.Date;
import java.sql.Timestamp;

import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceWithID;
import de.saumya.gwt.session.client.Venue;
import de.saumya.gwt.session.client.VenueFactory;

public class Course extends ResourceWithID<Course> {

    final VenueFactory venueFactory;
    
    Course(Repository repository, CourseFactory factory, VenueFactory venueFactory) {
        super(repository, factory);
        this.venueFactory = venueFactory;
    }

    public Venue venue;
    public Date from;
    public Date to;

    public Timestamp createdAt;
    public Timestamp updatedAt;


    protected void appendXml(StringBuffer buf) {
        super.appendXml(buf);
        venue.toXml(buf);
        append(buf, "from", from.toString());
        append(buf, "to", to.toString());
        append(buf, "created_at", createdAt);
        append(buf, "updated_at", updatedAt);
    }

    protected void fromXml(Element root) {
        super.fromXml(root);
        venue = venueFactory.newResource();
        venue.fromXml(root);
        from = getDate(root, "from");
        to = getDate(root, "to");
        createdAt = getTimestamp(root, "created_at");
        updatedAt = getTimestamp(root, "updated_at");
        GWT.log(this.toString(), null);
    }

    public void toString(StringBuffer buf) {
        super.toString(buf);
        if(venue != null) {
            buf.append(", :venue => ").append(venue.toString());
        }
        buf.append(", :from => ").append(from);
        buf.append(", :to => ").append(to);
        buf.append(", :created_at => ").append(createdAt);
        buf.append(", :updated_at => ").append(updatedAt);
    }
}