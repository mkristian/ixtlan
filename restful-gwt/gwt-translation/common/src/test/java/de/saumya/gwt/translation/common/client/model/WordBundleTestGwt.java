package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Resource;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class WordBundleTestGwt extends AbstractCommonTestGwt<WordBundle> {

    private WordBundle resource;

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">en<", ">en_GE<");
    }

    @Override
    protected String resourcesXml() {
        return "<word_bundles>" + resource1Xml() + resource2Xml()
                + "</word_bundles>";
    }

    private static final String RESOURCE_XML = "<word_bundle>"
                                                     + "<locale>en</locale>"
                                                     + "<words></words>"
                                                     + "</word_bundle>";

    @Override
    protected WordBundleFactory factorySetUp() {
        return new WordBundleFactory(this.repository,
                this.notifications,
                new WordFactory(this.repository, this.notifications));
    }

    @Override
    protected Resource<WordBundle> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.id = 1;
        this.resource.locale = "en";
        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();
        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("en", this.resource.locale);
    }

    @Override
    protected String keyValue() {
        return "1";
    }

    @Override
    protected String value() {
        return "en";
    }

    @Override
    protected String changedValue() {
        return "en_JP";
    }

    @Override
    protected void doTestUpdate() {
        this.resource.locale = changedValue();
        this.resource.save();
        assertEquals(changedValue(), this.resource.locale);
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

    private static final String XML = "<word_bundle>" + "<locale>en</locale>"
                                            + "<words>" + "<word>"
                                            + "<code>CODE</code>"
                                            + "<text>code</text>" + "</word>"
                                            + "</words>" + "</word_bundle>";

}
