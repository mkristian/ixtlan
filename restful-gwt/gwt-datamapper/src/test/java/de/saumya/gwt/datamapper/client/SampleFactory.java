/**
 * 
 */
package de.saumya.gwt.datamapper.client;

public class SampleFactory extends ResourceFactory<Sample> {

    public SampleFactory(final Repository repository) {
        super(repository);
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