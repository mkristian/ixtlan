package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.AbstractResourceTestGwt;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.GroupFactory;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.UserFactory;

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
                                                     + "<id>1</id>"
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
        return XML.replace("<id>1</id>", "");
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

        assertEquals("<authentication><id>0</id><login>asd</login><password>pwd</password></authentication>",
                     resource.toXml());
    }

    @Override
    protected String changedValue() {
        return "newtoken";
    }

    @Override
    protected ResourceFactory<Authentication> factorySetUp() {
        final LocaleFactory localeFactory = new LocaleFactory(this.repository,
                this.notifications);
        this.userFactory = new UserFactory(this.repository,
                this.notifications,
                localeFactory,
                new GroupFactory(this.repository,
                        this.notifications,
                        localeFactory,
                        new DomainFactory(this.repository, this.notifications)));
        return new AuthenticationFactory(this.repository,
                this.notifications,
                this.userFactory);
    }

    @Override
    protected String keyValue() {
        return "1";
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
