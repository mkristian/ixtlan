require "activerecord"
class ScaffoldWidgetsGenerator < Rails::Generator::NamedBase
  default_options :skip_timestamps => false, :skip_migration => true

  attr_reader   :controller_name,
                :controller_class_path,
                :controller_file_path,
                :controller_class_nesting,
                :controller_class_nesting_depth,
                :controller_class_name,
                :controller_singular_name,
                :controller_plural_name
  alias_method  :controller_file_name,  :controller_singular_name
  alias_method  :controller_table_name, :controller_plural_name

  def initialize(runtime_args, runtime_options = {})
    super

    @controller_name = @name.pluralize

    base_name, @controller_class_path, @controller_file_path, @controller_class_nesting, @controller_class_nesting_depth = extract_modules(@controller_name)
    @controller_class_name_without_nesting, @controller_singular_name, @controller_plural_name = inflect_names(base_name)

    if @controller_class_nesting.empty?
      @controller_class_name = @controller_class_name_without_nesting
    else
      @controller_class_name = "#{@controller_class_nesting}::#{@controller_class_name_without_nesting}"
    end
  end

  def manifest
    record do |m|
      # Check for class naming collisions.
      m.class_collisions("#{controller_class_name}Controller", "#{controller_class_name}Helper")
      m.class_collisions(class_name)
      
      if options[:add_guard] 
        m.directory(File.join('app/guards', controller_class_path))
        m.template 'guard.rb', File.join('app/guards', class_path, "#{table_name}_guard.rb")
      end
      
      if options[:i18n] 
        m.directory(File.join('config/locales', controller_class_path))
        m.template 'i18n.rb', File.join('config/locales', class_path, "#{table_name}.yml")
      end
      
      m.dependency 'rspec_dm_model', [name] + @args, :collision => options[:collision]

      m.directory File.join('spec/controllers', class_path)

      m.dependency 'rspec_dm_controller', [name], :collision => options[:collision]

      m.dependency 'widgets', [name] + @args, :collision => options[:collision], :skip_page => options[:skip_page], :sortable => options[:sortable]
    end
  end

  protected

  def banner
    "Usage: #{$0} scaffold_widgets ModelName [field:type, field:type]"
  end
  
  def add_options!(opt)
    opt.separator ''
    opt.separator 'Options:'
    opt.on("--skip-timestamps",
           "Don't add timestamps to the migration file for this model") { |v| options[:skip_timestamps] = v }
    opt.on("--add-guard",
           "Add a guard for the actions on this model") { |v| options[:add_guard] = v }
    opt.on("--add-constraints",
           "Add constraints for this model") { |v| options[:add_constraints] = v }
    opt.on("--skip-page",
           "Skip layout page") { |v| options[:skip_page] = v }
    opt.on("--ixtlan",
           "add ixtlan specific code") { |v| options[:ixtlan] = v }
    opt.on("--i18n",
           "use i18n keys instead of text") { |v| options[:i18n] = v }
    opt.on("--sortable",
           "generate sortable list views") { |v| options[:sortable] = v }
  end
  
  def model_name
    class_name.demodulize
  end
end
module Rails
  module Generator
    module Commands
      class Create < Base
        
        def route_resources(*resources)
          resource_list = resources.map { |r| r.to_sym.inspect }.join(', ')
          sentinel = 'ActionController::Routing::Routes.draw do |map|'
          
          logger.route "map.resources #{resource_list}"
          unless options[:pretend]
            gsub_file 'config/routes.rb', /(#{Regexp.escape(sentinel)})/mi do |match|
              "#{match}\n  map.resources #{resource_list}, :member => {:edit => [:post,:delete]}\n  map.connect '#{resource_list}', :controller => '#{resource_list}', :conditions => { :method => [:delete] }\n  map.connect '#{resource_list}/:id', :controller => '#{resource_list}', :conditions => { :method => [:post] }\n"  
            end
          end
        end
        
      end
    end
  end
end
