/**
 *
 */
package de.saumya.gwt.translation.common.client.route;

import de.saumya.gwt.session.client.models.Locale;

public class ScreenPath {

    enum Action {
        NEW, SHOW, EDIT, INDEX
    }

    public final String            locale;
    public final String            controllerName;
    public final ScreenPath.Action action;
    public final int               key;
    public final ScreenPath        child;
    public final String            query;

    private ScreenPath(final String locale, final String controller,
            final Action action, final int key, final ScreenPath child,
            final String query) {
        this.locale = locale;
        this.controllerName = controller;
        this.action = action;
        this.key = key;
        this.child = child;
        this.query = query;
    }

    /**
     * <ul>
     * <li>/locale/name</li>
     * <li>/locale/name/new</li>
     * <li>/locale/name/1</li>
     * <li>/locale/name/1/edit</li>
     * <li>/locale/name/1/nested</li>
     * <li>/locale/name/1/nested/new</li>
     * <li>/locale/name/1/nested/3</li>
     * <li>/locale/name/1/nested/3/edit</li>
     * </ul>
     * 
     * a key with with value <b>new</b> will produce the wrong action and a
     * controllerName with <b>edit</b> will produce the wrong action on the
     * wrong controller !
     * 
     * @param fullpath
     */
    public ScreenPath(final String fullpath) {
        String path = fullpath == null || fullpath.length() <= 1
                ? Locale.DEFAULT_CODE
                : fullpath.substring(1);
        if (path.contains("/")) {
            final int index = path.indexOf("/");
            this.locale = path.substring(0, index);
            path = path.substring(index + 1);
            if (path.contains("/")) {
                this.controllerName = path.substring(0, path.indexOf("/"));
                final String subpath = path.substring(path.indexOf("/") + 1);
                if (subpath.equals("new")) {
                    // case "/name/new"
                    this.action = Action.NEW;
                    this.query = null;
                    this.key = 0;
                    this.child = null;
                }
                else if (subpath.contains("/")) {
                    // case "/name/id/edit" or "/name/id/nested"
                    this.key = Integer.parseInt(subpath.substring(0,
                                                                  subpath.indexOf("/")));
                    final String subsubpath = subpath.substring(subpath.indexOf("/") + 1);
                    if (subsubpath.equals("edit")) {
                        // case "/name/id/edit"
                        this.action = Action.EDIT;
                        this.query = null;
                        this.child = null;
                    }
                    else {
                        // case "/name/id/nested"
                        this.query = null;
                        this.action = null;
                        this.child = new ScreenPath(subsubpath);
                    }
                }
                else {
                    // case "/name/id
                    this.action = Action.SHOW;
                    this.key = Integer.parseInt(subpath);
                    this.query = null;
                    this.child = null;
                }
            }
            else {
                // case "/name"
                this.controllerName = path.replaceFirst("[$].*", "");
                // assume NO $-character in query
                this.query = path.contains("$")
                        ? path.replaceFirst(".*[$]", "")
                        : "";
                this.action = Action.INDEX;
                this.key = 0;
                this.child = null;
            }
        }
        else {
            this.locale = path;
            this.action = null;
            this.controllerName = null;
            this.query = null;
            this.key = 0;
            this.child = null;
        }
    }

    public ScreenPath switchLocale(final String locale) {
        if (this.locale.equals(locale)) {
            return this;
        }
        else {
            return new ScreenPath(locale,
                    this.controllerName,
                    this.action,
                    this.key,
                    this.child,
                    this.query);
        }
    }

    public String toString(final String query) {
        final StringBuilder result = new StringBuilder("/");
        result.append(this.locale);
        if (this.controllerName != null) {
            result.append("/").append(this.controllerName);
        }
        if (this.key != 0) {
            result.append("/").append(this.key);
        }
        if (this.action == Action.EDIT || this.action == Action.NEW) {
            result.append("/").append(this.action.toString().toLowerCase());
        }
        if (query != null && query.length() > 0) {
            result.append("$").append(query);
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return toString(this.query);
    }
}
