package de.saumya.gwt.datamapper.client;

import com.google.gwt.junit.client.GWTTestCase;

import de.saumya.gwt.datamapper.client.Resource.State;

public class CacheTestGwt extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "de.saumya.gwt.datamapper.Datamapper";
    }

    private static final String RESOURCE_XML = "<sample>"
                                                     + "<id>123</id>"
                                                     + "<language>en</language>"
                                                     + "<country>GE</country>"
                                                     + "</sample>";

    private Sample              sample;

    private RepositoryMock      repository;

    private SampleFactory       factory;

    @Override
    protected void gwtSetUp() {
        this.repository = new RepositoryMock();
        this.factory = new SampleFactory(this.repository);
        this.sample = this.factory.newResource();

        this.sample.country = "GE";
        this.sample.language = "en";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.sample.save();
        this.repository.reset();
    }

    public void testGet() {
        final Sample second = this.factory.get(123, null);

        assertSame(this.sample, second);
        assertEquals(State.TO_BE_LOADED, second.state);
    }

    public void testGetAll() {
        this.repository.addXmlResponse("<samples>" + RESOURCE_XML
                + RESOURCE_XML.replace("123", "234") + "</samples>");
        final Resources<Sample> samples = this.factory.all(null);
        assertSame(this.sample, samples.get(0));

        this.repository.reset();
        final Sample second = this.factory.get(234, null);
        assertSame(second, samples.get(1));
    }

    public void testGetWithNested() {
        this.repository.addXmlResponse("<sample><id>345</id><language>de</language>"
                + RESOURCE_XML.replaceAll("sample", "child")
                        .replace("GE", "JP") + "</sample>");
        final Sample second = this.factory.get(345, null);
        assertSame(second.child, this.sample);
        assertEquals(this.sample.country, "JP");
    }

    public void testDelete() {
        this.repository.addXmlResponse("");
        this.sample.destroy();
        assertEquals(State.DELETED, this.sample.state);

        this.repository.reset();
        this.repository.addXmlResponse(RESOURCE_XML);
        final Sample second = this.factory.get(123, null);
        assertEquals(State.UP_TO_DATE, second.state);
        assertNotSame(this.sample, second);
    }

    public void testClearCache() {
        this.factory.clearCache();
        this.repository.addXmlResponse(RESOURCE_XML);
        final Sample second = this.factory.get(123, null);
        assertEquals(State.UP_TO_DATE, this.sample.state);
        assertNotSame(this.sample, second);
    }
}
