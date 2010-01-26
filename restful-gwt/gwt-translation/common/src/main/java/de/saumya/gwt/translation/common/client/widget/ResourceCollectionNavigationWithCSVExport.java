/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.RootPanel;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.translation.common.client.GetTextController;

public class ResourceCollectionNavigationWithCSVExport<E extends Resource<E>>
        extends ResourceCollectionNavigation<E> {

    private final Button export;

    private final Hidden fuzzy;
    private final Hidden query;

    public ResourceCollectionNavigationWithCSVExport(
            final LoadingNotice loadingNotice,
            final ResourceFactory<E> factory,
            final GetTextController getTextController) {
        super(loadingNotice, factory, getTextController);
        this.export = new TranslatableButton(getTextController, "export as CSV");

        // setup a form (without iframe) with hidden fields
        final ComplexPanel fields = new FlowPanel();
        this.fuzzy = new Hidden("fuzzy", this.pagination.fuzzy());
        this.query = new Hidden("query", this.pagination.rawQuery());
        fields.add(this.fuzzy);
        fields.add(this.query);

        // the null indicates no iframe and no target
        final FormPanel form = new FormPanel((String) null);
        form.setWidget(fields);

        // this might not be general enough
        form.setAction("../" + factory.storagePluralName() + ".csv");
        form.setMethod(FormPanel.METHOD_GET);

        // add the form somewhere in the page - there are no visible fields !!
        RootPanel.get().add(form);

        // submit action of the form
        this.export.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                form.submit();
            }
        });
        add(this.export);
    }

    @Override
    public void reset(final ResourceCollection<E> resources) {
        super.reset(resources);
        this.fuzzy.setValue(this.pagination.fuzzy());
        this.query.setValue(this.pagination.rawQuery());
        this.export.setEnabled(this.pagination.size > 0);
    }

}