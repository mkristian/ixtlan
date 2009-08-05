package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resources;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class AuthenticationTestGwt extends
        AbstractResourceTestGwt<Authentication> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Authentication        resource;
    private AuthenticationFactory factory;
    private static final String   RESOURCE_XML  = "<authentication>"
                                                        + "<token>asdqwe</token>"
                                                        + "<user>"
                                                        + "<login>root</login>"
                                                        + "<name>root</name>"
                                                        + "<email>root@example.com</email>"
                                                        + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                        + "</user>"
                                                        + "</authentication>";

    private static final String   RESOURCES_XML = "<authentications>"
                                                        + RESOURCE_XML
                                                        + RESOURCE_XML.replace(">asdqwe",
                                                                               ">1234567890")
                                                        + "</authentications>";

    @Override
    protected void resourceSetUp() {
        final LocaleFactory localeFactory = new LocaleFactory(this.repository);
        final UserFactory userFactory = new UserFactory(this.repository,
                localeFactory,
                new RoleFactory(this.repository,
                        localeFactory,
                        new VenueFactory(this.repository)));
        this.factory = new AuthenticationFactory(this.repository, userFactory);
        this.resource = this.factory.newResource();

        this.resource.token = "asdqwe";
        this.resource.user = userFactory.newResource();

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();
    }

    @Override
    public void testCreate() {
        assertTrue(this.resource.isUptodate());
        assertEquals("asdqwe", this.resource.token);
    }

    @Override
    public void testRetrieve() {
        this.repository.addXmlResponse(RESOURCE_XML);

        final Authentication rsrc = this.factory.get("root",
                                                     this.countingResourceListener);

        assertEquals(1, this.countingResourceListener.count());
        assertTrue(this.resource.isUptodate());
        assertEquals(this.resource.toString(), rsrc.toString());
    }

    @Override
    public void testRetrieveAll() {
        this.repository.addXmlResponse(RESOURCES_XML);

        final Resources<Authentication> resources = this.factory.all(this.countingResourcesListener);

        assertEquals(2, this.countingResourcesListener.count());
        int id = 0;
        final String[] codes = { "root", "admin" };
        for (final Authentication rsrc : resources) {
            assertTrue(this.resource.isUptodate());
            assertEquals(this.resource.toXml().replace(">root",
                                                       ">" + codes[id++]),
                         rsrc.toXml());
        }
    }

    @Override
    public void testUpdate() {
        this.resource.token = null;
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

    public void testMarshallingUnmarshallingResource() {
        final Authentication resource = this.factory.newResource();
        resource.login = "asd";
        resource.password = "pwd";

        assertEquals("<authentication><login>asd</login><password>pwd</password></authentication>",
                     resource.toXml());
    }

}
