/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.KeyCodes;

public class KeyCodesHelper {
    public static boolean isPrintable(final int keyCode) {
        switch (keyCode) {
        case KeyCodes.KEY_BACKSPACE:
        case KeyCodes.KEY_DELETE:
        case KeyCodes.KEY_END:
        case KeyCodes.KEY_ENTER:
        case KeyCodes.KEY_ESCAPE:
        case KeyCodes.KEY_HOME:
        case KeyCodes.KEY_PAGEDOWN:
        case KeyCodes.KEY_PAGEUP:
        case KeyCodes.KEY_TAB:
        case KeyCodes.KEY_DOWN:
        case KeyCodes.KEY_RIGHT:
        case KeyCodes.KEY_UP:
        case KeyCodes.KEY_LEFT:
            return false;
        default:
            return true;
        }
    }

}