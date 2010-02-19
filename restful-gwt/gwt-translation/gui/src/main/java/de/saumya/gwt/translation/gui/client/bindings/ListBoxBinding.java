/**
 * 
 */
package de.saumya.gwt.translation.gui.client.bindings;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.ListBox;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionResetable;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class ListBoxBinding<T extends Resource<T>, S extends Resource<S>>
        extends ListBox implements Binding<T>, ResourceCollectionResetable<S> {

    private final Map<String, S> map = new HashMap<String, S>();

    public void reset(final ResourceCollection<S> resources) {
        clear();
        for (final S resource : resources) {
            this.map.put(resource.key(), resource);
            addItem(resource.display(), resource.key());
        }
    }

    protected S getResource() {
        final String key = getValue(getSelectedIndex());
        return this.map.get(key);
    }
    // protected void setResource(final T value) {
    // setItemSelected(index, selected)
    // final String val = value == null ? null : value.toString();
    // for (final RadioButton button : this.buttons) {
    // button.setValue(button.getFormValue().equals(val));
    // }
    // }

}