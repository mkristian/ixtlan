class ErectorWidgets::SortableTableWidget < Erector::Widget

  def initialize(view, assigns, io, 
                 entities_symbol, entities, 
                 headers, sorted_field, direction)
    super(view, assigns, io)
    @__entities_symbol = entities_symbol
    @__entities = entities
    @__headers = headers
    @__field = sorted_field
    @__direction = direction
  end

  def link_args(entity)
    raise "not implemented"
  end
  
  def delete_form(entity)
    raise "not implemented"
  end

  def render
    table do
      thead do
        tr do
          render_table_header
        end
      end
      tbody do
        for entity in @__entities
          tr do
            render_table_row(entity)
          end
        end
      end
    end
  end

  def render_table_row(entity)
    first = true
    @__headers.each do |head|
      if first
        first = false
        td do
          if head.instance_of? Array
            a entity.send(head[0]).attribute_get(head[1]), link_args(entity)
          else
            a entity.attribute_get(head), link_args(entity)
          end
        end
      else
        if head.instance_of? Array
          td entity.send(head[0]).attribute_get(head[1])
        else
          td entity.attribute_get(head)
        end
      end
    end

    td :class => :cell_buttons do
      delete_form(entity)
    end
  end

  def render_table_header
    @__field = @__headers[0] unless @__field 
    @__headers.each do |head|
      head = head[0] if head.instance_of? Array
      if head == @__field
        th do
          if @__direction == :down
            direction = :up
            arrow = " ∇"
          else
            direction = :down
            arrow = " ∆"
          end
          a(t("#{@__entities_symbol}.#{head}") + arrow, 
            :href => "?field=#{head}&direction=#{direction}")
        end
      else
        th do
          a(t("#{@__entities_symbol}.#{head}"), 
            :href => "?field=#{head}&direction=down")
        end
      end
    end
  end
end
