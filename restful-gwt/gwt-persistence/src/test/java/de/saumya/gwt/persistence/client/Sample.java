/**
 * 
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.xml.client.Element;

public class Sample extends Resource<Sample> {

    private final SampleFactory factory;

    Sample(final Repository repository, final SampleFactory factory,
            final int id) {
        super(repository, factory, id);
        this.factory = factory;
    }

    String language;
    String country;

    Sample child;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "language", this.language);
        appendXml(buf, "country", this.country);
        appendXml(buf, "child", this.child);
    }

    @Override
    protected void fromElement(final Element root) {
        this.country = getString(root, "country");
        this.language = getString(root, "language");
        this.child = this.factory.getChildResource(root, "child");
    }

    @Override
    public void toString(final StringBuilder buf) {
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

    // @Override
    // protected void appendXml(final StringBuilder buf, final String name,
    // final Sample value) {
    // // TODO Auto-generated method stub
    //
    // }
}