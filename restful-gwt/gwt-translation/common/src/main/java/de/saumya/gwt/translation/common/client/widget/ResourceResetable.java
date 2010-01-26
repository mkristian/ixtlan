/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import de.saumya.gwt.persistence.client.Resource;

public interface ResourceResetable<E extends Resource<E>> {

    /**
     * reset the panel content with the new data
     * 
     * @param resource
     */
    void reset(final E resource);
}