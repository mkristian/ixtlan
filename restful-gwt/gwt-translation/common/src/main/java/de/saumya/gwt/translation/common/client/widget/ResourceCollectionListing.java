/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import de.saumya.gwt.persistence.client.AbstractResourceFactory;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.HasPathFactory;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class ResourceCollectionListing<E extends Resource<E>> extends FlowPanel
        implements ResourceCollectionResetable<E>, HasPathFactory {

    protected final Session                    session;

    protected final AbstractResourceFactory<E> factory;

    private final Label                        noResult;

    private PathFactory                        pathFactory;

    private final HyperlinkFactory             hyperlinkFactory;

    public ResourceCollectionListing(final Session session,
            final AbstractResourceFactory<E> factory,
            final GetTextController getTextController,
            final HyperlinkFactory hyperlinkFactory) {
        setStylePrimaryName("resource-collection-listing");

        this.session = session;
        this.hyperlinkFactory = hyperlinkFactory;
        this.factory = factory;
        this.noResult = new TranslatableLabel(getTextController, "no "
                + factory.storagePluralName());
    }

    @Override
    public void setPathFactory(final PathFactory pathFactory) {
        this.pathFactory = pathFactory;
    }

    @Override
    public PathFactory getPathFactory() {
        return this.pathFactory;
    }

    @Override
    public void reset(final ResourceCollection<E> resources) {
        clear();
        if (resources != null) {
            if (resources.isEmpty()) {
                add(this.noResult);
            }
            else {
                setVisible(false);
                // add the collection one by one and create a link according to
                // the permission of the logged in user
                if (this.session.isAllowed(Action.UPDATE,
                                           this.factory.storagePluralName())) {
                    for (final E resource : resources) {
                        add(this.hyperlinkFactory.newHyperlink(resource.display(),
                                                               this.pathFactory.editPath(resource.id,
                                                                                         false)));
                    }
                }
                else if (this.session.isAllowed(Action.SHOW,
                                                this.factory.storagePluralName())) {
                    for (final E resource : resources) {
                        add(this.hyperlinkFactory.newHyperlink(resource.display(),
                                                               this.pathFactory.showPath(resource.id,
                                                                                         false)));
                    }
                }
                else {
                    for (final E resource : resources) {
                        add(new Label(resource.display()));
                    }
                }
                setVisible(true);
            }
        }
    }
}
