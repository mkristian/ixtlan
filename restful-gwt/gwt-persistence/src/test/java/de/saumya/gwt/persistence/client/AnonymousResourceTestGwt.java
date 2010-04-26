package de.saumya.gwt.persistence.client;

abstract public class AnonymousResourceTestGwt<T extends AnonymousResource<T>>
        extends AbstractResourceTestGwt<T> {

    protected AnonymousResourceFactory<T> factory;

    @Override
    protected void gwtSetUp() {
        this.repository = new RepositoryMock();
        this.factory = factorySetUp();
        gwtSetUp(this.factory);
    }

    public void testRetrieveAll() {
        this.repository.reset();
        this.repository.addXmlResponse(resourcesXml());

        final ResourceCollection<T> resources = this.factory.all(this.countingResourcesListener);

        assertEquals(resourcesXml() + "\n" + resources.toString(),
                     2,
                     this.countingResourcesListener.count());
        int id = 0;
        final String[] xmls = { resource1Xml(), resource2Xml() };
        for (final AbstractResource<T> resource : resources) {
            assertTrue(resource.isUptodate());
            assertEquals(xmls[id++], resource.toXml());
        }
    }

    public void testMarshallingUnmarshallingResource() {
        final AbstractResource<T> resource = this.factory.newResource();
        resource.fromXml(resource1Xml());

        assertEquals(resource1Xml(), resource.toXml());
    }

    public void testMarshallingUnmarshallingResources() {
        if (resourcesXml() != null) {
            final ResourceCollection<T> resources = new ResourceCollection<T>(this.factory);
            resources.fromXml(resourcesXml());

            assertEquals(resourcesXml(), resources.toXml());
        }
    }

    abstract protected AnonymousResourceFactory<T> factorySetUp();

    abstract protected String resourcesXml();

    abstract protected String resource2Xml();
}
