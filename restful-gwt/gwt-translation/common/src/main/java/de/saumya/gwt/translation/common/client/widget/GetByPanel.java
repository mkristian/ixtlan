/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.TextBoxBase;

import de.saumya.gwt.persistence.client.AbstractResourceFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class GetByPanel extends TextBoxWithButton {

    public GetByPanel(final GetTextController getTextController,
            final AbstractResourceFactory<?> factory, final Session session,
            final HyperlinkFactory hyperlinkFactory) {
        super(getTextController);
        final PathFactory pathFactory = hyperlinkFactory.newPathFactory(factory.storagePluralName());
        setStyleName("get-by");

        this.button.setText("get by " + factory.storageName() + " id");
        this.button.add(new TextBoxButtonHandler() {

            @Override
            protected void action(final TextBoxBase textBox) {
                if (session.isAllowed(Action.UPDATE,
                                      factory.storagePluralName())) {
                    History.newItem(pathFactory.editPath(Integer.parseInt(textBox.getText())));
                }
                else {
                    History.newItem(pathFactory.showPath(Integer.parseInt(textBox.getText())));
                }
                textBox.setText("");
            }

        });

    }
}
