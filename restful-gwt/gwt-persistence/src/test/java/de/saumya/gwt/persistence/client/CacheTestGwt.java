package de.saumya.gwt.persistence.client;

import java.util.Iterator;

import de.saumya.gwt.persistence.client.Resource.State;

public class CacheTestGwt extends PersistenceTestGwt {

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
        this.factory = new SampleFactory(this.repository,
                new GWTResourceNotification());
        this.sample = this.factory.newResource();

        this.sample.country = "GE";
        this.sample.language = "en";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.sample.save();
        this.repository.reset();
    }

    public void testGet() {
        this.repository.addXmlResponse(RESOURCE_XML);
        final Sample second = this.factory.get(123);

        assertSame(this.sample, second);
        assertEquals(State.UP_TO_DATE, second.state);
    }

    public void testGetAll() {
        this.repository.addXmlResponse("<samples>" + RESOURCE_XML
                + RESOURCE_XML.replace("123", "234") + "</samples>");
        final ResourceCollection<Sample> samples = this.factory.all();
        final Iterator<Sample> iterator = samples.iterator();
        assertSame(this.sample, iterator.next());

        this.repository.reset();
        final Sample second = this.factory.get(234);
        assertSame(second, iterator.next());
    }

    public void testGetWithNested() {
        this.repository.addXmlResponse("<sample><id>345</id><language>de</language>"
                + RESOURCE_XML.replaceAll("sample", "child")
                        .replace("GE", "JP") + "</sample>");
        final Sample second = this.factory.get(345);
        assertSame(second.child, this.sample);
        assertEquals(this.sample.country, "JP");
    }

    public void testDelete() {
        this.repository.addXmlResponse("");
        this.sample.destroy();
        assertEquals(State.DELETED, this.sample.state);

        this.repository.reset();
        this.repository.addXmlResponse(RESOURCE_XML);
        final Sample second = this.factory.get(123);
        assertEquals(State.UP_TO_DATE, second.state);
        assertNotSame(this.sample, second);
    }

    public void testClearCache() {
        this.factory.clearCache();
        this.repository.addXmlResponse(RESOURCE_XML);
        final Sample second = this.factory.get(123);
        assertEquals(State.UP_TO_DATE, this.sample.state);
        assertNotSame(this.sample, second);
    }
}
