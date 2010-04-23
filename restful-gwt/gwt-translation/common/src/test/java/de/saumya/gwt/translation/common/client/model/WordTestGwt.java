package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Resource;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class WordTestGwt extends AbstractCommonTestGwt<Word> {

    private Word resource;

    @Override
    protected String resource1Xml() {
        return "<word>" + "<code>CODE</code>" + "<text>code</text>" + "</word>";
    }

    @Override
    protected String resource2Xml() {
        return "<word>" + "<code>CODE2</code>" + "<text>something</text>"
                + "</word>";
    }

    @Override
    protected String resourcesXml() {
        return "<words>" + resource1Xml() + resource2Xml() + "</words>";
    }

    @Override
    protected WordFactory factorySetUp() {
        return new WordFactory(this.repository, this.notifications);
    }

    @Override
    protected Resource<Word> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.id = 1;
        this.resource.code = "CODE";
        this.resource.text = "code";

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
        return "code";
    }

    @Override
    protected String keyValue() {
        return "1";
    }

    @Override
    protected void doTestUpdate() {
        this.resource.text = changedValue();
        this.resource.save();
        assertEquals(changedValue(), this.resource.text);
    }

    @Override
    protected String marshallingXml() {
        return resource1Xml();
    }

}
