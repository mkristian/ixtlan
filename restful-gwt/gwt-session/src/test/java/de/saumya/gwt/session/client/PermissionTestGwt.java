package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.AbstractResourceTestGwt;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class PermissionTestGwt extends AbstractResourceTestGwt<Permission> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Permission          resource;

    private RoleFactory         roleFactory;

    private static final String RESOURCE_XML = "<permission>"
                                                     + "<resource_name>config</resource_name>"
                                                     + "<action>create</action>"
                                                     + "<roles><role>"
                                                     + "<name>admin</name>"
                                                     + "</role></roles>"
                                                     + "</permission>";

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML.replaceFirst("<created_at>[0-9-:. ]*</created_at>",
                                         "");
    }

    @Override
    protected Resource<Permission> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.resource = "config";
        this.resource.action = "create";
        final Role role = this.roleFactory.newResource();
        role.name = "admin";
        this.resource.roles = this.roleFactory.newResources();
        this.resource.roles.add(role);

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("config", this.resource.resource);
        assertEquals("create", this.resource.action);
    }

    @Override
    public void doTestUpdate() {
        this.resource.action = changedValue();
        this.resource.save();
        assertEquals(this.resource.action, changedValue());
    }

    private final static String XML = "<permission>"
                                            + "<resource_name>config</resource_name>"
                                            + "<action>create</action>"
                                            + "<roles>" + "<role>"
                                            + "<name>root</name>" + "</role>"
                                            + "</roles>" + "</permission>";

    @Override
    protected String changedValue() {
        return "doit";
    }

    @Override
    protected ResourceFactory<Permission> factorySetUp() {
        this.roleFactory = new RoleFactory(this.repository);
        return new PermissionFactory(this.repository, this.roleFactory);
    }

    @Override
    protected String keyValue() {
        return null;
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">create<", ">update<");
    }

    @Override
    protected String resourcesXml() {
        return "<permissions>" + resource1Xml() + resource2Xml()
                + "</permissions>";
    }

    @Override
    protected String value() {
        return "create";
    }
}
