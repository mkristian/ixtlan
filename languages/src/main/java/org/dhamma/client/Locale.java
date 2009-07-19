/**
 * 
 */
package org.dhamma.client;

import org.dhamma.client.resource.Repository;
import org.dhamma.client.resource.ResourceWithID;

import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Element;

public class Locale extends ResourceWithID<Locale> {

    Locale(Repository repository, LocaleFactory factory) {
        super(repository, factory);
    }

    String language;
    String country;

    // Timestamp created_at;

    protected void appendXml(StringBuffer buf) {
        super.appendXml(buf);
        append(buf, "language", language);
        append(buf, "country", country);
    }

    protected void fromXml(Element root) {
        super.fromXml(root);
        // TODO if (!isNew) {
        country = getString(root, "country");
        language = getString(root, "language");
        // created_at = getTimestamp(root, "created_at");
        // }
        GWT.log(this.toString(), null);
    }

    public void toString(StringBuffer buf) {
        super.toString(buf);
        buf.append(", :language => ").append(language);
        if (country != null) buf.append(", :country => ").append(country);
        // buf.append(", :created_at => ").append(created_at);
    }
}