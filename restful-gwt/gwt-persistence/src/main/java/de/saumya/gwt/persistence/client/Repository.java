/**
 * 
 */
package de.saumya.gwt.persistence.client;

import java.util.Collections;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;

public class Repository {

    private static final String        URL      = GWT.getHostPageBaseURL();
    private static Map<String, String> NO_QUERY = Collections.emptyMap();

    private String                     authenticationToken;

    public void setAuthenticationToken(final String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    void all(final String resourceName, final Map<String, String> query,
            final RequestCallback callback) {
        final StringBuilder buf = new StringBuilder(URL);
        buf.append(resourceName).append(".xml");
        if (query != null && !query.isEmpty()) {
            buf.append("?");
            for (final Map.Entry<String, String> entry : query.entrySet()) {
                buf.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        }
        doIt("GET", buf.toString(), null, callback);
    }

    void all(final String resourceName, final RequestCallback callback) {
        all(resourceName, NO_QUERY, callback);
    }

    void get(final String resourceName, final int id,
            final RequestCallback callback) {
        get(resourceName, "" + id, callback);
    }

    void get(final String resourceName, final RequestCallback callback) {
        doIt("GET", URL + resourceName + ".xml", null, callback);
    }

    void get(final String resourceName, final String id,
            final RequestCallback callback) {
        doIt("GET", URL + resourceName + "/" + id + ".xml", null, callback);
    }

    void post(final Resource<? extends Resource<?>> resource,
            final RequestCallback callback) {
        if (resource.key() != null) {
            doIt("POST",
                 URL + resource.factory.storagePluralName() + ".xml",
                 resource.toXml(),
                 callback);
        }
        else {
            doIt("POST",
                 URL + resource.factory.storageName() + ".xml",
                 resource.toXml(),
                 callback);
        }
    }

    void put(final Resource<? extends Resource<?>> resource,
            final RequestCallback callback) {
        if (resource.key() != null) {
            doIt("PUT", URL + resource.factory.storagePluralName() + "/"
                    + resource.key() + ".xml", resource.toXml(), callback);
        }
        else { // singleton
            doIt("PUT",
                 URL + resource.factory.storageName() + ".xml",
                 resource.toXml(),
                 callback);
        }
    }

    void put(final Resource<? extends Resource<?>> resource, final String verb,
            final RequestCallback callback) {
        if (resource.key() != null) {
            doIt("PUT",
                 URL + resource.factory.storagePluralName() + "/"
                         + resource.key() + "/" + verb + ".xml",
                 resource.toXml(),
                 callback);
        }
        else { // singleton
            doIt("PUT", URL + resource.factory.storageName() + "/" + verb
                    + ".xml", resource.toXml(), callback);
        }
    }

    void delete(final Resource<? extends Resource<?>> resource,
            final RequestCallback callback) {
        if (resource.key() != null) {
            doIt("DELETE", URL + resource.factory.storagePluralName() + "/"
                    + resource.key() + ".xml", null, callback);
        }
        else { // singleton
            doIt("DELETE",
                 URL + resource.factory.storageName() + ".xml",
                 null,
                 callback);
        }
    }

    void doIt(final String method, final String url, final String data,
            final RequestCallback callback) {
        final RequestBuilder builder = new RestfulRequestBuilder(method,
                url,
                this.authenticationToken);
        try {
            builder.sendRequest(data, callback);
        }
        catch (final RequestException e) {
            // TODO
            GWT.log("TODO", e);
        }
    }
}