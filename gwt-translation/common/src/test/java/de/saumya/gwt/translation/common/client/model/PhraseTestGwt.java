package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.LocaleFactory;
import de.saumya.gwt.session.client.RoleFactory;
import de.saumya.gwt.session.client.UserFactory;
import de.saumya.gwt.session.client.VenueFactory;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.model.TranslationFactory;

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
                + "<to_be_approved>some text</to_be_approved>" + "</phrase>";
    }

    @Override
    protected String resource2Xml() {
        return "<phrase>" + "<id>345</id>"
                + "<to_be_approved>some text</to_be_approved>" + "</phrase>";
    }

    @Override
    protected String resourcesXml() {
        return "<phrases>" + resource1Xml() + resource2Xml() + "</phrases>";
    }

    static final String XML = "<phrase>"
                                    + "<id>123</id>"
                                    + "<to_be_approved>some text</to_be_approved>"
                                    + "<updated_at>2009-07-09 17:14:48.9</updated_at>"
                                    + "<updated_by><login>root</login><roles></roles></updated_by>"
                                    + "</phrase>";

    @Override
    protected ResourceFactory<Phrase> factorySetUp() {
        return new PhraseFactory(this.repository,
                new UserFactory(this.repository,
                        new LocaleFactory(this.repository),
                        new RoleFactory(this.repository,
                                new LocaleFactory(this.repository),
                                new VenueFactory(this.repository))),
                new TranslationFactory(this.repository,
                        new UserFactory(this.repository,
                                new LocaleFactory(this.repository),
                                new RoleFactory(this.repository,
                                        new LocaleFactory(this.repository),
                                        new VenueFactory(this.repository)))));
    }

    @Override
    protected Resource<Phrase> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.toBeApproved = "some text";

        this.repository.addXmlResponse(resource1Xml());

        this.resource.save();

        return this.resource;
    }

    @Override
    protected void doTestCreate() {
        assertEquals(value(), this.resource.toBeApproved);
        System.out.println(resourceNewXml());
        System.out.println(this.repository.requests.get(0));
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
        this.resource.toBeApproved = changedValue();
        this.resource.save();
        assertEquals(changedValue(), this.resource.toBeApproved);
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

}
