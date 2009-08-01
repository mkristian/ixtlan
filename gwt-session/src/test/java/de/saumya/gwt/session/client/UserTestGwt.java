package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class UserTestGwt extends AbstractResourceTestGwt<User> {

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private User                resource;
    private UserFactory         factory;
    private static final String RESOURCE_XML  = "<user>"
                                                      + "<login>root</login>"
                                                      + "<name>root</name>"
                                                      + "<email>root@example.com</email>"
                                                      + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                      + "</user>";

    private static final String RESOURCES_XML = "<users>"
                                                      + RESOURCE_XML
                                                      + RESOURCE_XML.replace(">root",
                                                                             ">admin")
                                                      + "</users>";

    protected void resourceSetUp() {
        LocaleFactory localeFactory = new LocaleFactory(repository);
        factory = new UserFactory(repository,
                localeFactory,
                new RoleFactory(repository,
                        localeFactory,
                        new VenueFactory(repository)));
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

        User rsrc = factory.get("root", countingResourceListener);

        assertEquals(1, countingResourceListener.count());
        assertTrue(resource.isUptodate());
        assertEquals(resource.toString(), rsrc.toString());
    }

    public void testRetrieveAll() {
        repository.addXmlResponse(RESOURCES_XML);

        Resources<User> resources = factory.all(countingResourcesListener);

        assertEquals(2, countingResourcesListener.count());
        int id = 0;
        String[] codes = { "root", "admin" };
        for (User rsrc : resources) {
            assertTrue(resource.isUptodate());
            assertEquals(resource.toXml().replace(">root",
                                                  ">" + codes[id++]),
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

    private final static String XML = "<user>"
                                            + "<login>root</login>"
                                            + "<name>root user</name>"
                                            + "<email>root@com</email>"
                                            + "<roles>"
                                            + "<role>"
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
                                            + "</role>" 
                                            + "</roles>"
                                            + "</user>";

    public void testMarshallingUnmarshallingResource() {
        Resource<User> resource = factory.newResource();
        resource.fromXml(XML);

        assertEquals(XML, resource.toXml());
    }

    public void testMarshallingUnmarshallingResources() {
        Resources<User> resources = new Resources<User>(factory);
        resources.fromXml(RESOURCES_XML);

        assertEquals(RESOURCES_XML, resources.toXml());
    }
}
