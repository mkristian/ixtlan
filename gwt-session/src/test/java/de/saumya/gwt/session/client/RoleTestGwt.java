package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class RoleTestGwt extends AbstractResourceTestGwt<Role> {

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Role                resource;
    private RoleFactory         factory;
    private static final String RESOURCE_XML  = "<role>"
                                                      + "<name>root</name>"
                                                      + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                      + "</role>";

    private static final String RESOURCES_XML = "<roles>"
                                                      + RESOURCE_XML
                                                      + RESOURCE_XML.replace(">root<",
                                                                             ">admin<")
                                                      + "</roles>";

    protected void resourceSetUp() {
        factory = new RoleFactory(repository,
                new LocaleFactory(repository),
                new VenueFactory(repository));
        resource = factory.newResource();

        resource.name = "root";

        repository.addXmlResponse(RESOURCE_XML);

        resource.save();
    }

    public void testCreate() {
        assertTrue(resource.isUptodate());
        assertEquals("root", resource.name);
    }

    public void testRetrieve() {
        repository.addXmlResponse(RESOURCE_XML);

        Role rsrc = factory.get("root", countingResourceListener);

        assertEquals(1, countingResourceListener.count());
        assertTrue(resource.isUptodate());
        assertEquals(resource.toString(), rsrc.toString());
    }

    public void testRetrieveAll() {
        repository.addXmlResponse(RESOURCES_XML);

        Resources<Role> resources = factory.all(countingResourcesListener);

        assertEquals(2, countingResourcesListener.count());
        int id = 0;
        String[] codes = { "root", "admin" };
        for (Resource<Role> rsrc : resources) {
            assertTrue(resource.isUptodate());
            assertEquals(resource.toXml().replace(">root<",
                                                  ">" + codes[id++] + "<"),
                         rsrc.toXml());
        }
    }

    public void testUpdate() {
        resource.name = null;
        resource.save();

        // TODO should result in an error since they are immutable
        assertTrue(resource.isUptodate());
    }

    public void testDelete() {
        this.resource.destroy();

        // TODO should result in an error since they are immutable
        assertTrue(resource.isDeleted());
    }

    private final static String XML = "<role>"
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

    public void testMarshallingUnmarshallingResource() {
        Resource<Role> resource = factory.newResource();
        resource.fromXml(XML);

        assertEquals(XML, resource.toXml());
    }

    public void testMarshallingUnmarshallingResources() {
        Resources<Role> resources = new Resources<Role>(factory);
        resources.fromXml(RESOURCES_XML);

        assertEquals(RESOURCES_XML, resources.toXml());
    }
}
