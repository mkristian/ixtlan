package org.dhamma.client;

import java.sql.Time;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.Element;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Translations implements EntryPoint {

	public static class Locale extends Resource {

		Locale(Repository repository, LocaleFactory factory) {
			super(repository, factory);
		}

		String language;
		String country;
		Time created_at;
		int id;

		String key() {
			return "" + id;
		}

		String toXml() {
			return "<locale>" + (isNew ? "" : "<id>" + id + "</id>")
					+ "<language>" + language + "</language><country>"
					+ country + "</country>" +
					// "<created_at>" + created_at + "</created_at>" +
					"</locale>";
		}

		void fromXml(Element root) {
			id = getInt(root, "id");
			// TODO if (!isNew) {
			country = getString(root, "country");
			language = getString(root, "language");
			// created_at = getTime(root, "created_at");
			// }
			GWT.log(this.toString(), null);
		}

		public String toString() {
			StringBuffer buf = new StringBuffer("Locale(:id => ").append(id)
					.append(", :language => ").append(language);
			if (country != null)
				buf.append(", :country => ").append(country);
			// buf.append(", :created_at => ").append(created_at);
			return buf.append(")").toString();
		}
	}

	public static class LocaleFactory extends ResourceFactory {

		public LocaleFactory(Repository repository) {
			super(repository);
		}

		public String storageName() {
			return "locale";
		}

		protected Resource newResource() {
			return new Locale(repository, this);
		}

		public Locale get(int id) {
			Locale locale = new Locale(repository, this);
			get(id, locale);
			return locale;
		}

		public ResourceList<Locale> all() {
			ResourceList<Locale> list = new ResourceList<Locale>(this);
			all(list);
			return list;
		}
	}

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
				ll.addResourceChangeListener(new ResourceChangeListener<Resource>() {

					@Override
					public void onChange(Resource locale) {
						label1.setText(locale.toString());

						locale.removeResourceChangeListener(this);
						((Locale)locale).language = "rew";
						locale.addResourceChangeListener(new ResourceChangeListener<Resource>() {

							@Override
							public void onChange(Resource resource) {
								label2.setText(resource.toString());
								
								//resource.destroy();
							}
						});
						locale.save();
					}
				});
			}

		});
		l.save();

		factory.all();
	}
}
