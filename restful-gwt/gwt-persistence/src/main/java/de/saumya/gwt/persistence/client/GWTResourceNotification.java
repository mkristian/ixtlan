/**
 *
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.core.client.GWT;

public class GWTResourceNotification implements ResourceNotifications {

    @Override
    public void error(final int status, final String statusMessage,
            final String message, final AbstractResource<?> resource) {
        GWT.log(status + ": " + statusMessage + ": " + message + " " + resource,
                null);
    }

    @Override
    public void info(final String message, final AbstractResource<?> resource) {
        GWT.log(message + " " + resource, null);
    }

}
