/**
 *
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.Hyperlink;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.widget.HyperlinkFactory;

public class ResourceHyperlinkFactory {

    private final PathFactory      pathFactory;
    private final HyperlinkFactory hyperlinkFactory;

    public ResourceHyperlinkFactory(final HyperlinkFactory hyperlinkFactory,
            final PathFactory factory) {
        this.pathFactory = factory;
        this.hyperlinkFactory = hyperlinkFactory;
    }

    public Hyperlink editResourceHyperklink(final Resource<?> resource) {
        return this.hyperlinkFactory.newHyperlink(resource.display(),
                                                  this.pathFactory.editPath(resource.id,
                                                                            false));
    }

    public Hyperlink showResourceHyperlink(final Resource<?> resource) {
        return this.hyperlinkFactory.newHyperlink(resource.display(),
                                                  this.pathFactory.showPath(resource.id,
                                                                            false));
    }

    public Hyperlink showAllResourcesHyperlink(final String name) {
        return this.hyperlinkFactory.newHyperlink(name,
                                                  this.pathFactory.showAllPath(false));
    }

    public Hyperlink newResourceHyperlink(final String name) {
        return this.hyperlinkFactory.newHyperlink(name,
                                                  this.pathFactory.newPath(false));
    }
}
