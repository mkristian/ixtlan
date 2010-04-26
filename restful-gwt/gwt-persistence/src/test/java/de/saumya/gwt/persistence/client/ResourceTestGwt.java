package de.saumya.gwt.persistence.client;

abstract public class ResourceTestGwt<T extends Resource<T>> extends
        AbstractMutableResourceTestGwt<T> {

    protected ResourceFactory<T> factory;

    @Override
    protected void gwtSetUp() {
        this.repository = new RepositoryMock();
        this.factory = factorySetUp();
        gwtSetUp(this.factory);
    }

    public void testRetrieve() {
        this.repository.addXmlResponse(resource1Xml());

        final AbstractResource<T> rsrc = this.factory.get(idValue(),
                                                          this.countingResourceListener);

        assertEquals(1, this.countingResourceListener.count());
        assertTrue(this.resource.isUptodate());
        assertEquals(this.resource.toString(), rsrc.toString());
    }

    public void testRetrieveAll() {
        if (resourcesXml() != null) {
            this.repository.addXmlResponse(resourcesXml());

            final ResourceCollection<T> resources = this.factory.all(this.countingResourcesListener);

            assertEquals(2, this.countingResourcesListener.count());
            int id = 0;
            final String[] xmls = { resource1Xml(), resource2Xml() };
            for (final AbstractResource<T> rsrc : resources) {
                assertTrue(this.resource.isUptodate());
                assertEquals(xmls[id++], rsrc.toXml());
            }
        }
    }

    public void testMarshallingUnmarshallingResource() {
        final AbstractResource<T> resource = this.factory.newResource(idValue());
        resource.fromXml(marshallingXml());

        assertEquals(marshallingXml(), resource.toXml());
    }

    public void testMarshallingUnmarshallingResources() {
        if (resourcesXml() != null) {
            final ResourceCollection<T> resources = new ResourceCollection<T>(this.factory);
            resources.fromXml(resourcesXml());

            assertEquals(resourcesXml(), resources.toXml());
        }
    }

    abstract protected ResourceFactory<T> factorySetUp();

    abstract protected String resourcesXml();

    abstract protected int idValue();

    abstract protected String resource2Xml();

    abstract protected String marshallingXml();
}
