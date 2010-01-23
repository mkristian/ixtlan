/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.Hyperlink;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class ResourceHyperlinkFactory {

    private final PathFactory pathFactory;

    public ResourceHyperlinkFactory(final PathFactory factory) {
        this.pathFactory = factory;
    }

    public Hyperlink editResourceHyperklink(final Resource<?> resource) {
        return new Hyperlink(resource.display(),
                this.pathFactory.editPath(resource.key()));
    }

    public Hyperlink showResourceHyperlink(final Resource<?> resource) {
        return new Hyperlink(resource.display(),
                this.pathFactory.showPath(resource.key()));
    }

    public Hyperlink showAllResourcesHyperlink(final String name) {
        return new Hyperlink(name, this.pathFactory.showAllPath());
    }

    public Hyperlink newResourceHyperlink(final String name) {
        return new Hyperlink(name, this.pathFactory.newPath());
    }
}