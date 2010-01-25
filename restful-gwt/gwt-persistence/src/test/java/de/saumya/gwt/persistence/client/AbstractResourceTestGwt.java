package de.saumya.gwt.persistence.client;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
abstract public class AbstractResourceTestGwt<T extends Resource<T>> extends
        GWTTestCase {

    protected RepositoryMock                     repository;
    protected ResourceNotifications              notifications;
    protected CountingResourceChangeListener<T>  countingResourceListener;
    protected CountingResourcesChangeListener<T> countingResourcesListener;

    private Resource<T>                          resource;
    protected ResourceFactory<T>                 factory;

    @Override
    protected void gwtSetUp() {
        this.repository = new RepositoryMock();
        this.notifications = new GWTResourceNotification();
        this.factory = factorySetUp();

        this.countingResourceListener = new CountingResourceChangeListener<T>();
        this.countingResourcesListener = new CountingResourcesChangeListener<T>();

        this.resource = resourceSetUp();

        this.countingResourceListener.reset();
        this.countingResourcesListener.reset();
    }

    public void testCreate() {
        assertTrue(this.resource.isUptodate());
        doTestCreate();
        assertEquals(resourceNewXml(), this.repository.requests.get(0));
    }

    public void testRetrieve() {
        this.repository.addXmlResponse(resource1Xml());

        final Resource<T> rsrc = this.factory.get(keyValue(),
                                                  this.countingResourceListener);

        assertEquals(1, this.countingResourceListener.count());
        assertTrue(this.resource.isUptodate());
        assertEquals(this.resource.toString(), rsrc.toString());
    }

    public void testRetrieveAll() {
        this.repository.addXmlResponse(resourcesXml());

        final ResourceCollection<T> resources = this.factory.all(this.countingResourcesListener);

        assertEquals(2, this.countingResourcesListener.count());
        int id = 0;
        final String[] xmls = { resource1Xml(), resource2Xml() };
        for (final Resource<T> rsrc : resources) {
            assertTrue(this.resource.isUptodate());
            assertEquals(xmls[id++], rsrc.toXml());
        }
    }

    public void testUpdate() {
        final String xml = resource1Xml().replace(">" + value() + "<",
                                                  ">" + changedValue() + "<");

        this.repository.reset();
        this.repository.addXmlResponse(xml);

        doTestUpdate();

        assertEquals(xml, this.repository.requests.get(0));
        assertTrue(this.resource.isUptodate());
    }

    public void testDelete() {
        this.resource.destroy();

        assertTrue(this.resource.isDeleted());
    }

    public void testMarshallingUnmarshallingResource() {
        final Resource<T> resource = this.factory.newResource();
        resource.fromXml(marshallingXml());

        assertEquals(marshallingXml(), resource.toXml());
    }

    public void testMarshallingUnmarshallingResources() {
        final ResourceCollection<T> resources = new ResourceCollection<T>(this.factory);
        resources.fromXml(resourcesXml());

        assertEquals(resourcesXml(), resources.toXml());
    }

    protected String resourceNewXml() {
        return resource1Xml().replaceFirst("<id>[0-9]*</id>", "");
    }

    abstract protected String resource1Xml();

    abstract protected String resource2Xml();

    abstract protected String resourcesXml();

    abstract protected String marshallingXml();

    abstract protected String keyValue();

    abstract protected String value();

    abstract protected String changedValue();

    abstract protected void doTestCreate();

    abstract protected void doTestUpdate();

    abstract protected Resource<T> resourceSetUp();

    abstract protected ResourceFactory<T> factorySetUp();

}
