/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class ResourceActionPanel<E extends Resource<E>> extends FlowPanel {

    private final GetTextController         getText;

    protected final Button                  fresh;
    protected final Button                  create;
    protected final Button                  save;
    protected final Button                  edit;
    protected final Button                  reload;
    protected final Button                  delete;

    private final ButtonHandler<E>          newHandler;
    private final ButtonHandler<E>          createHandler;
    private final ButtonHandler<E>          reloadHandler;
    private final ButtonHandler<E>          editHandler;
    private final ButtonHandler<E>          saveHandler;
    private final ButtonHandler<E>          destroyHandler;
    protected final Session                 session;

    protected final String                  resourceName;

    private final ResourceChangeListener<E> createdListener;

    private PathFactory                     pathFactory;

    public ResourceActionPanel(final GetTextController getText,
            final ResourceMutator<E> mutator, final Session session,
            final ResourceFactory<E> factory) {
        setStyleName("action-panel");
        this.getText = getText;
        this.session = session;
        this.resourceName = factory.storagePluralName();
        this.createdListener = new ResourceChangeListener<E>() {

            @Override
            public void onChange(final E resource, final String message) {
                if (resource.isUptodate()
                        && History.getToken()
                                .equals(ResourceActionPanel.this.pathFactory.newPath())) {
                    History.newItem(ResourceActionPanel.this.pathFactory.editPath(resource.key()));
                }
            }

            @Override
            public void onError(final E resource, final int status,
                    final String statusText) {
                // TODO Auto-generated method stub
            }
        };
        this.newHandler = new ButtonHandler<E>(mutator) {

            @Override
            protected void action(final E resource) {
                History.newItem(ResourceActionPanel.this.pathFactory.newPath());
            }

        };
        this.reloadHandler = new ButtonHandler<E>(mutator) {

            @Override
            protected void action(final E resource) {
                resource.reload();
            }

        };

        this.editHandler = new ButtonHandler<E>(mutator) {

            @Override
            protected void action(final E resource) {
                History.newItem(ResourceActionPanel.this.pathFactory.editPath(resource.key()));
            }

        };

        this.createHandler = new ButtonHandler<E>(mutator) {

            @Override
            protected void action(final E resource) {
                resource.addResourceChangeListener(new ResourceChangeListener<E>() {

                    @Override
                    public void onChange(final E resource, final String message) {
                        if (resource.isUptodate()
                                && History.getToken()
                                        .equals(ResourceActionPanel.this.pathFactory.newPath())) {
                            History.newItem(ResourceActionPanel.this.pathFactory.editPath(resource.key()));
                        }
                    }

                    @Override
                    public void onError(final E resource, final int status,
                            final String statusText) {
                        // TODO Auto-generated method stub
                    }
                });
                resource.save();
            }
        };
        this.saveHandler = new ButtonHandler<E>(mutator) {

            @Override
            protected void action(final E resource) {
                resource.save();
            }

        };
        this.destroyHandler = new ButtonHandler<E>(mutator) {

            @Override
            protected void action(final E resource) {
                resource.destroy();
                History.newItem(ResourceActionPanel.this.pathFactory.showAllPath());
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

        final ComplexPanel getBy = new FlowPanel();
        getBy.setStyleName("get-by");
        boxWithButton(getBy,
                      "get by " + this.resourceName + " key",
                      new TextBoxButtonHandler() {

                          @Override
                          protected void action(final TextBoxBase textBox) {
                              if (ResourceActionPanel.this.session.isAllowed(Action.UPDATE,
                                                                             ResourceActionPanel.this.resourceName)) {
                                  History.newItem(ResourceActionPanel.this.pathFactory.editPath(textBox.getText()));
                              }
                              else {
                                  History.newItem(ResourceActionPanel.this.pathFactory.showPath(textBox.getText()));
                              }
                              textBox.setText("");
                          }

                      });

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
            add(search);
            add(getBy);
        }
        add(newCreate);
        add(actionButtons);
    }

    private TextBox boxWithButton(final ComplexPanel panel, final String name,
            final TextBoxButtonHandler handler) {
        final TextBox box = new TextBox();
        box.setStyleName(name + "-box");
        final TranslatableTextBoxButton button = new TranslatableTextBoxButton(box,
                name,
                this.getText);
        button.add(handler);
        panel.add(box);
        panel.add(button);
        return box;
    }

    protected <T extends ClickHandler & KeyUpHandler> Button button(
            final ComplexPanel panel, final String name, final T handler) {
        final Button button = new TranslatableButton(name, this.getText);
        button.ensureDebugId(this.resourceName + "-" + name);
        button.setVisible(false);
        button.addClickHandler(handler);
        button.addKeyUpHandler(handler);
        panel.add(button);
        return button;
    }

    public final void setup(final PathFactory pathFactory) {
        this.pathFactory = pathFactory;
    }

    protected void doReset(final E resource) {
    }

    protected void doReset() {
    }

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