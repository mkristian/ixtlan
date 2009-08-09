/**
 * 
 */
package de.saumya.gwt.datamapper.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;

public class Repository {

    private static final String URL = GWT.getHostPageBaseURL();

    void all(final String resourceName, final RequestCallback callback) {
        doIt("GET", URL + resourceName + ".xml", null, callback);
    }

    void get(final String resourceName, final int id,
            final RequestCallback callback) {
        get(resourceName, "" + id, callback);
    }

    void get(final String resourceName, final String id,
            final RequestCallback callback) {
        doIt("GET", URL + resourceName + "/" + id + ".xml", null, callback);
    }

    void post(final Resource<? extends Resource<?>> resource,
            final RequestCallback callback) {
        doIt("POST",
             URL + resource.storageName + ".xml",
             resource.toXml(),
             callback);
    }

    void put(final Resource<? extends Resource<?>> resource,
            final RequestCallback callback) {
        doIt("PUT",
             URL + resource.storageName + "/" + resource.key() + ".xml",
             resource.toXml(),
             callback);
    }

    void delete(final Resource<? extends Resource<?>> resource,
            final RequestCallback callback) {
        doIt("DELETE", URL + resource.storageName + "/" + resource.key()
                + ".xml", null, callback);
    }

    void doIt(final String method, final String url, final String data,
            final RequestCallback callback) {
        final RequestBuilder builder = new RestfulRequestBuilder(method, url);
        try {
            builder.sendRequest(data, callback);
        }
        catch (final RequestException e) {
            // TODO
            GWT.log("TODO", e);
        }
    }
}