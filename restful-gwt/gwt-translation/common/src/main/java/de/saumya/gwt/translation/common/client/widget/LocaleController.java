/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.translation.common.client.route.ScreenPath;

public class LocaleController {

    private final Set<Locatable> locatables = new HashSet<Locatable>();
    private final LocaleFactory  localeFactory;

    public LocaleController(final LocaleFactory localeFactory) {
        this.localeFactory = localeFactory;
        History.addValueChangeHandler(new ValueChangeHandler<String>() {

            @Override
            public void onValueChange(final ValueChangeEvent<String> event) {
                final ScreenPath path = new ScreenPath(event.getValue());
                final String locale = path.locale;
                for (final Locatable locatable : LocaleController.this.locatables) {
                    locatable.reset(locale);
                }
            }
        });
    }

    public void add(final Locatable locatable) {
        this.locatables.add(locatable);
    }

    public void reset(final String locale) {
        final ScreenPath path = new ScreenPath(History.getToken());
        if (!path.locale.equals(locale)) {
            History.newItem(path.switchLocale(locale).toString());
            for (final Locatable locatable : this.locatables) {
                locatable.reset(locale);
            }
        }
    }

    public String currentLocaleCode() {
        return new ScreenPath(History.getToken()).locale;
    }

    public Locale currentLocale() {
        final ScreenPath path = new ScreenPath(History.getToken());
        final Locale locale = this.localeFactory.first(path.locale);
        return locale;
    }

    public void reset(final Locale locale) {
        reset(locale.code);
    }
}