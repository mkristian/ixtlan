module Ixtlan
  module ActionController #:nodoc:
    module Guard #:nodoc:
      def self.included(base)
        base.send(:include, InstanceMethods)
      end
      module InstanceMethods #:nodoc:

        protected

        def guard(locale = nil)
          guard!(params[:controller], params[:action], locale)
        end

        def guard!(resource, action, locale = nil)
          unless ::Ixtlan::Guard.check(self, resource, action, locale)
            raise ::Ixtlan::PermissionDenied.new("permission denied for '#{resource}##{action}'")
          end
          true
        end

        def allowed(action, locale = nil)
          ::Ixtlan::Guard.check(self, params[:controller], action, locale)
        end
      end
    end
  end

  module Allowed #:nodoc:
    # Inclusion hook to make #allowed available as method
    def self.included(base)
      base.send(:include, InstanceMethods)
    end

    module InstanceMethods #:nodoc:
      def allowed(resource, action, locale = nil)
        ::Ixtlan::Guard.check(helpers.controller, resource, action, locale)
      end
    end
  end

  class Guard

    @@map = {}

    def self.load(logger = Logger.new(STDOUT), superuser = :root, guard_dir = "#{RAILS_ROOT}/app/guards", &block)
      @@block =
        if block
          block
        else
          Proc.new do |controller|
          user = controller.send :current_user
          user.groups if user
        end
      end
      DataMapper.setup(:guard_memory, :adapter => :in_memory)
      @@logger = logger
      @@superuser = superuser
      if File.exists?(guard_dir)
        Dir.new(guard_dir).to_a.each do |f|
          if f.match(".rb$")
            require(File.join(guard_dir, f))
          end
        end
        logger.debug("initialized guard . . .")
      else
        logger.warn("guard directory #{guard_dir} not found, skip loading")
      end
    end

    def self.symbolize(h)
      result = {}

      h.each do |k, v|
        if v.is_a?(Hash)
          result[k.to_sym] = symbolize_keys(v) unless v.size == 0
        elsif v.is_a?(Array)
          val = []
          v.each {|vv| val << vv.to_sym }
          result[k.to_sym] = val
        end
      end

      result
    end

    def self.initialize(controller, map)
      msg = map.collect{ |k,v| "\n\t#{k} => [#{v.join(',')}]"}
      @@logger.debug("#{controller} guard: #{msg}")
      @@map[controller.to_sym] = symbolize(map)
    end

    ROLE = Models::Role
    PERMISSION = Models::Permission

    def self.export_xml
      xml = permissions.to_xml
      repository(:guard_memory) do
        PERMISSION.all.destroy!
        ROLE.all.destroy!
      end
      xml
    end

    def self.permissions(user = nil)
      repository(:guard_memory) do
        PERMISSION.all.destroy!
        ROLE.all.destroy!
        root = ROLE.create(:name => @@superuser)
        @@map.each do |controller, actions|
          actions.each do |action, roles|
            permission = PERMISSION.create(:resource => controller, :action => action)
            permission.roles << root
            roles.each do |role|
              r = ROLE.create(:name => role)
              permission.roles << r unless permission.roles.member? r
            end
            permission.save
          end
        end
        PERMISSION.all
      end
    end

    def self.check(controller, resource, action, locale = nil)
      groups =  @@block.call(controller)
      return true if groups.nil?
      resource = resource.to_sym
      if (@@map.key? resource)
        action = action.to_sym
        allowed = @@map[resource][action]
        if (allowed.nil?)
          @@logger.warn("unknown action '#{action}' for controller '#{resource}'")
          raise ::Ixtlan::GuardException.new("unknown action '#{action}' for controller '#{resource}'")
        else
          allowed << @@superuser unless allowed.member? @@superuser
          all_groups = allowed.member?(:*) 
          if(all_groups && locale.nil?)
            return true
          else
            for group in groups
              if all_groups || allowed.member?(group.name.to_sym)
                if(locale.nil? || group.locales.member?(locale))
                  return true
                end
              end
            end
          end
          return false
        end
      else
        @@logger.warn("unknown controller '#{resource}'")
        raise ::Ixtlan::GuardException.new("unknown controller '#{resource}'")
      end
    end
  end

  class GuardException < Exception; end
  class PermissionDenied < GuardException; end
end
