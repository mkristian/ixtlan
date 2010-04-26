/**
 *
 */
package de.saumya.gwt.session.client.models;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;

public class User extends Resource<User> {

    private final LocaleFactory    localeFactory;
    private final UserGroupFactory groupFactory;

    protected User(final Repository repository, final UserFactory factory,
            final LocaleFactory localeFactory,
            final UserGroupFactory groupFactory, final int id) {
        super(repository, factory, id);
        this.localeFactory = localeFactory;
        this.groupFactory = groupFactory;
        this.groups = groupFactory.newResources();
    }

    public String                        login;
    public String                        email;
    public String                        name;
    public Locale                        preferedLanguage;

    public Timestamp                     createdAt;
    public Timestamp                     updatedAt;

    public ResourceCollection<UserGroup> groups;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "login", this.login);
        appendXml(buf, "name", this.name);
        appendXml(buf, "email", this.email);
        appendXml(buf, "preferred_language", this.preferedLanguage);
        appendXml(buf, "groups", this.groups);
        appendXml(buf, "created_at", this.createdAt);
        appendXml(buf, "updated_at", this.updatedAt);
    }

    @Override
    public void fromElement(final Element root) {
        this.login = getString(root, "login");
        this.name = getString(root, "name");
        this.email = getString(root, "email");
        this.preferedLanguage = this.localeFactory.getChildResource(root,
                                                                    "preferred_language");
        this.groups = this.groupFactory.getChildResourceCollection(root,
                                                                   "groups");
        this.createdAt = getTimestamp(root, "created_at");
        this.updatedAt = getTimestamp(root, "updated_at");
    }

    @Override
    public void toString(final StringBuilder buf) {
        toString(buf, "login", this.login);
        toString(buf, "name", this.name);
        toString(buf, "email", this.email);
        toString(buf, "preferred_language", this.preferedLanguage);
        toString(buf, "groups", this.groups);
        toString(buf, "created_at", this.createdAt);
        toString(buf, "updated_at", this.updatedAt);
    }

    public Collection<Locale> getAllowedLocales() {
        final Collection<Locale> result = new HashSet<Locale>();
        for (final UserGroup group : this.groups) {
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

    public boolean isRoot() {
        for (final UserGroup g : this.groups) {
            if (g.isRoot()) {
                return true;
            }
        }
        return false;
    }

    public boolean isLocalesAdmin() {
        for (final UserGroup g : this.groups) {
            if (g.isLocalesAdmin()) {
                return true;
            }
        }
        return false;
    }
}
