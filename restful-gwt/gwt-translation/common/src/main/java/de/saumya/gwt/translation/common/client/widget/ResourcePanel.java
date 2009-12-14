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

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.translation.common.client.GetTextController;

public class ResourcePanel<E extends Resource<E>> extends FlowPanel {

    protected final GetTextController getTextController;

    private final ResourceMutator<E>  mutator;

    protected ResourcePanel(final GetTextController getTextController,
            final ResourceMutator<E> mutator) {
        setStyleName("resource-panel");
        this.getTextController = getTextController;
        this.mutator = mutator;
    }

    protected void doReset(final E resource) {
    }

    protected final void reset(final E resource) {
        doReset(resource);
        this.mutator.pullFromResource(resource);
        setVisible(true);
    }

    public void setReadOnly(final boolean isReadOnly) {
        this.mutator.setReadOnly(isReadOnly);
    }

    public boolean isReadOnly() {
        return this.mutator.isReadOnly();
    }

    protected void addTranslatableLabel(final String text) {
        add(new TranslatableLabel(text, this.getTextController));
    }

    protected void add(final String label, final IntegerTextBox textBox,
            final int min, final int max) {
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
                    message.setText("under minimum " + max);
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
        panel.add(new TranslatableLabel(label, this.getTextController));
        panel.add(textBox);
        add(panel);
    }

    protected void add(final String label, final TextBoxBase textBox,
            final int max) {
        add(label, textBox, false, max);
    }

    protected void add(final String label, final TextBoxBase textBox,
            final boolean required, final int max) {
        final ComplexPanel panel = new FlowPanel();
        if (max > 64) {
            panel.addStyleName("big-box");
        }
        final Label message;
        if (required) {
            message = new TranslatableLabel("required", this.getTextController);
        }
        else {
            message = new TranslatableLabel("", this.getTextController);
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
        panel.add(new TranslatableLabel(label, this.getTextController));
        panel.add(textBox);
        add(panel);
    }
}