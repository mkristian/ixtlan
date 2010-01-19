/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;

public class DefaultResourceActionPanel<E extends Resource<E>> extends
        AbstractResourceActionPanel<E> {
    protected final Button                  fresh;
    protected final Button                  create;
    protected final Button                  save;
    protected final Button                  edit;
    protected final Button                  reload;
    protected final Button                  delete;

    private final ButtonAction<E>           newHandler;
    private final ButtonAction<E>           createHandler;
    private final ButtonAction<E>           reloadHandler;
    private final ButtonAction<E>           editHandler;
    private final ButtonAction<E>           saveHandler;
    private final ButtonAction<E>           destroyHandler;
    protected final Session                 session;

    protected final String                  resourceName;

    private final String                    defaultParameterName;
    private final ResourceChangeListener<E> createdListener;

    public DefaultResourceActionPanel(final GetTextController getText,
            final ResourceBindings<E> bindings, final Session session,
            final ResourceFactory<E> factory,
            final ResourceNotifications changeNotification) {
        super(getText, bindings, session, factory);

        setStyleName("action-panel");
        this.session = session;
        this.resourceName = factory.storagePluralName();
        this.defaultParameterName = factory.defaultSearchParameterName();
        this.createdListener = new ResourceChangeListener<E>() {

            @Override
            public void onChange(final E resource) {
                if (resource.isUptodate()
                        && History.getToken()
                                .equals(DefaultResourceActionPanel.this.pathFactory.newPath())) {
                    History.newItem(DefaultResourceActionPanel.this.pathFactory.editPath(resource.key()));
                }
            }

            @Override
            public void onError(final E resource) {
            }
        };
        this.newHandler = new ButtonAction<E>(changeNotification) {

            @Override
            protected void action(final E resource) {
                History.newItem(DefaultResourceActionPanel.this.pathFactory.newPath());
            }

        };
        this.reloadHandler = new ButtonAction<E>(changeNotification) {

            @Override
            protected void action(final E resource) {
                resource.reload();
            }

        };

        this.editHandler = new ButtonAction<E>(changeNotification) {

            @Override
            protected void action(final E resource) {
                History.newItem(DefaultResourceActionPanel.this.pathFactory.editPath(resource.key()));
            }

        };

        this.createHandler = new MutatingButtonAction<E>(changeNotification,
                bindings) {

            @Override
            protected void action(final E resource) {
                resource.addResourceChangeListener(new ResourceChangeListener<E>() {

                    @Override
                    public void onChange(final E resource) {
                        if (resource.isUptodate()
                                && History.getToken()
                                        .equals(DefaultResourceActionPanel.this.pathFactory.newPath())) {
                            History.newItem(DefaultResourceActionPanel.this.pathFactory.editPath(resource.key()));
                        }
                    }

                    @Override
                    public void onError(final E resource) {
                        // TODO Auto-generated method stub
                        GWT.log("TODO error on create resource", null);
                    }
                });
                resource.save();
            }
        };
        this.saveHandler = new MutatingButtonAction<E>(changeNotification,
                bindings) {

            @Override
            protected void action(final E resource) {
                resource.save();
            }

        };
        this.destroyHandler = new ButtonAction<E>(changeNotification) {

            @Override
            protected void action(final E resource) {
                resource.destroy();
                History.newItem(DefaultResourceActionPanel.this.pathFactory.showAllPath());
            }

        };

        final ComplexPanel search = createSearchPanel();

        final ComplexPanel getBy = createGetByPanel();

        final ComplexPanel newCreate = new FlowPanel();
        newCreate.setStyleName("new-create");
        this.fresh = button(newCreate, "new", this.newHandler);
        this.create = button(newCreate, "create", this.createHandler);
        final ComplexPanel actionButtons = new FlowPanel();
        actionButtons.setStyleName("action-buttons");
        this.reload = button(actionButtons, "reload", this.reloadHandler);
        this.edit = button(actionButtons, "edit", this.editHandler);
        this.save = button(actionButtons, "save", this.saveHandler);
        this.delete = button(actionButtons, "delete", this.destroyHandler);

        if (factory.keyName() != null) { // unless it is a singleton resource
            if (search != null) {
                add(search);
            }
            if (getBy != null) {
                add(getBy);
            }
        }
        add(newCreate);
        add(actionButtons);
    }

    protected ComplexPanel createGetByPanel() {
        final ComplexPanel getBy = new FlowPanel();
        getBy.setStyleName("get-by");
        boxWithButton(getBy,
                      "get by " + this.resourceName + " key",
                      new TextBoxButtonHandler() {

                          @Override
                          protected void action(final TextBoxBase textBox) {
                              if (DefaultResourceActionPanel.this.session.isAllowed(Action.UPDATE,
                                                                                    DefaultResourceActionPanel.this.resourceName)) {
                                  History.newItem(DefaultResourceActionPanel.this.pathFactory.editPath(textBox.getText()));
                              }
                              else {
                                  History.newItem(DefaultResourceActionPanel.this.pathFactory.showPath(textBox.getText()));
                              }
                              textBox.setText("");
                          }

                      });
        return getBy;
    }

    private void search(final TextBoxBase textBox, final boolean exact) {
        final String token = DefaultResourceActionPanel.this.pathFactory.allPath((exact
                ? "exact=&"
                : "")
                + this.defaultParameterName
                + "="
                + (textBox.getText().length() == 0 ? "*" : textBox.getText()));
        if (token.equals(History.getToken())) {
            History.fireCurrentHistoryState();
        }
        else {
            History.newItem(token);
        }
    }

    protected ComplexPanel createSearchPanel() {
        final ComplexPanel search = new FlowPanel();
        search.setStyleName("search");
        search.add(new TranslatableLabel("search", this.getText));
        final TextBox box = boxWithButton(search,
                                          "similar",
                                          new TextBoxButtonHandler() {

                                              @Override
                                              protected void action(
                                                      final TextBoxBase textBox) {
                                                  search(textBox, false);
                                              }

                                          });
        final TranslatableTextBoxButton button = new TranslatableTextBoxButton(box,
                "exact",
                this.getText);
        button.add(new TextBoxButtonHandler() {

            @Override
            protected void action(final TextBoxBase textBox) {
                search(textBox, true);
            }
        });
        search.add(button);
        return search;
    }

    // TODO move into abstract class
    @Override
    public final void reset(final E resource, final boolean readOnly) {
        this.newHandler.reset(resource);
        this.reloadHandler.reset(resource);
        this.createHandler.reset(resource);
        this.editHandler.reset(resource);
        this.saveHandler.reset(resource);
        this.destroyHandler.reset(resource);

        resource.addResourceChangeListener(this.createdListener);

        // TODO this status check needs improvement
        this.delete.setVisible(!resource.isNew() && !resource.isDeleted()
                && this.session.isAllowed(Action.DESTROY, this.resourceName));
        this.reload.setVisible(!resource.isNew() && !resource.isDeleted()
                && this.session.isAllowed(Action.SHOW, this.resourceName));
        this.edit.setVisible(readOnly && !resource.isNew()
                && !resource.isDeleted()
                && this.session.isAllowed(Action.UPDATE, this.resourceName));
        this.save.setVisible(!readOnly && !resource.isNew()
                && !resource.isDeleted()
                && this.session.isAllowed(Action.UPDATE, this.resourceName));
        this.create.setVisible(resource.isNew()
                && this.session.isAllowed(Action.CREATE, this.resourceName));
        this.fresh.setVisible(!resource.isNew()
                && this.session.isAllowed(Action.CREATE, this.resourceName));

        doReset(resource);

        setVisible(true);
    }

    // TODO move into abstract class
    @Override
    public final void reset() {
        this.create.setVisible(false);
        this.reload.setVisible(false);
        this.edit.setVisible(false);
        this.save.setVisible(false);
        this.delete.setVisible(false);
        this.fresh.setVisible(this.session.isAllowed(Session.Action.CREATE,
                                                     this.resourceName));

        doReset();

        setVisible(true);
    }
}