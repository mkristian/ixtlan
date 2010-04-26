/**
 *
 */
package de.saumya.gwt.session.client;

public interface SessionListener {

    void onTimeout();

    void onAccessDenied();

    void onLogin();

    void onLogout();
}
