/**
 * 
 */
package org.dhamma.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Element;

public class Locale extends Resource {

	Locale(Repository repository, LocaleFactory factory) {
		super(repository, factory);
	}

	String language;
	String country;
	//Timestamp created_at;
	int id;

	String key() {
		return "" + id;
	}

	void appendXml(StringBuffer buf){
		if (state != State.TO_BE_CREATED) {
			append(buf, "id", "" + id);
		}
		append(buf, "language", language);
		append(buf, "country", country);
	}
	
	void fromXml(Element root) {
		id = getInt(root, "id");
		// TODO if (!isNew) {
		country = getString(root, "country");
		language = getString(root, "language");
		//created_at = getTimestamp(root, "created_at");
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