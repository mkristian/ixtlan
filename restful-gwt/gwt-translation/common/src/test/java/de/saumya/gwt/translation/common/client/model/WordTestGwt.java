package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.AbstractResource;

public class WordTestGwt extends AbstractCommonAnonymousTestGwt<Word> {

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
    protected AbstractResource<Word> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.code = "CODE";
        this.resource.text = "code";

        return this.resource;
    }
}
