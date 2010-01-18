/**
 * 
 */
package de.saumya.gwt.translation.common.client.route;

import com.google.gwt.core.client.GWT;

public class ScreenPath {

    enum Action {
        NEW, SHOW, EDIT, INDEX
    }

    public final String            controllerName;
    public final ScreenPath.Action action;
    public final String            key;
    public final ScreenPath        child;
    public final String            query;

    /**
     * <ul>
     * <li>/name</li>
     * <li>/name/new</li>
     * <li>/name/1</li>
     * <li>/name/1/edit</li>
     * <li>/name/1/nested</li>
     * <li>/name/1/nested/new</li>
     * <li>/name/1/nested/3</li>
     * <li>/name/1/nested/3/edit</li>
     * </ul>
     * 
     * a key with with value <b>new</b> will produce the wrong action and a
     * controllerName with <b>edit</b> will produce the wrong action on the
     * wrong controller !
     * 
     * @param fullpath
     */
    public ScreenPath(final String fullpath) {
        final String path = fullpath.substring(1);
        if (path.contains("/")) {
            this.controllerName = path.substring(0, path.indexOf("/"));
            final String subpath = path.substring(path.indexOf("/") + 1);
            if (subpath.equals("new")) {
                // case "/name/new"
                this.action = Action.NEW;
                this.query = null;
                this.key = null;
                this.child = null;
            }
            else if (subpath.contains("/")) {
                // case "/name/id/edit" or "/name/id/nested"
                this.key = subpath.substring(0, subpath.indexOf("/"));
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
                this.key = subpath;
                this.query = null;
                this.child = null;
            }
        }
        else {
            // case "/name"
            this.controllerName = path.replaceFirst("[$].*", "");
            // assume NO $-character in query
            this.query = path.replaceFirst(".*[$]", "");
            this.action = Action.INDEX;
            this.key = null;
            this.child = null;
            GWT.log(this.controllerName + " <=> " + this.query, null);
        }
    }

    @Override
    public String toString() {
        // TODO use stringbuffer and apply all cases properly
        return "/" + this.controllerName
                + (this.key != null ? "/" + this.key : "")
                + (this.query != null ? "$" + this.query : "");
    }
}