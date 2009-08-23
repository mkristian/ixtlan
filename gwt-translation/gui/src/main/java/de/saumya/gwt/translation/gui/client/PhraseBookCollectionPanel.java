/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import de.saumya.gwt.datamapper.client.Resources;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.PhraseBook;
import de.saumya.gwt.translation.common.client.model.PhraseBookFactory;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionPanel;
import de.saumya.gwt.translation.common.client.widget.TranslatableHyperlink;

class PhraseBookCollectionPanel extends ResourceCollectionPanel<PhraseBook> {

    private final GetTextController getTextController;

    public PhraseBookCollectionPanel(final Session session,
            final PhraseBookFactory phraseBookFactory,
            final GetTextController getTextController) {
        super(session, phraseBookFactory);
        this.getTextController = getTextController;
    }

    @Override
    protected final void reset(final Resources<PhraseBook> resources) {
        clear();
        if (this.session.isAllowed(Action.SHOW, this.resourceName)) {
            for (final PhraseBook resource : resources) {
                // TODO translate the display and append the display in brackets
                add(new TranslatableHyperlink(resource.display(),
                        getPathFactory().showPath(resource.key()),
                        this.getTextController));
            }
        }
        setVisible(true);
    }
}