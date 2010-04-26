package de.saumya.gwt.persistence.client;

abstract public class SingletonResourceTestGwt<T extends SingletonResource<T>>
        extends AbstractMutableResourceTestGwt<T> {

    protected SingletonResourceFactory<T> factory;

    @Override
    protected void gwtSetUp() {
        this.repository = new RepositoryMock();
        this.factory = factorySetUp();
        gwtSetUp(this.factory);
    }

    public void testRetrieve() {
        this.repository.addXmlResponse(resource1Xml());

        final AbstractResource<T> rsrc = this.factory.get(this.countingResourceListener);

        assertEquals(1, this.countingResourceListener.count());
        assertTrue(this.resource.isUptodate());
        assertEquals(this.resource.toString(), rsrc.toString());
    }

    public void testMarshallingUnmarshallingResource() {
        final AbstractResource<T> resource = this.factory.newResource();
        resource.fromXml(resource1Xml());

        assertEquals(resource1Xml(), resource.toXml());
    }

    abstract protected SingletonResourceFactory<T> factorySetUp();

    abstract protected String marshallingXml();
}
