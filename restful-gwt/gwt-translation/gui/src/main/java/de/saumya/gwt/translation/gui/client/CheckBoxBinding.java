/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.CheckBox;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class CheckBoxBinding<T extends Resource<T>> extends CheckBox
        implements Binding<T> {
}