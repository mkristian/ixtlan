package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.AnonymousResourceTestGwt;

public class RoleTestGwt extends AnonymousResourceTestGwt<Role> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Role                resource;

    private static final String RESOURCE_XML = "<role>" + "<name>root</name>"
                                                     + "</role>";

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">root<", ">admin<");
    }

    @Override
    protected String resourcesXml() {
        return "<roles>" + resource1Xml() + resource2Xml() + "</roles>";
    }

    @Override
    protected RoleFactory factorySetUp() {
        return new RoleFactory(this.repository, this.notifications);
    }

    @Override
    protected AbstractResource<Role> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.name = "root";

        return this.resource;
    }
}
