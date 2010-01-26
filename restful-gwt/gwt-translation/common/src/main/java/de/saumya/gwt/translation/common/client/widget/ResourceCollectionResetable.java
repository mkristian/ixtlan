/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;

public interface ResourceCollectionResetable<E extends Resource<E>> {

    /**
     * reset the panel content with the new data
     * 
     * @param resources
     */
    void reset(final ResourceCollection<E> resources);

}