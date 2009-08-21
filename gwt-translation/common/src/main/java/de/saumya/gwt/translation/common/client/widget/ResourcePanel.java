/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import java.util.Collection;
import java.util.HashSet;

import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.datamapper.client.Resource;

public class ResourcePanel<E extends Resource<E>> extends VerticalPanel {

    protected final Collection<AttributePanel<E>> attributes = new HashSet<AttributePanel<E>>();

    protected void add(final AttributePanel<E> attributePanel) {
        super.add(attributePanel);
        this.attributes.add(attributePanel);
    }

    @Override
    public void clear() {
        super.clear();
        this.attributes.clear();
    }

    protected void doReset(final E resource) {
    }

    protected final void reset(final E resource) {
        doReset(resource);
        for (final AttributePanel<E> attribute : this.attributes) {
            attribute.reset(resource);
        }
    }

    public void setReadOnly(final boolean isReadOnly) {
        for (final AttributePanel<E> attribute : this.attributes) {
            attribute.setReadOnly(isReadOnly);
        }
    }
}