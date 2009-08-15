/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.datamapper.client.ResourceChangeListener;
import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.Phrase;
import de.saumya.gwt.translation.common.client.PhraseFactory;

class PhraseScreen extends ResourceScreen<Phrase> implements Screen {

    private final PhraseFactory     phraseFactory;

    private final TranslatableLabel loading;

    PhraseScreen(final GetText getText, final PhraseFactory phraseFactory) {
        super(getText);
        this.phraseFactory = phraseFactory;
        this.loading = new TranslatableLabel("loading", getText);
        setVisible(false);

        add(new AttributePanel<Phrase>("to be approved", true, getText) {

            @Override
            String value(final Phrase resource) {
                return resource.toBeApproved;
            }

        });
    }

    @SuppressWarnings("unchecked")
    private void setReadOnly(final boolean isReadOnly) {
        for (final Widget panel : getChildren()) {
            if (panel instanceof AttributePanel) {
                ((AttributePanel<Phrase>) panel).setReadOnly(isReadOnly);
            }
        }
    }

    @Override
    protected void reset(final Phrase resource) {
        super.reset(resource, resource.updatedAt, resource.updatedBy);
    }

    @Override
    public Screen child(final String key) {
        throw new UnsupportedOperationException("phrase has no child");
    }

    @Override
    public void showAll() {
        throw new UnsupportedOperationException("phrase does not show list");
    }

    @Override
    public void showEdit(final String key) {
        setReadOnly(false);
        show(key);
    }

    @Override
    public void showNew() {
        reset(this.phraseFactory.newResource());
        PhraseScreen.this.loading.setVisible(false);
        setVisible(true);
    }

    @Override
    public void showRead(final String key) {
        setReadOnly(true);
        show(key);
    }

    private void show(final String key) {
        this.loading.setVisible(true);
        setVisible(true);
        final Phrase phrase = this.phraseFactory.get(key,
                                                     new ResourceChangeListener<Phrase>() {

                                                         @Override
                                                         public void onChange(
                                                                 final Phrase resource) {
                                                             reset(resource);
                                                             PhraseScreen.this.loading.setVisible(false);
                                                         }
                                                     });
        reset(phrase);
    }
}