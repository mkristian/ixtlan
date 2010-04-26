package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceTestGwt;

public class GroupTestGwt extends ResourceTestGwt<Group> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Group               resource;

    private static final String RESOURCE_XML = "<group>"
                                                     + "<id>1</id>"
                                                     + "<name>root</name>"
                                                     + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                     + "</group>";

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
        return "<groups>" + resource1Xml() + resource2Xml() + "</groups>";
    }

    @Override
    protected ResourceFactory<Group> factorySetUp() {
        return new GroupFactory(this.repository, this.notifications);
    }

    @Override
    protected AbstractResource<Group> resourceSetUp() {
        this.resource = this.factory.newResource(1);

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

    private final static String XML = "<group>"
                                            + "<id>1</id>"
                                            + "<name>root</name>"
                                            + "<created_at>2005-07-09 17:14:48.0</created_at>"
                                            + "</group>";

    @Override
    protected String changedValue() {
        return "superuser";
    }

    @Override
    protected int idValue() {
        return 1;
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
