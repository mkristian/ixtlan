package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceTestGwt;

public class DomainTestGwt extends ResourceTestGwt<Domain> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Domain              resource;

    private static final String RESOURCE_XML = "<domain>"
                                                     + "<id>1</id>"
                                                     + "<name>dhara</name>"
                                                     + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                     + "</domain>";

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML.replaceFirst("<created_at>[0-9-:. ]*</created_at>",
                                         "").replace("<id>1</id>", "");
    }

    @Override
    protected AbstractResource<Domain> resourceSetUp() {
        this.resource = this.factory.newResource(1);

        this.resource.name = "dhara";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("dhara", this.resource.name);
    }

    @Override
    public void doTestUpdate() {
        this.resource.name = changedValue();
        this.resource.save();

        assertEquals(this.resource.name, changedValue());
    }

    @Override
    protected String changedValue() {
        return "dvara";
    }

    @Override
    protected ResourceFactory<Domain> factorySetUp() {
        return new DomainFactory(this.repository, this.notifications);
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
        return RESOURCE_XML.replace(">dhara<", ">dvara<").replace(">1<", ">2<");
    }

    @Override
    protected String resourcesXml() {
        return "<domains>" + resource1Xml() + resource2Xml() + "</domains>";
    }

    @Override
    protected String value() {
        return "dhara";
    }

}
