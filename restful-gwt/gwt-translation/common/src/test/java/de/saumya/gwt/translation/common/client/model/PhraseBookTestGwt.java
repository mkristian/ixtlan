package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.session.client.model.LocaleFactory;
import de.saumya.gwt.session.client.model.GroupFactory;
import de.saumya.gwt.session.client.model.UserFactory;
import de.saumya.gwt.session.client.model.VenueFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class PhraseBookTestGwt extends AbstractResourceTestGwt<PhraseBook> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.translation.common.CommonTest";
    }

    private PhraseBook resource;

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">en<", ">en_GE<");
    }

    @Override
    protected String resourcesXml() {
        return "<phrase_books>" + resource1Xml() + resource2Xml()
                + "</phrase_books>";
    }

    private static final String RESOURCE_XML = "<phrase_book>"
                                                     + "<locale>en</locale>"
                                                     + "<phrases></phrases>"
                                                     + "</phrase_book>";

    @Override
    protected PhraseBookFactory factorySetUp() {
        final LocaleFactory localeFactory = new LocaleFactory(this.repository);
        final UserFactory userFactory = new UserFactory(this.repository,
                localeFactory,
                new GroupFactory(this.repository,
                        localeFactory,
                        new VenueFactory(this.repository)));
        return new PhraseBookFactory(this.repository,
                new PhraseFactory(this.repository,
                        userFactory,
                        new TranslationFactory(this.repository, userFactory)));
    }

    @Override
    protected Resource<PhraseBook> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.locale = "en";
        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();
        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("en", this.resource.locale);
    }

    @Override
    protected String keyValue() {
        return "en";
    }

    @Override
    protected String value() {
        return "en";
    }

    @Override
    protected String changedValue() {
        return "en_JP";
    }

    @Override
    protected void doTestUpdate() {
        this.resource.locale = changedValue();
        this.resource.save();
        assertEquals(changedValue(), this.resource.locale);
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

    private static final String XML = "<phrase_book>"
                                            + "<locale>en</locale>"
                                            + "<phrases>"
                                            + "<phrase>"
                                            + "<id>1</id>"
                                            + "<code>CODE</code>"
                                            + "<current_text>code</current_text>"
                                            + "</phrase>" + "</phrases>"
                                            + "</phrase_book>";

}
