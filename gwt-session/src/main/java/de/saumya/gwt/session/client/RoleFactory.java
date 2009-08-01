package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.datamapper.client.Resources;

public class RoleFactory extends ResourceFactory<Role>{

    private final LocaleFactory localeFactory;
    private final VenueFactory venueFactory;
    
    public RoleFactory(Repository repository, LocaleFactory localeFactory, VenueFactory venueFactory) {
        super(repository);
        this.localeFactory = localeFactory;
        this.venueFactory = venueFactory;
    }

    @Override
    public String storageName() {
        return "role";
    }

    @Override
    public Role newResource() {
        return new Role(repository, this);
    }
    
    public Resources<Locale> newLocaleResources() {
        return localeFactory.newResources();
    }

    public Resources<Venue> newVenueResources() {
        return venueFactory.newResources();
    }

}
