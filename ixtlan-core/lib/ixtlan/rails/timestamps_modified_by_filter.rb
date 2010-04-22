module Ixtlan
  module Rails
    module TimestampsModifiedBy
      module Base
        def self.included(base)
          base.prepend_around_filter(Filter)
        end
      end

      class Filter
        def self.filter(controller)
          name = controller.params[:controller]
          unless name.nil?
            parameters = controller.params[name.singular.to_sym]
            unless parameters.nil?
              parameters.delete(:created_at)
              parameters.delete(:created_on)
              parameters.delete(:created_by)
              # do not delete the updated_at so that optimistic persistence
              # can work !!!!
              #parameters.delete(:updated_at)
              parameters.delete(:updated_on)
              parameters.delete(:updated_by)
            end
          end
          yield if block_given?
        end
      end
    end
  end
end

::ActionController::Base.send(:include, Ixtlan::Rails::TimestampsModifiedBy::Base)
