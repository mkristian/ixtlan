/**
 * 
 */
package de.saumya.gwt.persistence.client;


public class SampleFactory extends ResourceFactory<Sample> {

    public SampleFactory(final Repository repository,
            final ResourceNotification notification) {
        super(repository, notification);
    }

    @Override
    public String storageName() {
        return "sample";
    }

    @Override
    public String keyName() {
        return "id";
    }

    @Override
    public Sample newResource() {
        return new Sample(this.repository, this);
    }

}