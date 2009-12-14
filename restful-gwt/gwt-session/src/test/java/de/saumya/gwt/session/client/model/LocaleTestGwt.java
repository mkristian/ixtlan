package de.saumya.gwt.session.client.model;

import de.saumya.gwt.persistence.client.AbstractResourceTestGwt;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class LocaleTestGwt extends AbstractResourceTestGwt<Locale> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Locale              locale;

    private static final String RESOURCE_XML = "<locale>"
                                                     + "<code>en</code>"
                                                     + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                     + "</locale>";

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML.replaceFirst("<created_at>[0-9-:. ]*</created_at>",
                                         "");
    }

    @Override
    protected Resource<Locale> resourceSetUp() {
        this.locale = this.factory.newResource();

        this.locale.code = "en";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.locale.save();

        return this.locale;
    }

    @Override
    public void doTestCreate() {
        assertEquals("en", this.locale.code);
    }

    @Override
    public void doTestUpdate() {
        this.locale.code = changedValue();
        this.locale.save();
        assertEquals(this.locale.code, changedValue());
    }

    @Override
    protected String changedValue() {
        return "en_JP";
    }

    @Override
    protected ResourceFactory<Locale> factorySetUp() {
        return new LocaleFactory(this.repository, this.notification);
    }

    @Override
    protected String keyValue() {
        return "en";
    }

    @Override
    protected String marshallingXml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">en<", ">fr<");
    }

    @Override
    protected String resourcesXml() {
        return "<locales>" + resource1Xml() + resource2Xml() + "</locales>";
    }

    @Override
    protected String value() {
        return "en";
    }
}
