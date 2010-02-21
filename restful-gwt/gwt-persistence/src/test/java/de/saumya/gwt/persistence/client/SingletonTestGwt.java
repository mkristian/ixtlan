package de.saumya.gwt.persistence.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Resource.State;

public class SingletonTestGwt extends PersistenceTestGwt {

    public static class Singleton extends Resource<Singleton> {

        Singleton(final Repository repository, final SingletonFactory factory) {
            super(repository, factory);
        }

        String name;

        @Override
        protected void appendXml(final StringBuilder buf) {
            appendXml(buf, "name", this.name);
        }

        @Override
        protected void fromXml(final Element root) {
            this.name = getString(root, "name");
        }

        @Override
        public void toString(final StringBuilder buf) {
            buf.append(", :name => ").append(this.name);
        }

        @Override
        public String display() {
            return this.name;
        }

        @Override
        public String key() {
            return null;
        }
    }

    public static class SingletonFactory extends ResourceFactory<Singleton> {

        public SingletonFactory(final Repository repository,
                final ResourceNotifications notification) {
            super(repository, notification);
        }

        @Override
        public String storageName() {
            return "singleton";
        }

        @Override
        public String keyName() {
            return null;
        }

        @Override
        public Singleton newResource() {
            return new Singleton(this.repository, this);
        }

        @Override
        public Singleton newResource(final String key) {
            return new Singleton(this.repository, this);
        }

        @Override
        public String defaultSearchParameterName() {
            return null;
        }

    }

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
