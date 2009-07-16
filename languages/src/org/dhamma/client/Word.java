/**
 * 
 */
package org.dhamma.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Element;

public class Word extends Resource {

	Word(Repository repository, WordFactory factory) {
		super(repository, factory);
	}

	String code;
	String text;
	//Timestamp created_at;
	int id;

	String key() {
		return "" + id;
	}

	void appendXml(StringBuffer buf){
		if (state != State.TO_BE_CREATED) {
			append(buf, "id", "" + id);
		}
		append(buf, "code", code);
		append(buf, "text", text);
	}
	
	void fromXml(Element root) {
		id = getInt(root, "id");
		// TODO if (!isNew) {
		code = getString(root, "code");
		text = getString(root, "text");
		//created_at = getTimestamp(root, "created_at");
		// }
		GWT.log(this.toString(), null);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("Word(:id => ").append(id)
				.append(", :code => ").append(code);
		buf.append(", :text => ").append(text);
		// buf.append(", :created_at => ").append(created_at);
		return buf.append(")").toString();
	}
}