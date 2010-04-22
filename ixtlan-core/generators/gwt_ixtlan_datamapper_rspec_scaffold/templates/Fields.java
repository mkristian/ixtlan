/**
 *
 */
package <%= package %>.views.<%= plural_name %>;

import <%= package %>.models.<%= class_name %>;

import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.gui.client.bindings.CheckBoxBinding;
import de.saumya.gwt.translation.gui.client.bindings.IntegerTextBoxBinding;
import de.saumya.gwt.translation.gui.client.bindings.TextBoxBinding;

public class <%= class_name %>Fields extends ResourceFields<<%= class_name %>> {

    public <%= class_name %>Fields(final GetTextController getTextController,
            final ResourceBindings<<%= class_name %>> bindings) {
        super(getTextController, bindings);
<% Array(attributes).each do |attribute| -%>
        add("<%= attribute.name %>", new <% if attribute.type == :integer %>IntegerText<% elsif attribute.type == :boolean %>Check<% else %>Text<% end -%>BoxBinding<<%= class_name %>>() {

            @Override
            public void pullFrom(final <%= class_name %> resource) {
<% if attribute.type == :boolean -%>
                setValue(resource.<%= attribute.name.javanize %>);
<% else -%>
                setText(resource.<%= attribute.name.javanize %>);
<% end -%>
            }

            @Override
            public void pushInto(final <%= class_name %> resource) {
<% if attribute.type == :boolean -%>
                resource.<%= attribute.name.javanize %> = getValue();
<% else -%>
                resource.<%= attribute.name.javanize %> = getText<% if attribute.type == :integer %>AsInt<% end -%>();
<% end -%>
            }
        }<% if attribute.type == :integer %>, 0, 123456<% elsif attribute.type == :boolean %><% else %>, true, 64<% end -%>);
<% end -%>
    }
}
