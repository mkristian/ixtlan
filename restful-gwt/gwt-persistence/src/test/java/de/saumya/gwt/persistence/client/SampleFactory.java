/**
 * 
 */
package de.saumya.gwt.persistence.client;

public class SampleFactory extends ResourceFactoryWithID<Sample> {

    public SampleFactory(final Repository repository,
            final ResourceNotifications notification) {
        super(repository, notification);
    }

    @Override
    public String storageName() {
        return "sample";
    }

    @Override
    public Sample newResource(final int id) {
        return new Sample(this.repository, this, id);
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}