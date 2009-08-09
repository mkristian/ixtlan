package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;

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
                                                     + "<created_at>2005-07-09 17:14:48.0</created_at>"
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

        this.resource.resourceName = "config";
        this.resource.action = "create";
        final Role role = this.roleFactory.newResource();
        role.name = "admin";
        this.resource.roles.add(role);

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("config", this.resource.resourceName);
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
                                            + "<roles>"
                                            + "<role>"
                                            + "<name>root</name>"
                                            + "<created_at>2005-07-09 17:14:48.0</created_at>"
                                            + "</role>" + "</roles>"
                                            + "</permission>";

    @Override
    protected String changedValue() {
        return "doit";
    }

    @Override
    protected ResourceFactory<Permission> factorySetUp() {
        this.roleFactory = new RoleFactory(this.repository,
                new LocaleFactory(this.repository),
                new VenueFactory(this.repository));
        return new PermissionFactory(this.repository, this.roleFactory);
    }

    @Override
    protected String keyValue() {
        // TODO Auto-generated method stub
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
