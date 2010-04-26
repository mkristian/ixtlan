/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.RootPanel;

import de.saumya.gwt.translation.common.client.GetTextController;

public class LoadingNotice extends TranslatableLabel {
    public LoadingNotice(final GetTextController getTextController) {
        super(getTextController, "loading ...");
        setStyleName("loading-notice");
        // needs absolute positioning with CSS !!!!
        RootPanel.get().add(this);
    }
}
