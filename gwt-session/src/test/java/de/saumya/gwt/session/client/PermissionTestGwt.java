package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

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
    private PermissionFactory   factory;
    private static final String RESOURCE_XML  = "<permission>"
                                                      + "<resource_name>config</resource_name>"
                                                      + "<action>create</action>"
                                                      + "<roles><role>"
                                                      + "<name>admin</name>"
                                                      + "<created_at>2005-07-09 17:14:48.0</created_at>"
                                                      + "</role></roles>"
                                                      + "</permission>";

    private static final String RESOURCES_XML = "<permissions>"
                                                      + RESOURCE_XML
                                                      + RESOURCE_XML.replace(">create<",
                                                                             ">update<")
                                                      + "</permissions>";

    @Override
    protected void resourceSetUp() {
        this.factory = new PermissionFactory(this.repository,
                new RoleFactory(this.repository,
                        new LocaleFactory(this.repository),
                        new VenueFactory(this.repository)));
        this.resource = this.factory.newResource();

        this.resource.resourceName = "config";
        this.resource.action = "create";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();
    }

    @Override
    public void testCreate() {
        assertTrue(this.resource.isUptodate());
        assertEquals("config", this.resource.resourceName);
    }

    @Override
    public void testRetrieve() {
        this.repository.addXmlResponse(RESOURCE_XML);

        final Permission rsrc = this.factory.get("config",
                                                 this.countingResourceListener);

        assertEquals(1, this.countingResourceListener.count());
        assertTrue(this.resource.isUptodate());
        assertEquals(this.resource.toString(), rsrc.toString());
    }

    @Override
    public void testRetrieveAll() {
        this.repository.addXmlResponse(RESOURCES_XML);

        final Resources<Permission> resources = this.factory.all(this.countingResourcesListener);

        assertEquals(2, this.countingResourcesListener.count());
        int id = 0;
        final String[] codes = { "create", "update" };
        for (final Resource<Permission> rsrc : resources) {
            assertTrue(this.resource.isUptodate());
            assertEquals(this.resource.toXml().replace(">create<",
                                                       ">" + codes[id++] + "<"),
                         rsrc.toXml());
        }
    }

    @Override
    public void testUpdate() {
        this.resource.action = null;
        this.resource.save();

        // TODO should result in an error since they are immutable
        assertTrue(this.resource.isUptodate());
    }

    @Override
    public void testDelete() {
        this.resource.destroy();

        // TODO should result in an error since they are immutable
        assertTrue(this.resource.isDeleted());
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

    public void testMarshallingUnmarshallingResource() {
        final Resource<Permission> resource = this.factory.newResource();
        resource.fromXml(XML);

        assertEquals(XML, resource.toXml());
    }

    public void testMarshallingUnmarshallingResources() {
        final Resources<Permission> resources = new Resources<Permission>(this.factory);
        resources.fromXml(RESOURCES_XML);

        assertEquals(RESOURCES_XML, resources.toXml());
    }
}
