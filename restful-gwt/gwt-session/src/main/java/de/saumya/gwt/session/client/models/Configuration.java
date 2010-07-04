/**
 *
 */
package de.saumya.gwt.session.client.models;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.SingletonResource;

public class Configuration extends SingletonResource<Configuration> {

    private final UserFactory   userFactory;
    private final LocaleFactory localeFactory;

    Configuration(final Repository repository,
            final ConfigurationFactory factory, final UserFactory userFactory,
            final LocaleFactory localeFactory) {
        super(repository, factory);
        this.userFactory = userFactory;
        this.localeFactory = localeFactory;
    }

    public int                        sessionIdleTimeout;
    public int                        keepAuditLogs;
    public String                     passwordSenderEmail;
    public String                     loginUrl;
    public ResourceCollection<Locale> locales;
    public String                     notificationSenderEmail;
    public String                     notificationRecipientEmails;
    public String                     errorsDumpDirectory;
    public String                     logfilesDirectory;
    public Timestamp                  updatedAt;
    public User                       updatedBy;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "session_idle_timeout", this.sessionIdleTimeout);
        appendXml(buf, "keep_audit_logs", this.keepAuditLogs);
        appendXml(buf, "password_sender_email", this.passwordSenderEmail);
        appendXml(buf, "login_url", this.loginUrl);
        appendXml(buf, "errors_dump_directory", this.errorsDumpDirectory);
        appendXml(buf, "logfiles_directory", this.logfilesDirectory);
        appendXml(buf, "locales", this.locales);
        appendXml(buf,
                  "notification_sender_email",
                  this.notificationSenderEmail);
        appendXml(buf,
                  "notification_recipient_emails",
                  this.notificationRecipientEmails);
        appendXml(buf, "updated_at", this.updatedAt);
        appendXml(buf, "updated_by", this.updatedBy);
    }

    @Override
    protected void fromElement(final Element root) {
        this.sessionIdleTimeout = getInt(root, "session_idle_timeout");
        this.keepAuditLogs = getInt(root, "keep_audit_logs");
        this.passwordSenderEmail = getString(root, "password_sender_email");
        this.loginUrl = getString(root, "login_url");
        this.logfilesDirectory = getString(root, "logfiles_directory");
        this.errorsDumpDirectory = getString(root, "errors_dump_directory");
        this.locales = this.localeFactory.getChildResourceCollection(root,
                                                                     "locales");
        this.notificationRecipientEmails = getString(root,
                                                     "notification_recipient_emails");
        this.notificationSenderEmail = getString(root,
                                                 "notification_sender_email");
        this.updatedAt = getTimestamp(root, "updated_at");
        this.updatedBy = this.userFactory.getChildResource(root, "updated_by");
    }

    @Override
    public void toString(final String indent, final StringBuilder buf) {
        toString(indent, buf, "session_idle_timeout", this.sessionIdleTimeout);
        toString(indent, buf, "keep_audit_logs", this.keepAuditLogs);
        toString(indent, buf, "locales", this.locales);
        toString(indent, buf, "password_sender_email", this.passwordSenderEmail);
        toString(indent, buf, "login_url", this.loginUrl);
        toString(indent, buf, "errors_dump_directory", this.errorsDumpDirectory);
        toString(indent, buf, "logfiles_directory", this.logfilesDirectory);
        toString(indent,
                 buf,
                 "notification_sender_emailn",
                 this.notificationSenderEmail);
        toString(indent,
                 buf,
                 "notification_recipient_emails",
                 this.notificationRecipientEmails);
        toString(indent, buf, "updated_at", this.updatedAt);
        toString(indent, buf, "updated_by", this.updatedBy);
    }

    @Override
    public String display() {
        return "Configuration";
    }
}
