package org.dhamma.client;


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
		final Label label1 = new Label("loading ...");
		final Label label2 = new Label("loading ...");
		RootPanel.get("nameFieldContainer").add(label1);
		RootPanel.get("sendButtonContainer").add(label2);

		Repository repository = new Repository();
		final LocaleFactory factory = new LocaleFactory(repository);
		final Locale l = new Locale(repository, factory);

		l.country = "asd";
		l.language = "dsa";
		// l.created_at = new Timestamp(0);
		l.addResourceChangeListener(new ResourceChangeListener<Resource>() {

			@Override
			public void onChange(Resource resource) {

				Locale ll = factory.get(l.id);
				ll.removeResourceChangeListener(this);
				ll
						.addResourceChangeListener(new ResourceChangeListener<Resource>() {

							@Override
							public void onChange(Resource locale) {
								label1.setText(locale.toString());

								locale.removeResourceChangeListener(this);
								((Locale) locale).language = "rew";

								locale.save();
								factory.get(((Locale)locale).id);
								locale
										.addResourceChangeListener(new ResourceChangeListener<Resource>() {

											@Override
											public void onChange(
													Resource resource) {
												label2.setText(resource
														.toString());

												// resource.destroy();
											}
										});
							}
						});
			}

		});
		l.save();

		factory.all();
		Resources<Locale> r = new Resources<Locale>(factory);
		r.fromXml("<locales type='array'>" +
				"<locale><id>1</id><created_at>2009-07-09T17:14:48+05:30</created_at></locale>" +
				"<locale><id>2</id><created_at>2009-07-01T17:24:48+05:30</created_at></locale>" +
				"</locales>");
		GWT.log(r.toString(), null);
	}
}
