/**
 * 
 */
package de.saumya.gwt.session.client;

interface SessionListener {
    
    void onSessionTimeout();
    
    void onAccessDenied();
    
    void onSuccessfulLogin();

    void onLoggedOut();
}