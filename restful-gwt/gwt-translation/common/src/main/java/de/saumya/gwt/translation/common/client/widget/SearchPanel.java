/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.TextBoxBase;

import de.saumya.gwt.persistence.client.AbstractResourceFactory;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class SearchPanel extends TextBoxWithButton {

    private final PathFactory pathFactory;
    private final String      defaultSearchParameterName;

    public SearchPanel(final GetTextController getTextController,
            final AbstractResourceFactory<?> factory,
            final HyperlinkFactory hyperlinkFactory) {
        super(getTextController);
        this.pathFactory = hyperlinkFactory.newPathFactory(factory.storagePluralName());
        this.defaultSearchParameterName = factory.defaultSearchParameterName();
        setStyleName("search");
        insert(new TranslatableLabel(getTextController, "search"), 0);

        this.button.setText("similar");
        this.button.add(new TextBoxButtonHandler() {

            @Override
            protected void action(final TextBoxBase textBox) {
                search(textBox, true);
            }

        });
        final TranslatableTextBoxButton button = new TranslatableTextBoxButton(this.box,
                "exact",
                getTextController);
        button.add(new TextBoxButtonHandler() {

            @Override
            protected void action(final TextBoxBase textBox) {
                search(textBox, false);
            }
        });
        add(button);
    }

    private void search(final TextBoxBase textBox, final boolean fuzzy) {
        final String token = this.pathFactory.allPath("limit=10&fuzzy=" + fuzzy
                + "&" + this.defaultSearchParameterName + "="
                + (textBox.getText().length() == 0 ? "" : textBox.getText()));
        if (token.equals(History.getToken())) {
            History.fireCurrentHistoryState();
        }
        else {
            History.newItem(token);
        }
        textBox.setSelectionRange(0, textBox.getText().length());
    }
}
