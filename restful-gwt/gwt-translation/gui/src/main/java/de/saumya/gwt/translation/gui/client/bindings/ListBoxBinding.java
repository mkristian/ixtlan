/**
 * 
 */
package de.saumya.gwt.translation.gui.client.bindings;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.ListBox;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionResetable;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class ListBoxBinding<T extends Resource<T>, S extends Resource<S>>
        extends ListBox implements Binding<T>, ResourceCollectionResetable<S> {

    private final Map<String, S> map = new HashMap<String, S>();

    protected ListBoxBinding(final boolean multiselect) {
        super(multiselect);
    }

    protected ListBoxBinding() {
        super();
    }

    public void reset(final ResourceCollection<S> resources) {
        clear();
        for (final S resource : resources) {
            this.map.put(resource.key(), resource);
            addItem(resource.display(), resource.key());
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
                setItemSelected(i, resource.key().equals(getValue(i)));
            }
        }
    }

    protected S getResource() {
        final String key = getValue(getSelectedIndex());
        return this.map.get(key);
    }

    protected ResourceCollection<S> getResources(
            final ResourceFactory<S> factory) {
        final ResourceCollection<S> result = new ResourceCollection<S>(factory);
        for (int i = 0; i < getItemCount(); i++) {
            if (isItemSelected(i)) {
                result.add(this.map.get(getValue(i)));
            }
        }
        return result;
    }

    // protected void setResource(final T value) {
    // setItemSelected(index, selected)
    // final String val = value == null ? null : value.toString();
    // for (final RadioButton button : this.buttons) {
    // button.setValue(button.getFormValue().equals(val));
    // }
    // }

}