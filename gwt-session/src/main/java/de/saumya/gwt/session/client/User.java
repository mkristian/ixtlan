/**
 * 
 */
package de.saumya.gwt.session.client;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

public class User extends Resource<User> {

    private final UserFactory factory;

    protected User(final Repository repository, final UserFactory factory) {
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
        return this.login;
    }

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "login", this.login);
        append(buf, "name", this.name);
        append(buf, "email", this.email);
        if (this.preferedLanguage != null) {
            this.preferedLanguage.toXml(buf);
        }
        if (this.roles != null) {
            this.roles.toXml(buf);
        }
        append(buf, "created_at", this.createdAt);
        append(buf, "updated_at", this.updatedAt);
    }

    @Override
    public void fromXml(final Element root) {
        this.login = getString(root, "login");
        this.name = getString(root, "name");
        this.email = getString(root, "email");
        final Locale locale = this.factory.newLocaleResource();
        locale.fromXml(root);
        if (locale.key() != null) {
            this.preferedLanguage = locale;
        }
        final Element child = getChildElement(root, "roles");
        if (child != null) {
            this.roles = this.factory.newRoleResources();
            this.roles.fromXml(child);
        }
        this.createdAt = getTimestamp(root, "created_at");
        this.updatedAt = getTimestamp(root, "updated_at");
    }

    @Override
    public void toString(final StringBuffer buf) {
        buf.append(":login => ").append(this.login);
        buf.append(", :name => ").append(this.name);
        buf.append(", :email => ").append(this.email);
        if (this.preferedLanguage != null) {
            buf.append(", :preferedLanguage => ");
            this.preferedLanguage.toString(buf);
        }
        if (this.roles != null) {
            buf.append(", :roles => ").append(this.roles);
        }
        buf.append(", :created_at => ").append(this.createdAt);
        buf.append(", :updated_at => ").append(this.updatedAt);
    }

    public Collection<Locale> getAllowedLocales() {
        final Collection<Locale> result = new HashSet<Locale>();
        for (final Role role : this.roles) {
            result.addAll(role.locales);
        }
        return result;
    }
}