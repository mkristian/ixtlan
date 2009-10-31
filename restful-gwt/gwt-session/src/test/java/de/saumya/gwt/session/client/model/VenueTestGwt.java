package de.saumya.gwt.session.client.model;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.model.Venue;
import de.saumya.gwt.session.client.model.VenueFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class VenueTestGwt extends AbstractResourceTestGwt<Venue> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Venue               resource;

    private static final String RESOURCE_XML = "<venue>"
                                                     + "<id>dhara</id>"
                                                     + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                     + "</venue>";

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML.replaceFirst("<created_at>[0-9-:. ]*</created_at>",
                                         "");
    }

    @Override
    protected Resource<Venue> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.id = "dhara";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("dhara", this.resource.id);
    }

    @Override
    public void doTestUpdate() {
        this.resource.id = changedValue();
        this.resource.save();

        assertEquals(this.resource.id, changedValue());
    }

    @Override
    protected String changedValue() {
        return "dvara";
    }

    @Override
    protected ResourceFactory<Venue> factorySetUp() {
        return new VenueFactory(this.repository);
    }

    @Override
    protected String keyValue() {
        return "dhara";
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
        return RESOURCE_XML.replace(">dhara<", ">dvara<");
    }

    @Override
    protected String resourcesXml() {
        return "<venues>" + resource1Xml() + resource2Xml() + "</venues>";
    }

    @Override
    protected String value() {
        return "dhara";
    }

}