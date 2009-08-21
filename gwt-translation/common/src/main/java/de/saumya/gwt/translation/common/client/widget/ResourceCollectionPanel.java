/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class ResourceCollectionPanel<E extends Resource<E>> extends
        VerticalPanel {

    private final Session session;

    public ResourceCollectionPanel(final Session session) {
        this.session = session;
    }

    protected final void reset(final Resources<E> resources,
            final PathFactory factory, final String resourceName) {
        clear();
        if (this.session.isAllowed(Action.UPDATE, resourceName)) {
            for (final E resource : resources) {
                add(new Hyperlink(resource.display(),
                        factory.editPath(resource.key())));
            }
        }
        else if (this.session.isAllowed(Action.RETRIEVE, resourceName)) {
            for (final E resource : resources) {
                add(new Hyperlink(resource.display(),
                        factory.showPath(resource.key())));
            }
        }
        else {
            for (final E resource : resources) {
                add(new Label(resource.display()));
            }
        }

    }
}