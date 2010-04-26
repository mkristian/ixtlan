package de.saumya.gwt.persistence.client;

public class SingletonTestGwt extends SingletonResourceTestGwt<Singleton> {

    @Override
    public String getModuleName() {
        return "de.saumya.gwt.persistence.Persistence";
    }

    private Singleton           singleton;

    private final static String RESOURCE_XML = "<singleton>"
                                                     + "<name>god</name>"
                                                     + "</singleton>";

    @Override
    protected SingletonResourceFactory<Singleton> factorySetUp() {
        return new SingletonFactory(this.repository,
                new GWTResourceNotification());
    }

    @Override
    protected AbstractResource<Singleton> resourceSetUp() {
        this.singleton = this.factory.newResource();

        this.singleton.name = "god";

        this.repository.addXmlResponse(resource1Xml());

        this.singleton.save();

        return this.singleton;
    }

    @Override
    public void doTestCreate() {
        assertEquals("god", this.singleton.name);
    }

    @Override
    protected void doTestUpdate() {
        this.singleton.name = changedValue();

        this.singleton.save();

        assertEquals(changedValue(), this.singleton.name);
    }

    @Override
    protected String changedValue() {
        return "angel";
    }

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String value() {
        return "god";
    }

    @Override
    protected String marshallingXml() {
        return RESOURCE_XML;
    }
}
