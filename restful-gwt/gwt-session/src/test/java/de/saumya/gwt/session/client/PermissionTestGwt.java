package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.AnonymousResourceTestGwt;

public class PermissionTestGwt extends AnonymousResourceTestGwt<Permission> {

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Permission          resource;

    private RoleFactory         roleFactory;

    private static final String RESOURCE_XML = "<permission>"
                                                     + "<resource>config</resource>"
                                                     + "<action>create</action>"
                                                     + "<roles><role>"
                                                     + "<name>admin</name>"
                                                     + "</role></roles>"
                                                     + "</permission>";

    @Override
    protected AbstractResource<Permission> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.resource = "config";
        this.resource.action = "create";
        final Role role = this.roleFactory.newResource();
        role.name = "admin";
        this.resource.roles = this.roleFactory.newResources();
        this.resource.roles.add(role);

        return this.resource;
    }

    // private final static String XML = "<permission>"
    // + "<resource>config</resource>"
    // + "<action>create</action>"
    // + "<roles>" + "<role>"
    // + "<name>root</name>" + "</role>"
    // + "</roles>" + "</permission>";

    @Override
    protected PermissionFactory factorySetUp() {
        this.roleFactory = new RoleFactory(this.repository, this.notifications);
        return new PermissionFactory(this.repository,
                this.notifications,
                this.roleFactory);
    }

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">create<", ">update<");
    }

    @Override
    protected String resourcesXml() {
        return "<permissions>" + resource1Xml() + resource2Xml()
                + "</permissions>";
    }
}
