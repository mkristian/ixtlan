/**
 * 
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceWithID;

public class Sample extends ResourceWithID<Sample> {

    private final SampleFactory factory;

    Sample(final Repository repository, final SampleFactory factory) {
        super(repository, factory, null);
        this.factory = factory;
    }

    String language;
    String country;

    Sample child;

    @Override
    protected void appendXml(final StringBuffer buf) {
        super.appendXml(buf);
        append(buf, "language", this.language);
        append(buf, "country", this.country);
        append(buf, "child", this.child);
    }

    @Override
    protected void fromXml(final Element root) {
        super.fromXml(root);
        this.country = getString(root, "country");
        this.language = getString(root, "language");
        this.child = this.factory.getChildResource(root, "child");
    }

    @Override
    public void toString(final StringBuffer buf) {
        super.toString(buf);
        buf.append(", :language => ").append(this.language);
        if (this.country != null) {
            buf.append(", :country => ").append(this.country);
        }
        if (this.child != null) {
            buf.append(", :child => ");
            this.child.toString(buf);
        }
    }

    @Override
    public String display() {
        return "sample(" + this.language + "_" + this.country + ")";
    }
}