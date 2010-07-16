module Ixtlan
  module Rails
    module CacheHeaders

      protected

      # Date: <ServercurrentDate>
      # Expires: Fri, 01 Jan 1990 00:00:00 GMT
      # Pragma: no-cache
      # Cache-control: no-cache, must-revalidate
      def no_caching(no_store = true)
        if cachable_response?
          response.headers["Date"] = timestamp
          response.headers["Expires"] = "Fri, 01 Jan 1990 00:00:00 GMT"
          response.headers["Pragma"] = "no-cache"
          response.headers["Cache-Control"] = "no-cache, must-revalidate" + (", no-store" if no_store).to_s
        end
      end

      # Date: <ServercurrentDate>
      # Expires: Fri, 01 Jan 1990 00:00:00 GMT
      # Cache-control: private, max-age=<1dayInSeconds>
      def only_browser_can_cache(no_store = false, max_age_in_seconds = 0)
        if cachable_response?
          response.headers["Date"] = timestamp
          response.headers["Expires"] = "Fri, 01 Jan 1990 00:00:00 UTC"
          response.headers["Cache-Control"] = "private, max-age=#{max_age_in_seconds}" + (", no-store" if no_store).to_s
        end
      end

      # Date: <ServercurrentDate>
      # Expires: <ServerCurrentDate + 1month>
      # Cache-control: public, max-age=<1month>
      def allow_browser_and_proxy_to_cache(no_store = false, max_age_in_seconds = 0)
        if cachable_response?
          now = Time.now
          response.headers["Date"] = timestamp(now)
          response.headers["Expires"] = timestamp(now + max_age_in_seconds)
          response.headers["Cache-Control"] = "public, max-age=#{max_age_in_seconds}" + (", no-store" if no_store).to_s
        end
      end

      def cache_headers
        if(current_user)
          case self.class.instance_variable_get(:@mode)
          when :private
            no_caching(self.class.instance_variable_get(:@no_store))
          when :protected
            only_browser_can_cache(self.class.instance_variable_get(:@no_store))
          when :public
            allow_browser_and_proxy_to_cache(self.class.instance_variable_get(:@no_store))
          end
        else
          allow_browser_and_proxy_to_cache(self.class.instance_variable_get(:@no_store))
        end
      end

      def self.included(base)
        base.class_eval <<-EOS, __FILE__, __LINE__
          def self.cache_headers(mode = nil, no_store = true)
            if(mode)
              raise "supported modi are :private, :protected and :public" unless [:private, :protected, :public].member? mode
              @mode = mode
            end
            @no_store = no_store
          end
          alias :render_old :render
          def render(*args)
            cache_headers
            render_old(*args)
          end
EOS
      end

      private
      def cachable_response?
        request.method == :get &&
          [200, 203, 206, 300, 301].member?(response.status)
      end

      def timestamp(now = Time.now)
        now.utc.strftime "%a, %d %b %Y %H:%M:%S %Z"
      end
    end
  end
end

ActionController::Base.send(:include, Ixtlan::Rails::CacheHeaders)
