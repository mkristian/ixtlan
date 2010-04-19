package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.AbstractResourceTestGwt;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class RoleTestGwt extends AbstractResourceTestGwt<Role> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Role                resource;

    private static final String RESOURCE_XML = "<role>" + "<name>root</name>"
                                                     + "</role>";

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">root<", ">admin<");
    }

    @Override
    protected String resourcesXml() {
        return "<roles>" + resource1Xml() + resource2Xml() + "</roles>";
    }

    @Override
    protected ResourceFactory<Role> factorySetUp() {
        return new RoleFactory(this.repository, this.notifications);
    }

    @Override
    protected Resource<Role> resourceSetUp() {
        this.resource = this.factory.newResource();

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

    @Override
    protected String changedValue() {
        return "superuser";
    }

    @Override
    protected String keyValue() {
        return "1";
    }

    @Override
    protected String marshallingXml() {
        return RESOURCE_XML;
    }

    @Override
    protected String value() {
        return "root";
    }
}
