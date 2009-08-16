/**
 * 
 */
package de.saumya.gwt.translation.common.client.route;

public class ScreenPath {

    enum Action {
        NEW, SHOW, EDIT, INDEX
    }

    public final String            controllerName;
    public final ScreenPath.Action action;
    public final String            key;
    public final ScreenPath        child;

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
                    this.child = null;
                }
                else {
                    // case "/name/id/nested"
                    this.action = null;
                    this.child = new ScreenPath(subsubpath);
                }
            }
            else {
                // case "/name/id
                this.action = Action.SHOW;
                this.key = subpath;
                this.child = null;
            }
        }
        else {
            // case "/name"
            this.controllerName = path;
            this.action = Action.INDEX;
            this.key = null;
            this.child = null;
        }
    }
}