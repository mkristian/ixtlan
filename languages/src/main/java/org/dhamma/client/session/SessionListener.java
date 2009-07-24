/**
 * 
 */
package org.dhamma.client.session;

interface SessionListener {
    
    void onSessionTimeout();
    
    void onAccessDenied();
    
    void onSuccessfulLogin();

    void onLoggedOut();
}