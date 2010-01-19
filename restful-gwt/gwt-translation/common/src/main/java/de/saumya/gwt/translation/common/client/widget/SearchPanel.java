/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.TextBoxBase;

import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class SearchPanel extends TextBoxWithButton {

    private final PathFactory pathFactory;
    private final String      defaultSearchParameterName;

    public SearchPanel(final GetTextController getTextController,
            final ResourceFactory<?> factory) {
        super(getTextController);
        this.pathFactory = new PathFactory(factory.storagePluralName());
        this.defaultSearchParameterName = factory.defaultSearchParameterName();
        setStyleName("search");
        add(new TranslatableLabel("search", getTextController));

        this.button.setText("similar");
        this.button.add(new TextBoxButtonHandler() {

            @Override
            protected void action(final TextBoxBase textBox) {
                search(textBox, false);
            }

        });
        final TranslatableTextBoxButton button = new TranslatableTextBoxButton(this.box,
                "exact",
                getTextController);
        button.add(new TextBoxButtonHandler() {

            @Override
            protected void action(final TextBoxBase textBox) {
                search(textBox, true);
            }
        });
        add(button);
    }

    private void search(final TextBoxBase textBox, final boolean exact) {
        final String token = this.pathFactory.allPath((exact
                ? "exact=&"
                : "")
                + this.defaultSearchParameterName
                + "="
                + (textBox.getText().length() == 0
                        ? "*"
                        : textBox.getText()));
        if (token.equals(History.getToken())) {
            History.fireCurrentHistoryState();
        }
        else {
            History.newItem(token);
        }
        textBox.setSelectionRange(0, textBox.getText().length());
    }
}