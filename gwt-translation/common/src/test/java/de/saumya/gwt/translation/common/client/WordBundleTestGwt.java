package de.saumya.gwt.translation.common.client;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class WordBundleTestGwt extends AbstractResourceTestGwt<WordBundle> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.translation.common.CommonTest";
    }

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
                                                     + "<words>" + "</words>"
                                                     + "</word_bundle>";

    @Override
    protected WordBundleFactory factorySetUp() {
        return new WordBundleFactory(this.repository,
                new WordFactory(this.repository));
    }

    @Override
    protected Resource<WordBundle> resourceSetUp() {
        this.resource = this.factory.newResource();

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
        return "en";
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
