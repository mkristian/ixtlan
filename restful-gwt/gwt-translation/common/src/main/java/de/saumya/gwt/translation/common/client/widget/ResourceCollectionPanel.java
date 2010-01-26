/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.FlowPanel;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class ResourceCollectionPanel<E extends Resource<E>> extends FlowPanel
        implements ResourceCollectionResetable<E> {

    private final ResourceCollectionNavigation<E> navigation;
    private final ResourceCollectionListing<E>    listing;

    public ResourceCollectionPanel(
            final ResourceCollectionNavigation<E> navigation,
            final ResourceCollectionListing<E> listing) {
        setStylePrimaryName("resource-collection-panel");

        this.navigation = navigation;
        this.listing = listing;
        this.navigation.setChangeListener(new ResourcesChangeListener<E>() {

            @Override
            public void onLoaded(final ResourceCollection<E> resources) {
                reset(resources);
            }
        });

        add(this.navigation);
        add(this.listing);
    }

    protected void setup(final PathFactory pathFactory) {
        this.listing.setup(pathFactory);
    }

    // protected PathFactory getPathFactory() {
    // return this.pathFactory;
    // }

    @Override
    public void reset(final ResourceCollection<E> resources) {
        setVisible(resources != null);
        this.navigation.reset(resources);
        this.listing.reset(resources);
    }
}