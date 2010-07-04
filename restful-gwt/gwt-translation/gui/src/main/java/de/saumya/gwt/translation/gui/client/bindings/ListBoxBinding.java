/**
 *
 */
package de.saumya.gwt.translation.gui.client.bindings;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.ListBox;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.AbstractResourceFactory;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionResetable;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class ListBoxBinding<T extends AbstractResource<T>, S extends Resource<S>>
        extends ListBox implements Binding<T>, ResourceCollectionResetable<S> {

    private final Map<Integer, S> map = new HashMap<Integer, S>();

    protected ListBoxBinding(final boolean multiselect) {
        super(multiselect);
    }

    protected ListBoxBinding() {
        super();
    }

    public void reset(final ResourceCollection<S> resources) {
        clear();
        for (final S resource : resources) {
            this.map.put(resource.id, resource);
            addItem(resource.display(), resource.id + "");
        }
    }

    protected void selectAll(final ResourceCollection<S> resources) {
        if (resources != null) {
            for (int i = 0; i < getItemCount(); i++) {
                setItemSelected(i,
                                resources.contains(this.map.get(getValue(i))));
            }
        }
        else {
            for (int i = 0; i < getItemCount(); i++) {
                setItemSelected(i, false);
            }
        }
    }

    protected void select(final S resource) {
        if (resource != null) {
            for (int i = 0; i < getItemCount(); i++) {
                setItemSelected(i, (resource.id + "").equals(getValue(i)));
            }
        }
    }

    protected S getResource() {
        final int selected = getSelectedIndex();
        if (selected > -1) {
            final String key = getValue(selected);
            return this.map.get(key);
        }
        else {
            return null;
        }
    }

    protected ResourceCollection<S> getResources(
            final AbstractResourceFactory<S> factory) {
        final ResourceCollection<S> result = new ResourceCollection<S>(factory);
        for (int i = 0; i < getItemCount(); i++) {
            if (isItemSelected(i)) {
                result.add(this.map.get(getValue(i)));
            }
        }
        return result;
    }

}
