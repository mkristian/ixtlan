/**
 *
 */
package de.saumya.gwt.translation.gui.client.bindings;

import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class TextBoxBinding<E extends AbstractResource<E>> extends TextBox
        implements Binding<E> {

}
