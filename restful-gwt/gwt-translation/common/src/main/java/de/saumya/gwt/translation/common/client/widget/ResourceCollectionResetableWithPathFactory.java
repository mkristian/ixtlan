/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.translation.common.client.route.HasPathFactory;

public interface ResourceCollectionResetableWithPathFactory<E extends Resource<E>>
        extends ResourceCollectionResetable<E>, HasPathFactory {

    /**
     * reset the panel content with the new data
     * 
     * @param resources
     */
    void reset(final ResourceCollection<E> resources);

}