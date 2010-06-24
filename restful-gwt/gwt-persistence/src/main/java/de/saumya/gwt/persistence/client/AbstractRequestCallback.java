package de.saumya.gwt.persistence.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public abstract class AbstractRequestCallback implements RequestCallback {

    private static final String AUTHENTICATION_TOKEN = "authentication-token";

    private final Repository    repository;

    AbstractRequestCallback(final Repository repository) {
        this.repository = repository;
    }

    @Override
    public void onResponseReceived(final Request request,
            final Response response) {
        final String token = response.getHeader(AUTHENTICATION_TOKEN);
        if (token != null) {
            this.repository.setAuthenticationToken(token);
        }
    }
}
