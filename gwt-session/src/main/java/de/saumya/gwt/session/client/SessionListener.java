/**
 * 
 */
package de.saumya.gwt.session.client;

public interface SessionListener {
    
    void onSessionTimeout();
    
    void onAccessDenied();
    
    void onSuccessfulLogin();

    void onLoggedOut();
}