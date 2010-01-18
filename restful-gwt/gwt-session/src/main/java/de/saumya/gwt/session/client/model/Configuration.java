/**
 * 
 */
package de.saumya.gwt.session.client.model;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;

public class Configuration extends Resource<Configuration> {

    private final UserFactory userFactory;

    Configuration(final Repository repository,
            final ConfigurationFactory factory, final UserFactory userFactory) {
        super(repository, factory, null);
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
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "session_idle_timeout", this.idleSessionTimeout);
        appendXml(buf, "keep_audit_logs", this.auditLogRotation);
        appendXml(buf, "locales", this.availableLocales);
        appendXml(buf,
               "notification_recipient_emails",
               this.emailForErrorNotification);
        appendXml(buf, "updated_at", this.updatedAt);
        appendXml(buf, "updated_by", this.updatedBy);
    }

    @Override
    protected void fromXml(final Element root) {
        this.idleSessionTimeout = getInt(root, "session_idle_timeout");
        this.auditLogRotation = getInt(root, "keep_audit_logs");
        final String locales = getString(root, "locales");
        if (locales != null) {
            this.availableLocales = Arrays.asList(locales.split(","));
        }
        this.emailForErrorNotification = getString(root,
                                                   "notification_recipient_emails");
        this.updatedAt = getTimestamp(root, "updated_at");
        this.updatedBy = this.userFactory.getChildResource(root, "updated_by");
    }

    @Override
    public void toString(final StringBuilder buf) {
        buf.append(":idle_session_timeout => ").append(this.idleSessionTimeout);
        buf.append(", :audit_log_rotation => ").append(this.auditLogRotation);
        buf.append(", :available_locales => ").append(this.availableLocales);
        buf.append(", :email_for_error_notification => ")
                .append(this.emailForErrorNotification);
        buf.append(", :updated_at => ").append(this.updatedAt);
        if (this.updatedBy != null) {
            buf.append(", :updated_by => ");
            this.updatedBy.toString(buf);
        }
    }

    @Override
    public String display() {
        return "configuration";
    }
}