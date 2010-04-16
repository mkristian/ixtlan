/**
 * 
 */
package de.saumya.gwt.session.client.model;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.session.client.AbstractUserResourceTestGwt;
import de.saumya.gwt.session.client.models.Configuration;
import de.saumya.gwt.session.client.models.ConfigurationFactory;
import de.saumya.gwt.session.client.models.LocaleFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class ConfigurationTestGwt extends
        AbstractUserResourceTestGwt<Configuration> {

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
                                                     + "<locales></locales>"
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
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">1<", ">2<");
    }

    @Override
    protected String resourcesXml() {
        return null;
    }

    @Override
    protected ResourceFactory<Configuration> factorySetUp() {
        return new ConfigurationFactory(this.repository,
                this.notifications,
                this.userFactory,
                new LocaleFactory(this.repository, this.notifications));
    }

    @Override
    protected Resource<Configuration> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.sessionIdleTimeout = 1;

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
                                            + "<locales></locales>"
                                            + "<updated_at>2007-07-09 17:14:48.0</updated_at>"
                                            + "</configuration>";

    @Override
    protected String changedValue() {
        return "2";
    }

    @Override
    protected String keyValue() {
        return null;
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

    @Override
    protected String value() {
        return "1";
    }
}
