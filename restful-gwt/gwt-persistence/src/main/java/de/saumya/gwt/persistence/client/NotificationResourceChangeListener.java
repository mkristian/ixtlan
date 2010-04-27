package de.saumya.gwt.persistence.client;

@SuppressWarnings("unchecked")
public class NotificationResourceChangeListener implements
        ResourceChangeListener {

    private final ResourceNotifications notifications;
    private final String                message;

    public NotificationResourceChangeListener(
            final ResourceNotifications notifications, final String message) {
        this.notifications = notifications;
        this.message = message;
    }

    @Override
    public void onChange(final AbstractResource resource) {
        this.notifications.info(this.message, resource);
    }

    @Override
    public void onError(final int status, final String errorMessage,
            final AbstractResource resource) {
        // TODO add message to notifications !!!
        this.notifications.error(status, errorMessage, resource);

    }

}
