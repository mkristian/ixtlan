/**
 *
 */
package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.SingletonResourceFactory;
import de.saumya.gwt.session.client.AbstractUserSingletonResourceTestGwt;

public class ConfigurationTestGwt extends
        AbstractUserSingletonResourceTestGwt<Configuration> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Configuration       resource;

    private static final String RESOURCE_XML = "<configuration>"
                                                     + "<session_idle_timeout>1</session_idle_timeout>"
                                                     + "<keep_audit_logs>0</keep_audit_logs>"
                                                     + "<password_sender_email>password@email.com</password_sender_email>"
                                                     + "<login_url>example.com</login_url>"
                                                     + "<errors_dump_directory>log/errors</errors_dump_directory>"
                                                     + "<logfiles_directory>log</logfiles_directory>"
                                                     + "<locales>"
                                                     + "</locales>"
                                                     + "<updated_at>2009-07-09 17:14:48.0</updated_at>"
                                                     + "</configuration>";

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML.replaceFirst("<updated_at>[0-9-:. ]*</updated_at>",
                                         "");
    }

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected SingletonResourceFactory<Configuration> factorySetUp() {
        return new ConfigurationFactory(this.repository,
                this.notifications,
                this.userFactory,
                new LocaleFactory(this.repository, this.notifications));
    }

    @Override
    protected AbstractResource<Configuration> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.sessionIdleTimeout = 1;
        this.resource.passwordSenderEmail = "password@email.com";
        this.resource.loginUrl = "example.com";
        this.resource.errorsDumpDirectory = "log/errors";
        this.resource.logfilesDirectory = "log";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("1", this.resource.sessionIdleTimeout + "");
    }

    @Override
    public void doTestUpdate() {
        this.resource.sessionIdleTimeout = Integer.parseInt(changedValue());
        this.resource.save();
        assertEquals(this.resource.sessionIdleTimeout + "", changedValue());
    }

    private final static String XML = "<configuration>"
                                            + "<session_idle_timeout>1</session_idle_timeout>"
                                            + "<keep_audit_logs>0</keep_audit_logs>"
                                            + "<locales>"
                                            + "<locale>"
                                            + "<id>20</id>"
                                            + "<code>de</code>"
                                            + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                            + "</locale>"
                                            + "</locales>"
                                            + "<updated_at>2007-07-09 17:14:48.0</updated_at>"
                                            + "</configuration>";

    @Override
    protected String changedValue() {
        return "2";
    }

    @Override
    protected String value() {
        return "1";
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }
}
