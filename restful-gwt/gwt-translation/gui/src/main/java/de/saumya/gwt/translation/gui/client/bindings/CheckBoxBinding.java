/**
 * 
 */
package de.saumya.gwt.translation.gui.client.bindings;

import com.google.gwt.user.client.ui.CheckBox;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class CheckBoxBinding<T extends AbstractResource<T>> extends CheckBox
        implements Binding<T> {
}