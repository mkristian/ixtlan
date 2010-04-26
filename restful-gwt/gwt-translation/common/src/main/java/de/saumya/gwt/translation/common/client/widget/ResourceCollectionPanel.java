/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;
import de.saumya.gwt.translation.common.client.route.HasPathFactory;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class ResourceCollectionPanel<E extends Resource<E>> extends FlowPanel
        implements ResourceCollectionResetable<E>, HasPathFactory {

    private final ResourceCollectionResetable<E> navigation;
    private final ResourceCollectionResetable<E> listing;
    private final HasPathFactory                 listingPathFactory;
    private final Widget                         loadingNotice;

    public ResourceCollectionPanel(final LoadingNotice loadingNotice,
            final ResourceCollectionNavigation<E> navigation,
            final ResourceCollectionListing<E> listing) {
        this(loadingNotice, navigation, listing, listing, listing);
    }

    // TODO from a javascript point of view casting might be better then
    // multiple references to the same object
    private ResourceCollectionPanel(final LoadingNotice loadingNotice,
            final ResourceCollectionNavigation<E> navigation,
            final ResourceCollectionResetable<E> listing,
            final HasPathFactory listingPathFactory, final Widget listingWidget) {
        setStylePrimaryName("resource-collection-panel");

        this.loadingNotice = loadingNotice;
        this.navigation = navigation;
        this.listing = listing;
        this.listingPathFactory = listingPathFactory;
        navigation.setChangeListener(new ResourcesChangeListener<E>() {

            @Override
            public void onLoaded(final ResourceCollection<E> resources) {
                reset(resources);
            }
        });

        add(navigation);
        add(listingWidget);
    }

    public <T extends Widget & ResourceCollectionResetable<E> & HasPathFactory> ResourceCollectionPanel(
            final LoadingNotice loadingNotice,
            final ResourceCollectionNavigation<E> navigation, final T listing) {
        this(loadingNotice, navigation, listing, listing, listing);
    }

    @Override
    public void setPathFactory(final PathFactory pathFactory) {
        this.listingPathFactory.setPathFactory(pathFactory);
    }

    @Override
    public PathFactory getPathFactory() {
        return this.listingPathFactory.getPathFactory();
    }

    @Override
    public void reset(final ResourceCollection<E> resources) {
        setVisible(resources != null);
        this.navigation.reset(resources);
        this.listing.reset(resources);
        this.loadingNotice.setVisible(false);
    }
}
