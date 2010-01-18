/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.widget.AbstractResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.ButtonAction;
import de.saumya.gwt.translation.common.client.widget.MutatingButtonAction;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.TextBoxButtonHandler;
import de.saumya.gwt.translation.common.client.widget.TranslatableLabel;
import de.saumya.gwt.translation.common.client.widget.TranslatableTextBoxButton;

public class PhraseActionPanel extends AbstractResourceActionPanel<Phrase> {

    private final GetTextController    getText;

    protected final Button             save;
    protected final Button             edit;
    protected final Button             reload;

    private final ButtonAction<Phrase> reloadHandler;
    private final ButtonAction<Phrase> editHandler;
    private final ButtonAction<Phrase> saveHandler;
    protected final Session            session;

    protected final String             resourceName;

    private PathFactory                pathFactory;

    public PhraseActionPanel(final GetTextController getText,
            final ResourceBindings<Phrase> binding, final Session session,
            final ResourceFactory<Phrase> factory) {
        super(getText, binding, session, factory);

        setStyleName("action-panel");
        this.getText = getText;
        this.session = session;
        this.resourceName = factory.storagePluralName();
        this.reloadHandler = new ButtonAction<Phrase>() {

            @Override
            protected void action(final Phrase resource) {
                resource.reload();
            }

        };

        this.editHandler = new ButtonAction<Phrase>() {

            @Override
            protected void action(final Phrase resource) {
                History.newItem(PhraseActionPanel.this.pathFactory.editPath(resource.key()));
            }

        };

        this.saveHandler = new MutatingButtonAction<Phrase>(binding) {

            @Override
            protected void action(final Phrase resource) {
                resource.save();
            }

        };

        final ComplexPanel search = new FlowPanel();
        search.setStyleName("search");
        search.add(new TranslatableLabel("search", getText));
        final TextBox box = boxWithButton(search,
                                          "similar",
                                          new TextBoxButtonHandler() {

                                              @Override
                                              protected void action(
                                                      final TextBoxBase textBox) {
                                                  // TODO
                                                  GWT.log("TODO fuzzy search",
                                                          null);
                                              }
                                          });
        final TranslatableTextBoxButton button = new TranslatableTextBoxButton(box,
                "exact",
                this.getText);
        button.add(new TextBoxButtonHandler() {

            @Override
            protected void action(final TextBoxBase textBox) {
                // TODO
                GWT.log("TODO exact search", null);
            }
        });
        search.add(button);

        // session.getUser().getAllowedLocales();

        final ComplexPanel actionButtons = new FlowPanel();
        actionButtons.setStyleName("action-buttons");
        this.reload = button(actionButtons, "reload", this.reloadHandler);
        this.edit = button(actionButtons, "edit", this.editHandler);
        this.save = button(actionButtons, "save", this.saveHandler);

        add(search);
        add(actionButtons);
    }

    @Override
    public final void reset(final Phrase resource, final boolean readOnly) {
        this.reloadHandler.reset(resource);
        this.editHandler.reset(resource);
        this.saveHandler.reset(resource);

        // TODO this status check needs improvement
        this.reload.setVisible(!resource.isNew() && !resource.isDeleted()
                && this.session.isAllowed(Action.SHOW, this.resourceName));
        this.edit.setVisible(readOnly && !resource.isNew()
                && !resource.isDeleted()
                && this.session.isAllowed(Action.UPDATE, this.resourceName));
        this.save.setVisible(!readOnly && !resource.isNew()
                && !resource.isDeleted()
                && this.session.isAllowed(Action.UPDATE, this.resourceName));

        doReset(resource);

        setVisible(true);
    }

    @Override
    public final void reset() {
        this.reload.setVisible(false);
        this.edit.setVisible(false);
        this.save.setVisible(false);

        doReset();

        setVisible(true);
    }
}