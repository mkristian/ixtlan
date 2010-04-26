package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.UserFactory;
import de.saumya.gwt.session.client.models.UserGroupFactory;

public class PhraseBookTestGwt extends
        AbstractCommonAnonymousTestGwt<PhraseBook> {

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
        final LocaleFactory localeFactory = new LocaleFactory(this.repository,
                this.notifications);
        final UserFactory userFactory = new UserFactory(this.repository,
                this.notifications,
                localeFactory,
                new UserGroupFactory(this.repository,
                        this.notifications,
                        localeFactory,
                        new DomainFactory(this.repository, this.notifications)));
        return new PhraseBookFactory(this.repository,
                this.notifications,
                new PhraseFactory(this.repository,
                        this.notifications,
                        userFactory,
                        localeFactory,
                        new TranslationFactory(this.repository,
                                this.notifications,
                                userFactory)));
    }

    @Override
    protected AbstractResource<PhraseBook> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.locale = "en";

        return this.resource;
    }
}
