/**
 * 
 */
package de.saumya.gwt.translation.common.client.route;

import java.util.HashMap;
import java.util.Map;

public class ScreenDispatcher {

    private final Map<String, Screen<?>> registry = new HashMap<String, Screen<?>>();

    void register(final String controllerName, final Screen<?> screen) {
        this.registry.put(controllerName, screen);
    }

    void dispatch(final ScreenPath path) {
        if (path.controllerName != null) {
            dispatch(this.registry.get(path.controllerName), path);
        }
    }

    private void dispatch(final Screen<?> screen, final ScreenPath path) {
        dispatch(screen, path, null);
    }

    private void dispatch(final Screen<?> screen, final ScreenPath path,
            final String parentPath) {
        if (screen == null) {
            return;
        }
        if (path.child != null) {
            dispatch(screen.child(path.key), path.child, (parentPath == null
                    ? ""
                    : parentPath)
                    + "/" + path);
        }
        else {
            screen.setupPathFactory(parentPath);
            switch (path.action) {
            case NEW:
                screen.showNew();
                break;
            case SHOW:
                screen.showRead(path.key);
                break;
            case EDIT:
                screen.showEdit(path.key);
                break;
            case INDEX:
                final Map<String, String> query = new HashMap<String, String>();
                if (path.query != null) {
                    for (final String param : path.query.split("&")) {
                        final int index = param.indexOf('=');
                        if (index > 0) {
                            query.put(param.substring(0, index),
                                      param.substring(index + 1));
                        }
                    }
                }
                screen.showAll(query);
            }
        }
    }
}