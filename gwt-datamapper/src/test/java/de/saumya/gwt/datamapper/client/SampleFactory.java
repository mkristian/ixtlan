/**
 * 
 */
package de.saumya.gwt.datamapper.client;

public class SampleFactory extends ResourceFactory<Sample> {

    public SampleFactory(Repository repository) {
        super(repository);
    }

    public String storageName() {
        return "locale";
    }

    protected Sample newResource() {
        return new Sample(repository, this);
    }

}