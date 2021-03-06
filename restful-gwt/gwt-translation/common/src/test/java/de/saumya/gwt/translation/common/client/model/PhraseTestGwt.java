package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.GroupFactory;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.UserFactory;
import de.saumya.gwt.session.client.models.UserGroupFactory;

public class PhraseTestGwt extends AbstractCommonTestGwt<Phrase> {

    private Phrase resource;

    @Override
    protected String resource1Xml() {
        return "<phrase>" + "<id>1</id><code>123</code>"
                + "<current_text>some text</current_text>" + "</phrase>";
    }

    @Override
    protected String resource2Xml() {
        return "<phrase>" + "<id>2</id><code>345</code>"
                + "<current_text>some text</current_text>" + "</phrase>";
    }

    @Override
    protected String resourcesXml() {
        return "<phrases>" + resource1Xml() + resource2Xml() + "</phrases>";
    }

    static final String XML = "<phrase>"
                                    + "<id>1</id>"
                                    + "<code>123</code>"
                                    + "<current_text>text</current_text>"
                                    + "<text>some text</text>"
                                    + "<updated_at>2009-07-09 17:14:48.9</updated_at>"
                                    + "<updated_by>" + "<id>1</id>"
                                    + "<login>root</login>"
                                    + "<groups></groups>" + "</updated_by>"
                                    + "</phrase>";

    @Override
    protected ResourceFactory<Phrase> factorySetUp() {
        final LocaleFactory localeFactory = new LocaleFactory(this.repository,
                this.notifications);
        final DomainFactory domainFactory = new DomainFactory(this.repository,
                this.notifications);
        final UserGroupFactory userGroupFactory = new UserGroupFactory(this.repository,
                this.notifications,
                localeFactory,
                domainFactory);
        final UserFactory userFactory = new UserFactory(this.repository,
                this.notifications,
                new GroupFactory(this.repository,
                        this.notifications,
                        userGroupFactory));
        return new PhraseFactory(this.repository,
                this.notifications,
                userFactory,
                localeFactory,
                new TranslationFactory(this.repository,
                        this.notifications,
                        userFactory));
    }

    @Override
    protected AbstractResource<Phrase> resourceSetUp() {
        this.resource = this.factory.newResource(1);

        this.resource.code = "123";
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
    protected int idValue() {
        return 1;
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
