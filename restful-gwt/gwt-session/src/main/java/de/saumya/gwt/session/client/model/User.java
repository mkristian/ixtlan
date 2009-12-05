/**
 * 
 */
package de.saumya.gwt.session.client.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.Resources;

public class User extends Resource<User> {

    private final LocaleFactory localeFactory;
    private final GroupFactory  groupFactory;

    protected User(final Repository repository, final UserFactory factory,
            final LocaleFactory localeFactory, final GroupFactory groupFactory) {
        super(repository, factory);
        this.localeFactory = localeFactory;
        this.groupFactory = groupFactory;
    }

    public String           login;
    public String           email;
    public String           name;
    public Locale           preferedLanguage;

    public Timestamp        createdAt;
    public Timestamp        updatedAt;

    public Resources<Group> groups;

    @Override
    public String key() {
        return this.login;
    }

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "login", this.login);
        append(buf, "name", this.name);
        append(buf, "email", this.email);
        append(buf, "preferred_language", this.preferedLanguage);
        append(buf, "groups", this.groups);
        append(buf, "created_at", this.createdAt);
        append(buf, "updated_at", this.updatedAt);
    }

    @Override
    public void fromXml(final Element root) {
        this.login = getString(root, "login");
        this.name = getString(root, "name");
        this.email = getString(root, "email");
        this.preferedLanguage = this.localeFactory.getChildResource(root,
                                                                    "preferred_language");
        this.groups = this.groupFactory.getChildResources(root, "groups");
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
        if (this.groups != null) {
            buf.append(", :groups => ").append(this.groups);
        }
        buf.append(", :created_at => ").append(this.createdAt);
        buf.append(", :updated_at => ").append(this.updatedAt);
    }

    public Collection<Locale> getAllowedLocales() {
        final Collection<Locale> result = new HashSet<Locale>();
        for (final Group group : this.groups) {
            result.addAll(group.locales);
        }
        return result;
    }

    @Override
    public String display() {
        final StringBuffer buf = new StringBuffer(this.name);
        buf.append("<").append(this.email).append(">");
        buf.append(" (").append(this.login).append(")");
        return buf.toString();
    }
}