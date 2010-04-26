/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import de.saumya.gwt.persistence.client.AbstractResource;

public interface ResourceResetable<E extends AbstractResource<E>> {

    /**
     * reset the panel content with the new data
     * 
     * @param resource
     */
    void reset(final E resource);
}