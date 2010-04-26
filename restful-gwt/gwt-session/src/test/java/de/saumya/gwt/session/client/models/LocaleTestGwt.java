package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceTestGwt;

public class LocaleTestGwt extends ResourceTestGwt<Locale> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Locale              locale;

    private static final String RESOURCE_XML = "<locale>"
                                                     + "<id>1</id>"
                                                     + "<code>en</code>"
                                                     + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                     + "</locale>";

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML.replaceFirst("<created_at>[0-9-:. ]*</created_at>",
                                         "").replace("<id>1</id>", "");
    }

    @Override
    protected AbstractResource<Locale> resourceSetUp() {
        this.locale = ((LocaleFactory) this.factory).newResource(1);

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
        // is immutable
        // this.locale.code = changedValue();
        this.locale.save();
        assertEquals(this.locale.code, this.locale.code);
    }

    @Override
    protected String changedValue() {
        // is immutable
        return "en";
    }

    @Override
    protected ResourceFactory<Locale> factorySetUp() {
        return new LocaleFactory(this.repository, this.notifications);
    }

    @Override
    protected int idValue() {
        return 1;
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
        return RESOURCE_XML.replace(">en<", ">fr<").replace(">1<", ">2<");
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
