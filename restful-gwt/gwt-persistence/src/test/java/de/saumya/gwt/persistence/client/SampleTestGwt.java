package de.saumya.gwt.persistence.client;

public class SampleTestGwt extends ResourceTestGwt<Sample> {

    @Override
    public String getModuleName() {
        return "de.saumya.gwt.persistence.Persistence";
    }

    private Sample       sample;

    private final String RESOURCE_XML = "<sample>" + "<id>123</id>"
                                              + "<language>en</language>"
                                              + "<country>GE</country>"
                                              + "<child>" + "<id>1</id>"
                                              // + "<language>en</language>"
                                              // + "<country>AT</country>"
                                              + "</child>" + "</sample>";

    @Override
    protected ResourceFactory<Sample> factorySetUp() {
        return new SampleFactory(this.repository, new GWTResourceNotification());
    }

    @Override
    protected AbstractResource<Sample> resourceSetUp() {
        this.sample = this.factory.newResource(123);

        this.sample.country = "GE";
        this.sample.language = "en";
        this.sample.child = this.factory.newResource(1);

        this.repository.addXmlResponse(resource1Xml());

        this.sample.save();

        return this.sample;
    }

    @Override
    public void doTestCreate() {
        assertEquals(123, this.sample.id);
    }

    @Override
    protected void doTestUpdate() {
        this.sample.country = changedValue();

        this.sample.save();

        assertEquals(changedValue(), this.sample.country);
    }

    @Override
    protected String changedValue() {
        return "DE";
    }

    @Override
    protected int idValue() {
        return 123;
    }

    @Override
    protected String marshallingXml() {
        return this.RESOURCE_XML;
    }

    @Override
    protected String resource1Xml() {
        return this.RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return this.RESOURCE_XML.replace("123", "124");
    }

    @Override
    protected String resourcesXml() {
        return "<samples>" + resource1Xml() + resource2Xml() + "</samples>";
    }

    @Override
    protected String value() {
        return "GE";
    }
}
