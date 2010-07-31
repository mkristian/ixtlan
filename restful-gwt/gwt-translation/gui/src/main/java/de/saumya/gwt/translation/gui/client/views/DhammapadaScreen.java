/**
 * 
 */
package de.saumya.gwt.translation.gui.client.views;

import java.util.Map;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.TranslatableLabel;
import de.saumya.webfortune.client.QuoteButtons;
import de.saumya.webfortune.client.QuoteController;
import de.saumya.webfortune.client.QuoteHTML;
import de.saumya.webfortune.client.TopicListLoader;
import de.saumya.webfortune.client.TopicListPanel;

@SuppressWarnings("unchecked")
public class DhammapadaScreen extends FlowPanel implements Screen {

    private final QuoteHTML       quoteHtml  = new QuoteHTML();
    private final QuoteController controller = new QuoteController(this.quoteHtml);
    private final QuoteButtons    buttons    = new QuoteButtons(this.controller);
    private final TopicListPanel  topicList  = new TopicListPanel(this.controller,
                                                     this.buttons);

    public DhammapadaScreen(final GetTextController getTextController) {
        setStyleName("dhammapada");
        new TopicListLoader(this.topicList).load("topics.xml");
        final Label head = new TranslatableLabel(getTextController,
                "Dhammapada Quotes");
        head.setStyleName("header");
        add(head);
        add(this.topicList);
        add(this.buttons);
        add(this.quoteHtml);
    }

    @Override
    public Screen child(final int parentKey) {
        return null;
    }

    @Override
    public void setupPathFactory(final String parentPath) {
    }

    @Override
    public void showAll(final Map query) {
    }

    @Override
    public void showEdit(final int key) {
    }

    @Override
    public void showNew() {
    }

    @Override
    public void showRead(final int key) {
    }

    @Override
    public PathFactory getPathFactory() {
        return null;
    }

    @Override
    public void setPathFactory(final PathFactory pathFactory) {
    }

}