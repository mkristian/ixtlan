/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.AbstractResourceFactory;
import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.HasPathFactory;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public abstract class AbstractResourceActionPanel<E extends AbstractResource<E>>
        extends FlowPanel implements ResourceCollectionResetable<E>,
        AllowReadOnly<E>, HasPathFactory, ResourceResetable<E> {

    protected final GetTextController getTextController;

    protected final String            resourceName;

    protected PathFactory             pathFactory;

    ResourceChangeListener<E>         listener;

    public AbstractResourceActionPanel(
            final GetTextController getTextController,
            final ResourceBindings<E> binding, final Session session,
            final AbstractResourceFactory<E> factory) {
        setStyleName("resource-action-panel");
        this.getTextController = getTextController;
        this.resourceName = factory.storagePluralName();
    }

    protected TextBox boxWithButton(final ComplexPanel panel,
            final String name, final TextBoxButtonHandler handler) {
        final TextBox box = new TextBox();
        box.setStyleName(name + "-box");
        final TranslatableTextBoxButton button = new TranslatableTextBoxButton(box,
                name,
                this.getTextController);
        button.add(handler);
        panel.add(box);
        panel.add(button);
        return box;
    }

    protected <T extends ClickHandler & KeyUpHandler> Button button(
            final ComplexPanel panel, final String name, final T handler) {
        final Button button = new TranslatableButton(this.getTextController,
                name);
        button.ensureDebugId(this.resourceName + "-" + name);
        button.setVisible(false);
        button.addClickHandler(handler);
        button.addKeyUpHandler(handler);
        panel.add(button);
        return button;
    }

    @Override
    public final void setPathFactory(final PathFactory pathFactory) {
        this.pathFactory = pathFactory;
    }

    @Override
    public final PathFactory getPathFactory() {
        return this.pathFactory;
    }

}
