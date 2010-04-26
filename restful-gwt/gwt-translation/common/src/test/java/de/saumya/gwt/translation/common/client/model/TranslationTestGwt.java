package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.UserFactory;
import de.saumya.gwt.session.client.models.UserGroupFactory;

public class TranslationTestGwt extends AbstractCommonTestGwt<Translation> {

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
                                    + "<approved_by>"
                                    + "<id>1</id><login>root</login>"
                                    + "<groups></groups>" + "</approved_by>"
                                    + "</translation>";

    @Override
    protected ResourceFactory<Translation> factorySetUp() {
        return new TranslationFactory(this.repository,
                this.notifications,
                new UserFactory(this.repository,
                        this.notifications,
                        new LocaleFactory(this.repository, this.notifications),
                        new UserGroupFactory(this.repository,
                                this.notifications,
                                new LocaleFactory(this.repository,
                                        this.notifications),
                                new DomainFactory(this.repository,
                                        this.notifications))));
    }

    @Override
    protected AbstractResource<Translation> resourceSetUp() {
        this.resource = this.factory.newResource(123);

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
    protected int idValue() {
        return 123;
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
