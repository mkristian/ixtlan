package de.saumya.gwt.session.client.model;

import de.saumya.gwt.persistence.client.AbstractResourceTestGwt;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.GroupFactory;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.session.client.models.UserFactory;

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

    private static final String RESOURCE_XML = "<user>"
                                                     + "<id>1</id>"
                                                     + "<login>root</login>"
                                                     + "<name>root user</name>"
                                                     + "<email>root@example.com</email>"
                                                     + "<groups></groups>"
                                                     + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                     + "</user>";

    @Override
    protected Resource<User> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.id = 1;
        this.resource.login = "root";
        this.resource.name = "root user";
        this.resource.email = "root@example.com";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML.replaceFirst("<created_at>[0-9-:. ]*</created_at>",
                                         "").replace("<id>1</id>", "");
    }

    @Override
    public void doTestCreate() {
        assertEquals("root user", this.resource.name);
    }

    @Override
    public void doTestUpdate() {
        this.resource.name = changedValue();
        this.resource.save();
        assertEquals(this.resource.name, changedValue());
    }

    private final static String XML = "<user>"
                                            + "<id>1</id>"
                                            + "<login>root</login>"
                                            + "<name>root user</name>"
                                            + "<email>root@com</email>"
                                            + "<groups>"
                                            + "<group>"
                                            + "<id>1</id>"
                                            + "<name>admin</name>"
                                            + "<locales>"
                                            + "<locale>"
                                            + "<id>0</id>"
                                            + "<code>en</code>"
                                            + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                            + "</locale>"
                                            + "</locales>"
                                            + "<domains></domains>"
                                            + "</group>"
                                            + "<group>"
                                            + "<id>2</id>"
                                            + "<name>root</name>"
                                            + "<locales>"
                                            + "<locale>"
                                            + "<id>0</id>"
                                            + "<code>en</code>"
                                            + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                            + "</locale>"
                                            + "<locale>"
                                            + "<id>0</id>"
                                            + "<code>en</code>"
                                            + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                            + "</locale>"
                                            + "</locales>"
                                            + "<domains>"
                                            + "<domain>"
                                            + "<id>1</id>"
                                            + "<name>dvara</name>"
                                            + "<created_at>2007-07-09 17:14:48.0</created_at>"
                                            + "</domain>"
                                            + "</domains>"
                                            + "<created_at>2005-07-09 17:14:48.0</created_at>"
                                            + "</group>" + "</groups>"
                                            + "</user>";

    public void testAllowedLocales() {
        final User resource = this.factory.newResource();
        resource.fromXml(XML);

        assertEquals(1, resource.getAllowedLocales().size());
    }

    @Override
    protected String changedValue() {
        return "super user";
    }

    @Override
    protected ResourceFactory<User> factorySetUp() {
        final LocaleFactory localeFactory = new LocaleFactory(this.repository,
                this.notifications);
        return new UserFactory(this.repository,
                this.notifications,
                localeFactory,
                new GroupFactory(this.repository,
                        this.notifications,
                        localeFactory,
                        new DomainFactory(this.repository, this.notifications)));
    }

    @Override
    protected String keyValue() {
        return "1";
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
        return RESOURCE_XML.replace(">root<", ">admin<").replace(">1<", ">2<");
    }

    @Override
    protected String resourcesXml() {
        return "<users>" + resource1Xml() + resource2Xml() + "</users>";
    }

    @Override
    protected String value() {
        return "root user";
    }
}
