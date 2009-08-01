/**
 * 
 */
package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class VenueFactory extends ResourceFactory<Venue> {

    public VenueFactory(Repository repository) {
        super(repository);
    }

    public String storageName() {
        return "venue";
    }

    public Venue newResource() {
        return new Venue(repository, this);
    }

}