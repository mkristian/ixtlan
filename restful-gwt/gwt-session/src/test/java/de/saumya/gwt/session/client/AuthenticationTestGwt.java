package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.SingletonResourceFactory;
import de.saumya.gwt.persistence.client.SingletonResourceTestGwt;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.UserFactory;
import de.saumya.gwt.session.client.models.UserGroupFactory;

public class AuthenticationTestGwt extends
        SingletonResourceTestGwt<Authentication> {

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
                                                     + "<id>1</id>"
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
    protected AbstractResource<Authentication> resourceSetUp() {
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
    protected void doTestUpdate() {
        // obsolete
    }

    @Override
    protected String changedValue() {
        // obsolete
        return null;
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
    protected SingletonResourceFactory<Authentication> factorySetUp() {
        final LocaleFactory localeFactory = new LocaleFactory(this.repository,
                this.notifications);
        this.userFactory = new UserFactory(this.repository,
                this.notifications,
                localeFactory,
                new UserGroupFactory(this.repository,
                        this.notifications,
                        localeFactory,
                        new DomainFactory(this.repository, this.notifications)));
        return new AuthenticationFactory(this.repository,
                this.notifications,
                this.userFactory);
    }

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String value() {
        return "asdqwe";
    }

    @Override
    protected String marshallingXml() {
        return RESOURCE_XML;
    }
}
