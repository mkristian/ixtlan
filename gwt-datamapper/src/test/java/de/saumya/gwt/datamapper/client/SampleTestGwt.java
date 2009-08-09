package de.saumya.gwt.datamapper.client;

public class SampleTestGwt extends AbstractResourceTestGwt<Sample> {

    @Override
    public String getModuleName() {
        return "de.saumya.gwt.datamapper.Datamapper";
    }

    private Sample       locale;

    private final String RESOURCE_XML = "<locale>" + "<id>123</id>"
                                              + "<language>en</language>"
                                              + "<country>GE</country>"
                                              + "</locale>";

    @Override
    protected ResourceFactory<Sample> factorySetUp() {
        return new SampleFactory(this.repository);
    }

    @Override
    protected Resource<Sample> resourceSetUp() {
        this.locale = this.factory.newResource();

        this.locale.country = "GE";
        this.locale.language = "en";

        this.repository.addXmlResponse(resource1Xml());

        this.locale.save();

        return this.locale;
    }

    @Override
    public void doTestCreate() {
        assertEquals(123, this.locale.id);
    }

    @Override
    protected void doTestUpdate() {
        this.locale.country = changedValue();

        this.locale.save();

        assertEquals(changedValue(), this.locale.country);
    }

    @Override
    protected String changedValue() {
        return "DE";
    }

    @Override
    protected String keyValue() {
        return "123";
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
        return "<locales>" + resource1Xml() + resource2Xml() + "</locales>";
    }

    @Override
    protected String value() {
        return "GE";
    }
}
