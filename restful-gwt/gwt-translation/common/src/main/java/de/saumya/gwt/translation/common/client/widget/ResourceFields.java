/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public class ResourceFields<E extends AbstractResource<E>> extends FlowPanel implements
        AllowReadOnly<E> {

    protected final GetTextController getTextController;

    private final ResourceBindings<E> bindings;

    protected ResourceFields(final GetTextController getTextController,
            final ResourceBindings<E> bindings) {
        setStyleName("resource-fields");
        this.getTextController = getTextController;
        this.bindings = bindings;
    }

    public void reset(final E resource) {
        this.bindings.pullFromResource(resource);
        if (resource.isImmutable() && !resource.isNew()) {
            setReadOnly(true);
        }
        setVisible(true);
    }

    public void setReadOnly(final boolean isReadOnly) {
        this.bindings.setReadOnly(isReadOnly);
    }

    public boolean isReadOnly() {
        return this.bindings.isReadOnly();
    }

    protected void addTranslatableLabel(final String text) {
        add(new TranslatableLabel(this.getTextController, text));
    }

    protected <T extends Widget & Binding<E>> void add(final String label,
            final T widget) {
        this.bindings.add(widget);
        final ComplexPanel panel = new FlowPanel();
        final Label message = new TranslatableLabel(this.getTextController);
        panel.add(message);
        panel.add(new TranslatableLabel(this.getTextController, label));
        panel.add(widget);
        add(panel);
    }

    protected <T extends IntegerTextBox & Binding<E>> void add(
            final String label, final T textBox, final int min, final int max) {
        this.bindings.add(textBox);
        final ComplexPanel panel = new FlowPanel();
        final Label message = new TranslatableLabel(this.getTextController);
        message.setStyleName("is-ok");
        textBox.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(final KeyUpEvent event) {
                if (!textBox.isNumber()) {
                    message.setText("must be a number");
                    message.setStyleName("is-error");
                    panel.setStyleName("has-errors");
                }
                else if (textBox.getTextAsInt() > max) {
                    message.setText("exceeded maximum " + max);
                    message.setStyleName("is-error");
                    panel.setStyleName("has-errors");
                }
                else if (textBox.getTextAsInt() < min) {
                    message.setText("under minimum " + min);
                    message.setStyleName("is-error");
                    panel.setStyleName("has-errors");
                }
                else {
                    message.setText("");
                    panel.setStyleName("");
                }
            }
        });
        panel.add(message);
        panel.add(new TranslatableLabel(this.getTextController, label));
        panel.add(textBox);
        add(panel);
    }

    protected <T extends TextBoxBase & Binding<E>> void add(final String label,
            final T textBox, final int max) {
        add(label, textBox, false, max);
    }

    protected <T extends TextBoxBase & Binding<E>> void add(final String label,
            final T textBox, final boolean required, final int max) {
        this.bindings.add(textBox);
        final ComplexPanel panel = new FlowPanel();
        if (max > 64) {
            panel.addStyleName("big-box");
        }
        final Label message;
        if (required) {
            message = new TranslatableLabel(this.getTextController, "required");
        }
        else {
            message = new TranslatableLabel(this.getTextController, "");
        }
        message.setStyleName("is-ok");
        panel.add(message);
        textBox.addKeyPressHandler(new KeyPressHandler() {

            @Override
            public void onKeyPress(final KeyPressEvent event) {
                if (textBox.getText().length() >= max
                        && KeyCodesHelper.isPrintable(event.getNativeEvent()
                                .getKeyCode())) {
                    event.stopPropagation();
                    event.preventDefault();
                    panel.setStyleName("has-errors");
                    if (max > 64) {
                        panel.addStyleName("big-box");
                    }
                }
            }
        });
        textBox.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(final KeyUpEvent event) {
                final int len = ((TextBoxBase) event.getSource()).getText()
                        .length();
                if ((!required || len > 0) && len < max) {
                    if (required) {
                        message.setText("required");
                    }
                    else {
                        message.setText("");
                    }
                    message.setStyleName("is-ok");
                    panel.setStyleName("");
                    if (max > 64) {
                        panel.addStyleName("big-box");
                    }
                }
                else {
                    if (len == max) {
                        message.setText("maximum length reached");
                        message.setStyleName("is-ok");
                        panel.setStyleName("");
                        if (max > 64) {
                            panel.addStyleName("big-box");
                        }
                    }
                    else if (len > max) {
                        message.setText("maximum length exceeded");
                        message.setStyleName("is-error");
                        panel.setStyleName("has-errors");
                        if (max > 64) {
                            panel.addStyleName("big-box");
                        }
                    }
                    else {
                        message.setStyleName("is-error");
                        panel.setStyleName("has-errors");
                        if (max > 64) {
                            panel.addStyleName("big-box");
                        }
                        message.setText("required");
                    }
                }
            }
        });
        panel.add(new TranslatableLabel(this.getTextController, label));
        panel.add(textBox);
        add(panel);
    }
}
