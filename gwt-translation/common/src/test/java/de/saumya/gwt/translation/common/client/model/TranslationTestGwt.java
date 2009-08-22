package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.LocaleFactory;
import de.saumya.gwt.session.client.RoleFactory;
import de.saumya.gwt.session.client.UserFactory;
import de.saumya.gwt.session.client.VenueFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class TranslationTestGwt extends AbstractResourceTestGwt<Translation> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.translation.common.CommonTest";
    }

    private Translation resource;

    @Override
    protected String resource1Xml() {
        return "<translation>" + "<id>123</id>"
                + "<previous_text>text</previous_text>"
                + "<text>some text</text>" + "</translation>";
    }

    @Override
    protected String resource2Xml() {
        return "<translation>" + "<id>234</id>"
                + "<previous_text>other text</previous_text>"
                + "<text>some other text</text>" + "</translation>";
    }

    @Override
    protected String resourcesXml() {
        return "<translations>" + resource1Xml() + resource2Xml()
                + "</translations>";
    }

    static final String XML = "<translation>"
                                    + "<id>123</id>"
                                    + "<previous_text>text</previous_text>"
                                    + "<text>some text</text>"
                                    + "<approved_at>2009-07-09 17:14:48.9</approved_at>"
                                    + "<approved_by>" + "<login>root</login>"
                                    + "<roles></roles>" + "</approved_by>"
                                    + "</translation>";

    @Override
    protected ResourceFactory<Translation> factorySetUp() {
        return new TranslationFactory(this.repository,
                new UserFactory(this.repository,
                        new LocaleFactory(this.repository),
                        new RoleFactory(this.repository,
                                new LocaleFactory(this.repository),
                                new VenueFactory(this.repository))));
    }

    @Override
    protected Resource<Translation> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.text = "some text";
        this.resource.previousText = "text";

        this.repository.addXmlResponse(resource1Xml());

        this.resource.save();

        return this.resource;
    }

    @Override
    protected void doTestCreate() {
        assertEquals(value(), this.resource.text);
    }

    @Override
    protected String changedValue() {
        return "something else";
    }

    @Override
    protected String value() {
        return "some text";
    }

    @Override
    protected String keyValue() {
        return "123";
    }

    @Override
    protected void doTestUpdate() {
        this.resource.text = changedValue();
        this.resource.save();
        assertEquals(changedValue(), this.resource.text);
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

}
