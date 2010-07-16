module Ixtlan
  module Controllers
    module SearchQuery

      private 

      def simple_query(search_parameter)
        query(search_parameter, params[search_parameter])
      end

      def query(parameter, value)
        args = {}
        args[:limit] = (params[:limit] || 10).to_i + 1 #if params[:limit]
        args[:offset] = params[:offset].to_i if params[:offset]

        if value
          if "false" == params[:fuzzy]
            args[parameter] = value
          else
            args[parameter.like] = "%" + value.to_s + "%"
          end
        end
        args
      end
    end
  end
end
