module Ixtlan
  module Controllers
    module SearchQuery

      private 

      def simple_query(search_parameter)
        args = {}
        args[:limit] = params[:limit].to_i + 1 if params[:limit]
        args[:offset] = params[:offset].to_i if params[:offset]

        if login = params[search_parameter]
          if "false" == params[:fuzzy]
            args[search_parameter] = login
          else
            args[search_parameter.like] = "%" + login.to_s + "%"
          end
        end
        args
      end

    end
  end
end
