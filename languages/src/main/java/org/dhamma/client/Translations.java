package org.dhamma.client;

import org.dhamma.client.resource.Repository;
import org.dhamma.client.resource.Resource;
import org.dhamma.client.resource.ResourceChangeListener;
import org.dhamma.client.resource.Resources;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Translations implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        Repository repository = new Repository();
        final LocaleFactory lfactory = new LocaleFactory(repository);
        final Locale l = new Locale(repository, lfactory);
        final WordFactory wordFactory = new WordFactory(repository);

        final GetText getText = new GetText(wordFactory);
        getText.load();

        new TranslationsController(getText, new TranslationsPopupPanel(getText));

        if (true) return;

        final Label label1 = new Label("loading ...");
        final Label label2 = new Label("loading ...");
        RootPanel.get("nameFieldContainer").add(label1);
        RootPanel.get("sendButtonContainer").add(label2);

        l.country = "asd";
        l.language = "dsa";
        // l.created_at = new Timestamp(0);
        l.addResourceChangeListener(new ResourceChangeListener<Locale>() {

            public void onChange(Locale resource) {

                Resource<Locale> ll = lfactory.get(l.id, null);
                ll.removeResourceChangeListener(this);
                ll.addResourceChangeListener(new ResourceChangeListener<Locale>() {

                    public void onChange(Locale locale) {
                        label1.setText(locale.toString());

                        locale.removeResourceChangeListener(this);
                        locale.language = "rew";

                        locale.save();
                        lfactory.get(((Locale) locale).id, null);
                        locale.addResourceChangeListener(new ResourceChangeListener<Locale>() {

                            public void onChange(Locale resource) {
                                label2.setText(resource.toString());

                                // resource.destroy();
                            }
                        });
                    }
                });
            }

        });
        l.save();

        lfactory.all(null);
        Resources<Locale> r = new Resources<Locale>(lfactory);
        // r
        // .fromXml("<locales type='array'>"
        // +
        // "<locale><id>1</id><created_at>2009-07-09T17:14:48+05:30</created_at></locale>"
        // +
        // "<locale><id>2</id><created_at>2009-07-01T17:24:48+05:30</created_at></locale>"
        // + "</locales>");
        // GWT.log(r.toString(), null);
    }
}
