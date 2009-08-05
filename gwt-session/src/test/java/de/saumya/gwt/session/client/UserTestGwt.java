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
    @Override
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

    @Override
    protected void resourceSetUp() {
        final LocaleFactory localeFactory = new LocaleFactory(this.repository);
        this.factory = new UserFactory(this.repository,
                localeFactory,
                new RoleFactory(this.repository,
                        localeFactory,
                        new VenueFactory(this.repository)));
        this.resource = this.factory.newResource();

        this.resource.name = "root";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();
    }

    @Override
    public void testCreate() {
        assertTrue(this.resource.isUptodate());
        assertEquals("root", this.resource.name);
    }

    @Override
    public void testRetrieve() {
        this.repository.addXmlResponse(RESOURCE_XML);

        final User rsrc = this.factory.get("root",
                                           this.countingResourceListener);

        assertEquals(1, this.countingResourceListener.count());
        assertTrue(this.resource.isUptodate());
        assertEquals(this.resource.toString(), rsrc.toString());
    }

    @Override
    public void testRetrieveAll() {
        this.repository.addXmlResponse(RESOURCES_XML);

        final Resources<User> resources = this.factory.all(this.countingResourcesListener);

        assertEquals(2, this.countingResourcesListener.count());
        int id = 0;
        final String[] codes = { "root", "admin" };
        for (final User rsrc : resources) {
            assertTrue(this.resource.isUptodate());
            assertEquals(this.resource.toXml().replace(">root",
                                                       ">" + codes[id++]),
                         rsrc.toXml());
        }
    }

    @Override
    public void testUpdate() {
        this.resource.name = null;
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

    private final static String XML = "<user>"
                                            + "<login>root</login>"
                                            + "<name>root user</name>"
                                            + "<email>root@com</email>"
                                            + "<roles>"
                                            + "<role>"
                                            + "<name>admin</name>"
                                            + "<locales>"
                                            + "<locale>"
                                            + "<code>de</code>"
                                            + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                            + "</locale>"
                                            + "</locales>"
                                            + "</role>"
                                            + "<role>"
                                            + "<name>root</name>"
                                            + "<locales>"
                                            + "<locale>"
                                            + "<code>de</code>"
                                            + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                            + "</locale>"
                                            + "<locale>"
                                            + "<code>en</code>"
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
                                            + "</role>" + "</roles>"
                                            + "</user>";

    public void testMarshallingUnmarshallingResource() {
        final Resource<User> resource = this.factory.newResource();
        resource.fromXml(XML);

        assertEquals(XML, resource.toXml());
    }

    public void testMarshallingUnmarshallingResources() {
        final Resources<User> resources = new Resources<User>(this.factory);
        resources.fromXml(RESOURCES_XML);

        assertEquals(RESOURCES_XML, resources.toXml());
    }

    public void testAllowedLocales() {
        final User resource = this.factory.newResource();
        resource.fromXml(XML);

        assertEquals(2, resource.getAllowedLocales().size());
    }
}
