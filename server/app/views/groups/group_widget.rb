class Views::Groups::GroupWidget < ErectorWidgets::EntityWidget

  def entity_symbol 
    :group
  end
 
  def title
    if @group.new_record?
      "new group"
    else
      "group #{@group.name}"
    end
  end

  def render_navigation(disabled)
    super
    if disabled and not @group.new_record?
      if allowed(:groups, :edit)
        div :class => :nav_buttons do
          button_to 'edit', edit_group_path(@group.id), :method => :get, :class => :button
        end
      end
      if allowed(:groups, :new)
        div :class => :nav_buttons do
          button_to 'new', new_group_path, :method => :get, :class => :button
        end
      end
    end
  end

#   alias :form_for_old :form_for
#   def form_for(*args, &block)
#     p args.inspect
#     form_for_old(*args) do |f|
#       wrapper = FormWrapper.new
#       wrapper.form = f
#       wrapper.widget = self
#       block.call(wrapper)
#     end
#   end

  def render_entity(disabled)
    args = 
      if @group.new_record?
        {:url => groups_path.to_s, :html => {:method => :post}}
      elsif disabled
        {:url => edit_group_path(@group.id).to_s, :html => {:method => :get} }
      else
        {:url => group_path(@group.id).to_s, :html => {:method => :put} }
      end
    
    form_for(:group, args) do |f|
      div :class => :second do
        b "Name"
        br
        rawtext(f.text_field(:name, :disabled => disabled))
      end
      
      div :class => :first do
        b "Description"
        br
        rawtext(f.text_area(:description, :disabled => disabled))
      end
      
      unless disabled
        div :class => :action_button do
          if @group.new_record?
            rawtext(f.submit("Create"))
          else
            rawtext(f.submit("Update"))
          end
        end
      end
    end
  end
end
# class FormWrapper

#   attr_writer :widget, :form

#   def method_missing(sym, *args, &block)
#     @widget.rawtext @form.send(sym, *args, &block)
#   end
# end
