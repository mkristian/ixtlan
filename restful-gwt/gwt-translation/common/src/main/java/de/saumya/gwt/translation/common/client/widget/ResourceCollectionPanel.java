/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class ResourceCollectionPanel<E extends Resource<E>> extends
        VerticalPanel {

    protected final Session session;

    protected final String  resourceName;

    private PathFactory     pathFactory;

    public ResourceCollectionPanel(final Session session,
            final ResourceFactory<E> factory) {
        this.session = session;
        this.resourceName = factory.storagePluralName();
    }

    protected void setup(final PathFactory pathFactory) {
        this.pathFactory = pathFactory;
    }

    protected PathFactory getPathFactory() {
        return this.pathFactory;
    }

    protected void reset(final ResourceCollection<E> resources) {
        clear();
        if (resources != null) {
            if (this.session.isAllowed(Action.UPDATE, this.resourceName)) {
                for (final E resource : resources) {
                    add(new Hyperlink(resource.display(),
                            this.pathFactory.editPath(resource.key())));
                }
            }
            else if (this.session.isAllowed(Action.SHOW, this.resourceName)) {
                for (final E resource : resources) {
                    add(new Hyperlink(resource.display(),
                            this.pathFactory.showPath(resource.key())));
                }
            }
            else {
                for (final E resource : resources) {
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