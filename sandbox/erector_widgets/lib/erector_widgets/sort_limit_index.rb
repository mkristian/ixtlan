module ErectorWidgets
  module SortLimitIndex
    module Base
      def self.included(base)
        base.append_before_filter(SortLimitIndexFilter, :only => :index)
      end
    end
    
    class SortLimitIndexFilter
      
#      include Slf4r::Logger
      
      def self.filter(controller)
        field = controller.params[:field].to_sym if controller.params[:field]
        direction = controller.params[:direction] == "up" ? :up : :down
        args = if field
                 {:order => [direction == :down ? field.asc : field.desc]}
               else
                 {}
               end
        controller.instance_variable_set(:@find_all_args, args)
        if field
          controller.instance_variable_set(:@field, field)
          controller.instance_variable_set(:@direction, direction)
        end
      end
    end
  end
end


::ActionController::Base.send(:include, ErectorWidgets::SortLimitIndex::Base)
