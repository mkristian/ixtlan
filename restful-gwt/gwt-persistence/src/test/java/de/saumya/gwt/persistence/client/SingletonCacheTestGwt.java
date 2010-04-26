package de.saumya.gwt.persistence.client;


import de.saumya.gwt.persistence.client.AbstractResource.State;

public class SingletonCacheTestGwt extends PersistenceTestGwt {

    private static final String RESOURCE_XML = "<singleton>"
                                                     + "<name>god</name>"
                                                     + "</singleton>";

    private Singleton           singleton;

    private RepositoryMock      repository;

    private SingletonFactory    factory;

    @Override
    protected void gwtSetUp() {
        this.repository = new RepositoryMock();
        this.factory = new SingletonFactory(this.repository,
                new GWTResourceNotification());
        this.singleton = this.factory.newResource();

        this.singleton.name = "god";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.singleton.save();
        this.repository.reset();
    }

    public void testGet() {
        this.repository.addXmlResponse(RESOURCE_XML);
        final Singleton second = this.factory.get();

        assertSame(this.singleton, second);
        assertEquals(State.UP_TO_DATE, second.state);
    }

    public void testDelete() {
        this.repository.addXmlResponse("");
        this.singleton.destroy();
        assertEquals(State.DELETED, this.singleton.state);

        this.repository.reset();
        this.repository.addXmlResponse(RESOURCE_XML);
    }

    public void testClearCache() {
        this.factory.clearCache();
        this.repository.addXmlResponse(RESOURCE_XML);
        final Singleton second = this.factory.get();
        assertEquals(State.UP_TO_DATE, this.singleton.state);
        assertNotSame(this.singleton, second);
    }
}
