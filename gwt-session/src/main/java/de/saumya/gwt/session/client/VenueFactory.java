/**
 * 
 */
package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class VenueFactory extends ResourceFactory<Venue> {

    public VenueFactory(final Repository repository) {
        super(repository);
    }

    @Override
    public String storageName() {
        return "venue";
    }

    @Override
    public String keyName() {
        return "id";
    }

    @Override
    public Venue newResource() {
        return new Venue(this.repository, this);
    }

}