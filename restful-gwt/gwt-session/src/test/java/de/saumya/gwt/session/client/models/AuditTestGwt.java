/**
 *
 */
package de.saumya.gwt.session.client.models;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceTestGwt;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class AuditTestGwt extends ResourceTestGwt<Audit> {

    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Audit               resource;

    private static final String RESOURCE_XML = "<audit>"
                                                     + "<id>1</id>"
                                                     + "<date>2009-09-09 09:09:09.0</date>"
                                                     + "<login>first valule of login</login>"
                                                     + "<message>first valule of message</message>"
                                                     + "</audit>";

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML.replaceFirst("<created_at>[0-9-:. ]*</created_at>",
                                         "")
                .replaceFirst("<updated_at>[0-9-:. ]*</updated_at>", "")
                .replace("<id>1</id>", "");
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
        return "<audits>" + resource1Xml() + resource2Xml() + "</audits>";
    }

    @Override
    protected ResourceFactory<Audit> factorySetUp() {
        return new AuditFactory(this.repository, this.notifications);
    }

    @Override
    protected Resource<Audit> resourceSetUp() {
        this.resource = this.factory.newResource(idValue());

        this.resource.id = 1;
        this.resource.date = "2009-09-09 09:09:09.0";
        this.resource.login = "first valule of login";
        this.resource.message = "first valule of message";

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("first valule of login", this.resource.login);
    }

    @Override
    public void doTestUpdate() {
        this.resource.login = changedValue();
        this.resource.save();
        assertEquals(this.resource.login, changedValue());
    }

    private final static String XML = "<audit>"
                                            + "<id>1</id>"
                                            + "<date>2009-09-09 09:09:09.0</date>"
                                            + "<login>first valule of login</login>"
                                            + "<message>first valule of message</message>"
                                            + "</audit>";

    @Override
    protected String changedValue() {
        return "second value of login";
    }

    @Override
    protected int idValue() {
        return 1;
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

    @Override
    protected String value() {
        return "first valule of login";
    }
}
