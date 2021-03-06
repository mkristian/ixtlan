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
    public User                          createdBy;
    public Timestamp                     updatedAt;
    public User                          updatedBy;

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
        // to avoid endless recursion the createBy and updatedBy get omitted
        // appendXml(buf, "created_by", this.createdBy);
        // appendXml(buf, "updated_by", this.updatedBy);
    }

    @Override
    public void fromElement(final Element root) {
        this.login = getString(root, "login");
        this.name = getString(root, "name");
        this.email = getString(root, "email");
        this.preferedLanguage = this.localeFactory.getChildResource(root,
                                                                    "preferred_language");
        this.groups = this.groupFactory.getChildResourceCollection(root,
                                                                   "groups",
                                                                   this.groups);
        this.createdAt = getTimestamp(root, "created_at", this.createdAt);
        this.createdBy = this.factory.getChildResource(root,
                                                       "created_by",
                                                       this.createdBy);
        this.updatedAt = getTimestamp(root, "updated_at", this.updatedAt);
        this.updatedBy = this.factory.getChildResource(root,
                                                       "updated_by",
                                                       this.updatedBy);
    }

    @Override
    public void toString(final String indent, final StringBuilder buf) {
        toString(indent, buf, "login", this.login);
        toString(indent, buf, "name", this.name);
        toString(indent, buf, "email", this.email);
        toString(indent, buf, "preferred_language", this.preferedLanguage);
        toString(indent, buf, "groups", this.groups);
        toString(indent, buf, "created_at", this.createdAt);
        if (this.createdBy != null) {
            toString(indent, buf, "created_by", this.createdBy.login);
        }
        toString(indent, buf, "updated_at", this.updatedAt);
        if (this.updatedBy != null) {
            toString(indent, buf, "updated_by", this.updatedBy.login);
        }
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
        // buf.append("<").append(this.email).append(">");
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

    public boolean isDomainsAdmin() {
        for (final UserGroup g : this.groups) {
            if (g.isDomainsAdmin()) {
                return true;
            }
        }
        return false;
    }
}
