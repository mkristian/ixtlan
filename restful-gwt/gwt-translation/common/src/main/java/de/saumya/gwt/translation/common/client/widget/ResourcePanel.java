/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.persistence.client.AbstractResource;

public class ResourcePanel<E extends AbstractResource<E>> extends FlowPanel implements
        AllowReadOnly<E> {
    private final ResourceResetable<E> header;
    private final AllowReadOnly<E>     fields;

    public ResourcePanel(final ResourceHeaderPanel<E> header,
            final ResourceFields<E> fields) {
        this(header, header, fields, fields);
    }

    // TODO from a javascript point of view casting might be better then
    // multiple references to the same object
    private ResourcePanel(final ResourceResetable<E> header,
            final Widget headerWidget, final AllowReadOnly<E> fields,
            final Widget fieldsWidget) {
        this.header = header;
        this.fields = fields;
        add(headerWidget);
        add(fieldsWidget);

    }

    public <S extends Widget & ResourceResetable<E>, T extends Widget & AllowReadOnly<E>> ResourcePanel(
            final S header, final T fields) {
        this(header, header, fields, fields);
    }

    @Override
    public void setReadOnly(final boolean isReadOnly) {
        this.fields.setReadOnly(isReadOnly);
    }

    @Override
    public boolean isReadOnly() {
        return this.fields.isReadOnly();
    }

    @Override
    public final void reset(final E resource) {
        this.header.reset(resource);
        this.fields.reset(resource);
    }
}