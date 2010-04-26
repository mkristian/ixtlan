package de.saumya.gwt.translation.common.client.route;

import java.util.Map;

import com.google.gwt.core.client.GWT;

import de.saumya.gwt.translation.common.client.widget.HyperlinkFactory;
import de.saumya.gwt.translation.common.client.widget.Locatable;

public class PathFactory implements Locatable {

    private final String           base;
    private final HyperlinkFactory hyperlinkFactory;
    private String                 locale;

    public PathFactory(final String locale, final String base,
            final HyperlinkFactory hyperlinkFactory) {
        this.hyperlinkFactory = hyperlinkFactory;
        this.base = base == null ? "" : (base.startsWith("/") ? base : "/"
                + base);
        this.locale = locale;
        GWT.log("pathfactory " + locale + this.base, null);
    }

    public PathFactory newPathFactory(final String rootPath) {
        return this.hyperlinkFactory.newPathFactory(this.locale, rootPath
                + this.base);
    }

    public String newPath() {
        return newPath(true);
    }

    public String newPath(final boolean withLocale) {
        return path(withLocale).append("/new").toString();
    }

    public String editPath(final int key) {
        return editPath(key, true);
    }

    public String editPath(final int key, final boolean withLocale) {
        return path(withLocale).append("/")
                .append(key)
                .append("/edit")
                .toString();
    }

    public String showPath(final int key) {
        return showPath(key, true);
    }

    public String showPath(final int key, final boolean withLocale) {
        return path(withLocale).append("/").append(key).toString();
    }

    public String showAllPath() {
        return showAllPath(true);
    }

    public String showAllPath(final boolean withLocale) {
        return path(withLocale).toString();
    }

    public String allPath(final String query) {
        return allPath(query, true);
    }

    public String allPath(final String query, final boolean withLocale) {
        return path(withLocale).append("$").append(query).toString();
    }

    public String allPath(final Map<String, String> query) {
        return allPath(query, true);
    }

    public String allPath(final Map<String, String> query,
            final boolean withLocale) {
        final StringBuilder buf = path(withLocale);
        buf.append("$");
        for (final Map.Entry<String, String> entry : query.entrySet()) {
            buf.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        return buf.toString();
    }

    private StringBuilder path(final boolean withLocale) {
        return withLocale ? new StringBuilder("/").append(this.locale)
                .append(this.base) : new StringBuilder(this.base);
    }

    @Override
    public void reset(final String locale) {
        this.locale = locale;
    }

}
