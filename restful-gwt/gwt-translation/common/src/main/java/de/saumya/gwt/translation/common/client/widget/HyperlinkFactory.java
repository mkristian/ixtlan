/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.Hyperlink;

import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;

public class HyperlinkFactory {

    public static class LocatableHyperlink extends Hyperlink implements
            Locatable {
        private final String path;

        LocatableHyperlink(final String locale, final String text,
                final String path) {
            this.path = path;
            setText(text);
            setTargetHistoryToken("/" + locale + path);
        }

        public void reset(final String locale) {
            setTargetHistoryToken("/" + locale + this.path);
        }

    }

    private final GetTextController getTextController;

    private final LocaleController  localeController;

    public HyperlinkFactory(final GetTextController getTextController,
            final LocaleController localeController) {
        this.getTextController = getTextController;
        this.localeController = localeController;
    }

    public TranslatableHyperlink newTranslatableHyperlink(final String text,
            final String path) {
        final TranslatableHyperlink link = new TranslatableHyperlink(text,
                path,
                this.getTextController);
        this.localeController.add(link);
        return link;
    }

    public Hyperlink newHyperlink(final String text, final String path) {
        final LocatableHyperlink link = new LocatableHyperlink(this.localeController.currentLocaleCode(),
                text,
                path);
        this.localeController.add(link);
        return link;
    }

    public PathFactory newPathFactory(final String locale, final String base) {
        final PathFactory pathFactory = new PathFactory(locale, base, this);
        this.localeController.add(pathFactory);
        return pathFactory;
    }

    public PathFactory newPathFactory(final String base) {
        return newPathFactory(this.localeController.currentLocaleCode(), base);
    }
}
