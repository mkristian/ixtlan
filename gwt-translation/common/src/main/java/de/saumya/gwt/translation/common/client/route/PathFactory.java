package de.saumya.gwt.translation.common.client.route;

import com.google.gwt.core.client.GWT;

public class PathFactory {

    private final String base;

    public PathFactory(final String base) {
        this.base = base == null ? "" : (base.startsWith("/") ? base : "/"
                + base);
        GWT.log("pathfactory " + base, null);
    }

    public PathFactory newPathFactory(final String rootPath) {
        return new PathFactory(rootPath + this.base);
    }

    public String newPath() {
        return path().append("/new").toString();
    }

    public String editPath(final String key) {
        return path().append("/").append(key).append("/edit").toString();
    }

    public String showPath(final String key) {
        return path().append("/").append(key).toString();
    }

    public String showAllPath() {
        return path().toString();
    }

    private StringBuffer path() {
        return new StringBuffer(this.base);
    }

}
