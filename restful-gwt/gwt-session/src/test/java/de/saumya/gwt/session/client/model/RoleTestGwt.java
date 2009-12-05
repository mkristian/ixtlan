package de.saumya.gwt.session.client.model;

import de.saumya.gwt.persistence.client.AbstractResourceTestGwt;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class RoleTestGwt extends AbstractResourceTestGwt<Group> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Group                resource;

    private static final String RESOURCE_XML = "<role>"
                                                     + "<id>1</id>"
                                                     + "<name>root</name>"
                                                     + "<locales></locales>"
                                                     + "<venues></venues>"
                                                     + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                     + "</role>";

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML.replaceFirst("<created_at>[0-9-:. ]*</created_at>",
                                         "").replace("<id>1</id>", "");
    }

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">root<", ">admin<").replace(">1<", ">2<");
    }

    @Override
    protected String resourcesXml() {
        return "<roles>" + resource1Xml() + resource2Xml() + "</roles>";
    }

    @Override
    protected ResourceFactory<Group> factorySetUp() {
        return new GroupFactory(this.repository,
                new LocaleFactory(this.repository),
                new VenueFactory(this.repository));
    }

    @Override
    protected Resource<Group> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.id = 1;
        this.resource.name = "root";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("root", this.resource.name);
    }

    @Override
    public void doTestUpdate() {
        this.resource.name = changedValue();
        this.resource.save();
        assertEquals(this.resource.name, changedValue());
    }

    private final static String XML = "<role>"
                                            + "<id>1</id>"
                                            + "<name>root</name>"
                                            + "<locales>"
                                            + "<locale>"
                                            + "<code>de</code>"
                                            + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                            + "</locale>"
                                            + "</locales>"
                                            + "<venues>"
                                            + "<venue>"
                                            + "<id>dvara</id>"
                                            + "<created_at>2007-07-09 17:14:48.0</created_at>"
                                            + "</venue>"
                                            + "</venues>"
                                            + "<created_at>2005-07-09 17:14:48.0</created_at>"
                                            + "</role>";

    @Override
    protected String changedValue() {
        return "superuser";
    }

    @Override
    protected String keyValue() {
        return "root";
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

    @Override
    protected String value() {
        return "root";
    }
}
