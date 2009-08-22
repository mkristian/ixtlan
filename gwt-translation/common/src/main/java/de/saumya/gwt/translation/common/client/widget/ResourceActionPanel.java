/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;

import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class ResourceActionPanel<E extends Resource<E>> extends HorizontalPanel {

    private final GetTextController getText;

    protected final Hyperlink       fresh;
    protected final Button          create;
    protected final Button          save;
    protected final Button          delete;

    private final ButtonHandler<E>  saveHandler    = new ButtonHandler<E>() {

                                                       @Override
                                                       protected void action(
                                                               final E resource) {
                                                           resource.save();
                                                       }

                                                   };
    private final ButtonHandler<E>  destroyHandler = new ButtonHandler<E>() {

                                                       @Override
                                                       protected void action(
                                                               final E resource) {
                                                           resource.destroy();
                                                       }

                                                   };

    protected final Session         session;

    protected final String          resourceName;

    public ResourceActionPanel(final GetTextController getText,
            final Session session, final ResourceFactory<E> factory) {
        this.getText = getText;
        this.session = session;
        this.resourceName = factory.storageName();
        this.fresh = linkbutton("new");
        this.create = button("create", this.saveHandler);
        this.save = button("save", this.saveHandler);
        this.delete = button("delete", this.destroyHandler);
    }

    protected Button button(final String name, final ButtonHandler<E> handler) {
        final Button button = new TranslatableButton(name, this.getText);
        button.setVisible(false);
        button.addClickHandler(handler);
        button.addKeyUpHandler(handler);
        add(button);
        return button;
    }

    protected Hyperlink linkbutton(final String name) {
        final Hyperlink button = new TranslatableHyperlink(name, this.getText);
        button.setVisible(false);
        add(button);
        return button;
    }

    public final void reset(final PathFactory pathFactory) {
        this.fresh.setTargetHistoryToken(pathFactory.newPath());
        this.fresh.setVisible(this.session.isAllowed(Session.Action.CREATE,
                                                     this.resourceName));
        this.create.setVisible(false);
        this.save.setVisible(false);
        this.delete.setVisible(false);
        setVisible(true);
    }

    protected void doReset(final E resource, final String locale) {
    }

    public final void reset(final E resource, final String locale) {
        this.saveHandler.reset(resource);
        this.destroyHandler.reset(resource);

        // TODO this stati check needs improvement
        this.delete.setVisible(!resource.isNew()
                && this.session.isAllowed(Action.DELETE, this.resourceName));
        this.save.setVisible(!resource.isNew() && !resource.isDeleted()
                && this.session.isAllowed(Action.UPDATE, this.resourceName));
        this.create.setVisible(!resource.isUptodate() && !resource.isDeleted()
                && this.session.isAllowed(Action.CREATE, this.resourceName));
        this.fresh.setVisible(false);

        doReset(resource, locale);

        setVisible(true);
    }
}