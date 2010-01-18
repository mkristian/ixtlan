/**
 * 
 */
package de.saumya.gwt.persistence.client;

public interface ResourceChangeNotification {

    public void onChange(final String message, final Resource<?> resource);

    public void onError(final int status, final String statusText,
            final Resource<?> resource);

}