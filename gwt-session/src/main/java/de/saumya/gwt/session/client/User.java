/**
 * 
 */
package de.saumya.gwt.session.client;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

class User extends Resource<User> {

    private final UserFactory factory;

    protected User(Repository repository, UserFactory factory) {
        super(repository, factory);
        this.factory = factory;
    }

    public String          login;
    public String          email;
    public String          name;
    public Locale          preferedLanguage;

    public Timestamp       createdAt;
    public Timestamp       updatedAt;

    public Resources<Role> roles;

    @Override
    protected String key() {
        return login;
    }

    protected void appendXml(StringBuffer buf) {
        append(buf, "login", login);
        append(buf, "name", name);
        append(buf, "email", email);
        if (preferedLanguage != null) preferedLanguage.toXml(buf);
        if (roles != null) roles.toXml(buf);
        append(buf, "created_at", createdAt);
        append(buf, "updated_at", updatedAt);
    }

    protected void fromXml(Element root) {
        login = getString(root, "login");
        name = getString(root, "name");
        email = getString(root, "email");
        Locale locale = factory.newLocaleResource();
        locale.fromXml(root);
        if (locale.key() != null)
            preferedLanguage = locale;
        Element child = getChildElement(root, "roles");
        if (child != null) {
            roles = factory.newRoleResources();
            roles.fromXml(child);
        }
        createdAt = getTimestamp(root, "created_at");
        updatedAt = getTimestamp(root, "updated_at");
    }

    public void toString(StringBuffer buf) {
        buf.append(":login => ").append(login);
        buf.append(", :name => ").append(name);
        buf.append(", :email => ").append(email);
        if (preferedLanguage != null) {
            buf.append(", :preferedLanguage => ");
            preferedLanguage.toString(buf);
        }
        if (roles != null) buf.append(", :roles => ").append(roles);
        buf.append(", :created_at => ").append(createdAt);
        buf.append(", :updated_at => ").append(updatedAt);
    }
}