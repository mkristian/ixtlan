/**
 * 
 */
package de.saumya.gwt.datamapper.client;

import com.google.gwt.xml.client.Element;

public class Sample extends ResourceWithID<Sample> {

    Sample(Repository repository, SampleFactory factory) {
        super(repository, factory);
    }

    String language;
    String country;

    protected void appendXml(StringBuffer buf) {
        super.appendXml(buf);
        append(buf, "language", language);
        append(buf, "country", country);
    }

    protected void fromXml(Element root) {
        super.fromXml(root);
        country = getString(root, "country");
        language = getString(root, "language");
    }

    public void toString(StringBuffer buf) {
        super.toString(buf);
        buf.append(", :language => ").append(language);
        if (country != null) buf.append(", :country => ").append(country);
    }
}