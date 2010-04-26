package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.AbstractResource;

public class WordBundleTestGwt extends
        AbstractCommonAnonymousTestGwt<WordBundle> {

    private WordBundle resource;

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
        return "<word_bundles>" + resource1Xml() + resource2Xml()
                + "</word_bundles>";
    }

    private static final String RESOURCE_XML = "<word_bundle>"
                                                     + "<locale>en</locale>"
                                                     + "<words></words>"
                                                     + "</word_bundle>";

    @Override
    protected WordBundleFactory factorySetUp() {
        return new WordBundleFactory(this.repository,
                this.notifications,
                new WordFactory(this.repository, this.notifications));
    }

    @Override
    protected AbstractResource<WordBundle> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.locale = "en";

        return this.resource;
    }
}
