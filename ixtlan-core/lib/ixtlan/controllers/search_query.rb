module Ixtlan
  module Controllers
    module SearchQuery

      private 
      def query(model, *parameters)
        _query(model, false, *parameters)
      end

      def query_limit_all(model, *parameters)
        _query(model, true, *parameters)
      end

      def _query(model, limit_all, *parameters)
        result = nil
        value = parameters[0].is_a?(String) ? parameters.shift : params[:query]
        if value
          parameters.each do |p|
            args = {}
            if "false" == params[:fuzzy]
              args[p] = value
            else
              args[p.like] = "%" + value.to_s + "%"
            end
            if result
              result = result + model.all(args)
            else
              result = model.all(args)
            end
          end
        end
        if limit_all || value
          limit = (params[:limit] || 10).to_i + 1
          offset = (params[:offset] || 0).to_i
          
          (result || model.all)[offset, offset + limit]
        else
          model.all
        end
      end
    end
  end
end
