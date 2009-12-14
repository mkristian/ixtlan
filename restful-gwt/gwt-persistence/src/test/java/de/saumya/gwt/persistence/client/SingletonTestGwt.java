package de.saumya.gwt.persistence.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Resource.State;

public class SingletonTestGwt extends PersistenceTestGwt {

    public static class Singleton extends Resource<Singleton> {

        Singleton(final Repository repository, final SingletonFactory factory) {
            super(repository, factory, null);
        }

        String name;

        @Override
        protected void appendXml(final StringBuffer buf) {
            append(buf, "name", this.name);
        }

        @Override
        protected void fromXml(final Element root) {
            this.name = getString(root, "name");
        }

        @Override
        public void toString(final StringBuffer buf) {
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

        public SingletonFactory(final Repository repository) {
            super(repository, null);
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
        this.factory = new SingletonFactory(this.repository);
        this.singleton = this.factory.newResource();

        this.singleton.name = "god";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.singleton.save();
        this.repository.reset();
    }

    public void testGet() {
        final Singleton second = this.factory.get(null);

        assertSame(this.singleton, second);
        assertEquals(State.TO_BE_LOADED, second.state);
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
        final Singleton second = this.factory.get(null);
        assertEquals(State.UP_TO_DATE, this.singleton.state);
        assertNotSame(this.singleton, second);
    }
}
