/**
 * 
 */
package de.saumya.gwt.courses.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.VenueFactory;

public class CourseFactory extends ResourceFactory<Course> {

    final VenueFactory venueFactory;
    
    public CourseFactory(Repository repository, VenueFactory venueFactory) {
        super(repository);
        this.venueFactory = venueFactory;
    }

    public String storageName() {
        return "course";
    }

    protected Course newResource() {
        return new Course(repository, this, venueFactory);
    }

}