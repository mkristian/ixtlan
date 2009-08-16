/**
 * 
 */
package de.saumya.gwt.session.client;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;

public class Configuration extends Resource<Configuration> {

    private final UserFactory userFactory;

    Configuration(final Repository repository,
            final ConfigurationFactory factory, final UserFactory userFactory) {
        super(repository, factory);
        this.userFactory = userFactory;
    }

    public int                idleSessionTimeout;
    public int                auditLogRotation;
    public Collection<String> availableLocales;
    public String             emailForErrorNotification;
    public Timestamp          updatedAt;
    public User               updatedBy;

    @Override
    public String key() {
        return null;
    }

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "idle_session_timeout", this.idleSessionTimeout);
        append(buf, "audit_log_rotation", this.auditLogRotation);
        append(buf, "available_locales", this.availableLocales);
        append(buf,
               "email_for_error_notification",
               this.emailForErrorNotification);
        append(buf, "updated_at", this.updatedAt);
        append(buf, "updated_by", this.updatedBy);
    }

    @Override
    protected void fromXml(final Element root) {
        this.idleSessionTimeout = getInt(root, "idle_session_timeout");
        this.auditLogRotation = getInt(root, "audit_log_rotation");
        this.availableLocales = Arrays.asList(getString(root,
                                                        "available_locales").split(","));
        this.emailForErrorNotification = getString(root,
                                                   "email_for_error_notification");
        this.updatedAt = getTimestamp(root, "updated_at");
        this.updatedBy = this.userFactory.getChildResource(root, "updated_by");
    }

    @Override
    public void toString(final StringBuffer buf) {
        buf.append(":idle_session_timeout => ").append(this.idleSessionTimeout);
        buf.append(":audit_log_rotation => ").append(this.auditLogRotation);
        buf.append(":available_locales => ").append(this.availableLocales);
        buf.append(":email_for_error_notification => ")
                .append(this.emailForErrorNotification);
        buf.append(", :updated_at => ").append(this.updatedAt);
        buf.append(", :updated_by => ");
        this.updatedBy.toString(buf);
    }
}