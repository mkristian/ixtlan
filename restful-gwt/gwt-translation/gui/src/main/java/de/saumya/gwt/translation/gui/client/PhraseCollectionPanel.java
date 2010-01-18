/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;

import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionPanel;

class PhraseCollectionPanel extends ResourceCollectionPanel<Phrase> {

    private String locale;

    public PhraseCollectionPanel(final Session session,
            final PhraseFactory phraseFactory) {
        super(session, phraseFactory);
    }

    void setLocale(final String locale) {
        this.locale = locale;
    }

    @Override
    protected final void reset(final ResourceCollection<Phrase> resources) {
        clear();
        if (resources != null) {
            if (this.session.isAllowed(Action.UPDATE,
                                       this.resourceName,
                                       this.locale)) {
                for (final Phrase resource : resources) {
                    add(new Hyperlink(resource.display(),
                            getPathFactory().editPath(resource.key())));
                }

            }
            else if (this.session.isAllowed(Action.SHOW, this.resourceName)) {
                for (final Phrase resource : resources) {
                    add(new Hyperlink(resource.display(),
                            getPathFactory().showPath(resource.key())));
                }
            }
            else {
                for (final Phrase resource : resources) {
                    add(new Label(resource.display()));
                }
            }
            setVisible(true);
        }
        else {
            setVisible(false);
        }
    }
}