/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.TextBoxBase;

import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class GetByPanel extends TextBoxWithButton {

    public GetByPanel(final GetTextController getTextController,
            final ResourceFactory<?> factory, final Session session,
            final HyperlinkFactory hyperlinkFactory) {
        super(getTextController);
        final PathFactory pathFactory = hyperlinkFactory.newPathFactory(factory.storagePluralName());
        setStyleName("get-by");

        this.button.setText("get by " + factory.storageName() + " key");
        this.button.add(new TextBoxButtonHandler() {

            @Override
            protected void action(final TextBoxBase textBox) {
                if (session.isAllowed(Action.UPDATE,
                                      factory.storagePluralName())) {
                    History.newItem(pathFactory.editPath(textBox.getText()));
                }
                else {
                    History.newItem(pathFactory.showPath(textBox.getText()));
                }
                textBox.setText("");
            }

        });

    }
}