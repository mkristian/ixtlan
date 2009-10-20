package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.model.LocaleFactory;
import de.saumya.gwt.session.client.model.RoleFactory;
import de.saumya.gwt.session.client.model.UserFactory;
import de.saumya.gwt.session.client.model.VenueFactory;

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

    private Authentication      resource;
    private UserFactory         userFactory;

    private static final String RESOURCE_XML = "<authentication>"
                                                     + "<token>asdqwe</token>"
                                                     + "<user>"
                                                     + "<login>root</login>"
                                                     + "<name>root</name>"
                                                     + "<email>root@example.com</email>"
                                                     + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                     + "</user>"
                                                     + "</authentication>";

    private static final String XML          = "<authentication><login>root</login><password>pwd</password></authentication>";

    @Override
    protected String resourceNewXml() {
        return XML;
    }

    @Override
    protected Resource<Authentication> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.login = "root";
        this.resource.password = "pwd";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("asdqwe", this.resource.token);
    }

    @Override
    public void testUpdate() {
        // not applicable
    }

    @Override
    public void testRetrieveAll() {
        // not applicable
    }

    @Override
    public void testMarshallingUnmarshallingResources() {
        // not applicable
    }

    @Override
    public void testMarshallingUnmarshallingResource() {
        final Authentication resource = this.factory.newResource();
        resource.login = "asd";
        resource.password = "pwd";

        assertEquals("<authentication><login>asd</login><password>pwd</password></authentication>",
                     resource.toXml());
    }

    @Override
    protected String changedValue() {
        return "newtoken";
    }

    @Override
    protected ResourceFactory<Authentication> factorySetUp() {
        final LocaleFactory localeFactory = new LocaleFactory(this.repository);
        this.userFactory = new UserFactory(this.repository,
                localeFactory,
                new RoleFactory(this.repository,
                        localeFactory,
                        new VenueFactory(this.repository)));
        return new AuthenticationFactory(this.repository, this.userFactory);
    }

    @Override
    protected String keyValue() {
        return "asdqwe";
    }

    @Override
    protected String marshallingXml() {
        // obsolete
        return null;
    }

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">asdqwe", ">1234567890");
    }

    @Override
    protected String resourcesXml() {
        return "<authentications>" + resource1Xml() + resource2Xml()
                + "</authentications>";
    }

    @Override
    protected String value() {
        return "asdqwe";
    }

    @Override
    protected void doTestUpdate() {
        // obsolete
    }

}
