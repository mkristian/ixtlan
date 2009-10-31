package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.model.LocaleFactory;
import de.saumya.gwt.session.client.model.GroupFactory;
import de.saumya.gwt.session.client.model.UserFactory;
import de.saumya.gwt.session.client.model.VenueFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class PhraseTestGwt extends AbstractResourceTestGwt<Phrase> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.translation.common.CommonTest";
    }

    private Phrase resource;

    @Override
    protected String resource1Xml() {
        return "<phrase>" + "<id>123</id>"
                + "<current_text>some text</current_text>" + "</phrase>";
    }

    @Override
    protected String resource2Xml() {
        return "<phrase>" + "<id>345</id>"
                + "<current_text>some text</current_text>" + "</phrase>";
    }

    @Override
    protected String resourcesXml() {
        return "<phrases>" + resource1Xml() + resource2Xml() + "</phrases>";
    }

    static final String XML = "<phrase>"
                                    + "<id>123</id>"
                                    + "<current_text>text</current_text>"
                                    + "<text>some text</text>"
                                    + "<updated_at>2009-07-09 17:14:48.9</updated_at>"
                                    + "<updated_by>" + "<login>root</login>"
                                    + "<roles></roles>" + "</updated_by>"
                                    + "</phrase>";

    @Override
    protected ResourceFactory<Phrase> factorySetUp() {
        final UserFactory userFactory = new UserFactory(this.repository,
                new LocaleFactory(this.repository),
                new GroupFactory(this.repository,
                        new LocaleFactory(this.repository),
                        new VenueFactory(this.repository)));
        return new PhraseFactory(this.repository,
                userFactory,
                new TranslationFactory(this.repository, userFactory));
    }

    @Override
    protected Resource<Phrase> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.currentText = "some text";

        this.repository.addXmlResponse(resource1Xml());

        this.resource.save();

        return this.resource;
    }

    @Override
    protected void doTestCreate() {
        assertEquals(value(), this.resource.currentText);
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
        this.resource.currentText = changedValue();
        this.resource.save();
        assertEquals(changedValue(), this.resource.currentText);
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

}
