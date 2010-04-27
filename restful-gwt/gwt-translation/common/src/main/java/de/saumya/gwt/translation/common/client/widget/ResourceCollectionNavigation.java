/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.ScreenPath;

public class ResourceCollectionNavigation<E extends Resource<E>> extends
        FlowPanel implements ResourceCollectionResetable<E> {

    static class Pagination {

        private final Map<String, String> params = new HashMap<String, String>();
        int                               limit;
        int                               offset;
        int                               size;

        private ScreenPath                screenPath;

        public void reset(final ResourceCollection<?> resources) {
            this.screenPath = new ScreenPath(History.getToken());
            this.params.clear();
            final String query = this.screenPath.query == null
                    ? ""
                    : this.screenPath.query;
            for (final String param : query.split("&")) {
                final int index = param.indexOf('=');
                if (index > -1) {
                    this.params.put(param.substring(0, index),
                                    param.substring(index + 1));
                }
            }
            this.size = resources.size();
            this.limit = this.params.containsKey("limit")
                    ? Integer.parseInt(this.params.get("limit"))
                    : 10;
            this.offset = this.params.containsKey("offset")
                    ? Integer.parseInt(this.params.get("offset"))
                    : 0;
        }

        String rawQuery() {
            return this.params.get("query");
        }

        String fuzzy() {
            return this.params.get("fuzzy");
        }

        String query() {
            final StringBuilder buf = new StringBuilder();
            for (final Map.Entry<String, String> entry : this.params.entrySet()) {
                buf.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            return buf.substring(0, buf.length() - 1);
        }

        void createHistoryToken() {
            History.newItem(this.screenPath.toString(query()), false);
        }

        boolean hasNext() {
            return this.size > this.limit;
        }

        boolean hasPrevious() {
            return this.offset > 0 && this.size > 0;
        }
    }

    abstract class PaginationActions implements ClickHandler {

        final ResourceCollectionNavigation.Pagination pagination;

        PaginationActions(
                final ResourceCollectionNavigation.Pagination pagination) {
            this.pagination = pagination;
        }

        @Override
        public void onClick(final ClickEvent event) {
            doAction();
        }

        abstract void doAction();
    }

    class PreviousAction extends PaginationActions {

        PreviousAction(final ResourceCollectionNavigation.Pagination pagination) {
            super(pagination);
        }

        /**
         * adjust the offset and load the next set of result and trigger the
         * reset of the panel
         */
        @Override
        void doAction() {
            this.pagination.params.put("offset", ""
                    + (this.pagination.offset - this.pagination.limit));
            this.pagination.createHistoryToken();
            ResourceCollectionNavigation.this.loadingNotice.setVisible(true);
            ResourceCollectionNavigation.this.factory.all(this.pagination.params,
                                                          ResourceCollectionNavigation.this.changeListener);
        }
    }

    class NextAction extends PaginationActions {
        NextAction(final ResourceCollectionNavigation.Pagination pagination) {
            super(pagination);
        }

        @Override
        void doAction() {
            this.pagination.params.put("offset", ""
                    + (this.pagination.offset + this.pagination.limit));
            this.pagination.createHistoryToken();
            ResourceCollectionNavigation.this.loadingNotice.setVisible(true);
            ResourceCollectionNavigation.this.factory.all(this.pagination.params,
                                                          ResourceCollectionNavigation.this.changeListener);
        }
    }

    private final Button               previous;
    private final Button               next;

    private final ResourceFactory<E>   factory;
    protected final LoadingNotice      loadingNotice;

    protected final Pagination         pagination = new Pagination();

    private ResourcesChangeListener<E> changeListener;

    public ResourceCollectionNavigation(final LoadingNotice loadingNotice,
            final ResourceFactory<E> factory,
            final GetTextController getTextController) {
        setStyleName("resource-collection-navigation");
        this.loadingNotice = loadingNotice;
        this.factory = factory;
        this.previous = new TranslatableButton(getTextController,
                "previous results");
        this.next = new TranslatableButton(getTextController, "next results");
        this.previous.addClickHandler(new PreviousAction(this.pagination));
        this.next.addClickHandler(new NextAction(this.pagination));
        add(this.previous);
        add(this.next);
    }

    void setChangeListener(final ResourcesChangeListener<E> changeListener) {
        this.changeListener = changeListener;
    }

    @Override
    public void reset(final ResourceCollection<E> resources) {
        this.pagination.reset(resources);
        this.previous.setEnabled(this.pagination.hasPrevious());
        this.next.setEnabled(this.pagination.hasNext());
    }

}
