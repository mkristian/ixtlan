package de.saumya.gwt.session.client.model;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class GroupFactory extends ResourceFactory<Group> {

    private final LocaleFactory localeFactory;
    private final VenueFactory  venueFactory;

    public GroupFactory(final Repository repository,
            final LocaleFactory localeFactory, final VenueFactory venueFactory) {
        super(repository);
        this.localeFactory = localeFactory;
        this.venueFactory = venueFactory;
    }

    @Override
    public String storageName() {
        return "group";
    }

    @Override
    public String keyName() {
        return "id";
    }

    @Override
    public Group newResource() {
        return new Group(this.repository,
                this,
                this.localeFactory,
                this.venueFactory);
    }

}
