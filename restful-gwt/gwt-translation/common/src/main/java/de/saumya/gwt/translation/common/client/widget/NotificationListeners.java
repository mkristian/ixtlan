/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import de.saumya.gwt.persistence.client.NotificationResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class NotificationListeners {
    public final NotificationResourceChangeListener loaded;
    public final NotificationResourceChangeListener created;
    public final NotificationResourceChangeListener updated;
    public final NotificationResourceChangeListener deleted;

    public NotificationListeners(final ResourceNotifications notifications) {
        this.loaded = new NotificationResourceChangeListener(notifications,
                "loaded");
        this.created = new NotificationResourceChangeListener(notifications,
                "created");
        this.updated = new NotificationResourceChangeListener(notifications,
                "updated");
        this.deleted = new NotificationResourceChangeListener(notifications,
                "deleted");
    }
}
