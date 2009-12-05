/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.Hyperlink;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class HyperlinkFactory {

    private final PathFactory factory;

    public HyperlinkFactory(final PathFactory factory) {
        this.factory = factory;
    }

    public Hyperlink editResourceHyperklink(final Resource<?> resource) {
        return new Hyperlink(resource.display(),
                this.factory.editPath(resource.key()));
    }

    public Hyperlink showResourceHyperlink(final Resource<?> resource) {
        return new Hyperlink(resource.display(),
                this.factory.showPath(resource.key()));
    }

    public Hyperlink showAllResourcesHyperlink(final String name) {
        return new Hyperlink(name, this.factory.showAllPath());
    }

    public Hyperlink newResourceHyperlink(final String name) {
        return new Hyperlink(name, this.factory.newPath());
    }
}