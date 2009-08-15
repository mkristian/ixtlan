package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class RoleFactory extends ResourceFactory<Role> {

    private final LocaleFactory localeFactory;
    private final VenueFactory  venueFactory;

    public RoleFactory(final Repository repository,
            final LocaleFactory localeFactory, final VenueFactory venueFactory) {
        super(repository);
        this.localeFactory = localeFactory;
        this.venueFactory = venueFactory;
    }

    @Override
    public String storageName() {
        return "role";
    }

    @Override
    public String keyName() {
        return "name";
    }

    @Override
    public Role newResource() {
        return new Role(this.repository,
                this,
                this.localeFactory,
                this.venueFactory);
    }

}
