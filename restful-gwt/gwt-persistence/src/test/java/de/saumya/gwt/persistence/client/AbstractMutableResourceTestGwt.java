package de.saumya.gwt.persistence.client;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
abstract public class AbstractMutableResourceTestGwt<T extends AbstractResource<T>>
        extends AbstractResourceTestGwt<T> {

    public void testCreate() {
        assertTrue(this.resource.isUptodate());
        doTestCreate();
        assertEquals(resourceNewXml(), this.repository.requests.get(0));
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

    protected String resourceNewXml() {
        return resource1Xml().replaceFirst("<id>[0-9]*</id>", "");
    }

    abstract protected String value();

    abstract protected String changedValue();

    abstract protected void doTestCreate();

    abstract protected void doTestUpdate();
}
