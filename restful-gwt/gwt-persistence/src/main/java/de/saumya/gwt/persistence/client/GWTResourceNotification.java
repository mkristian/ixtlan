/**
 * 
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.core.client.GWT;

public class GWTResourceNotification implements ResourceNotifications {

    @Override
    public void error(final int status, final String message,
            final Resource<? extends Resource<?>> resource) {
        GWT.log(status + ": " + message + " " + resource, null);
    }

    @Override
    public void info(final String message,
            final Resource<? extends Resource<?>> resource) {
        GWT.log(message + " " + resource, null);
    }

}