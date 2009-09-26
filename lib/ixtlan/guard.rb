module Ixtlan
  module ActionController #:nodoc:
    module Guard #:nodoc:
      def self.included(base)
        base.send(:include, InstanceMethods)
      end
      module InstanceMethods #:nodoc:
        
        protected
        
        def guard(locale = nil)
p session
          guard!(params[:controller], params[:action], locale)
        end

        def guard!(resource, action, locale = nil)
          unless ::Ixtlan::Guard.check(self, resource, action, locale)
            raise ::Ixtlan::PermissionDenied.new("permission denied for '#{resource}##{action}'")
          end
          true
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
          user = controller.current_user
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
        logger.info("initialized guard . . .")
      else
        logger.warn("guard directory #{guard_dir} not found, skip loading")
      end
    end
    
    def self.initialize(controller, map)
      msg = map.collect{ |k,v| "\n\t#{k} => [#{v.join(',')}]"}
      @@logger.debug("#{controller} guard: #{msg}")
      @@map[controller] = map
    end

    def self.export_xml
      repository(:guard_memory) do
        @@map.each do |controller, actions|
          actions.each do |action, roles|
            permission = Permission.create(:resource => controller, :action => action)
            roles.each do |role|
              permission.roles << Role.create(:name => role)
            end
            permission.save
          end
        end
        xml = Permission.all.to_xml(:collection_element_name => 'permissions')
        Permission.all.destroy!
        Role.all.destroy!
        xml
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
          @@logger.warn("Guard: unknown action '#{action}' for controller '#{resource}'")
          raise ::Ixtlan::GuardException.new("GuardException: unknown action '#{action}' for controller '#{resource}'")
        else
          allowed << @@superuser unless allowed.member? @@superuser
          for group in groups
             if allowed.member? group.name.to_sym
               return locale.nil? ? true : (group.locales.member? locale)
             end
          end
          return false
        end
      else
        @@logger.warn("Guard: unknown controller '#{resource}'")
        raise ::Ixtlan::GuardException.new("GuardException: unknown controller '#{resource}'")
      end
    end
  end

  class GuardException < Exception; end
  class PermissionDenied < GuardException; end
end

