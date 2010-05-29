require 'datamapper4rails/overlay'
require 'datamapper4rails/rspec_default_values'

class IxtlanDatamapperRspecScaffoldGenerator < DatamapperRspecScaffoldGenerator

  def manifest
    overlay_dirs.add_generator("ixtlan_datamapper_model")
    overlay_dirs.add_generator("ixtlan_datamapper_rspec_model")
    overlay_dirs.add_generator("datamapper_rspec_scaffold")

    m = super

    unless options[:skip_guard]
      m.directory(File.join('app/guards', controller_class_path))
      m.template 'guard.rb', File.join('app/guards', controller_class_path, "#{table_name}_guard.rb")
    end

    if options[:i18n]
      m.directory(File.join('config/locales', controller_class_path))
      m.template 'i18n.rb', File.join('config/locales', controller_class_path, "#{table_name}.yml")
    end

    m
  end

  def add_options!(opt)
    super
    opt.on("--skip-timestamps",
           "Don't add timestamps for this model") { |v| options[:skip_timestamps] = v }
    opt.on("--skip-modified-by",
           "Don't add modified_by references for this model") { |v| options[:skip_modified_by] = v }
    opt.on("--add-current-user",
           "set current user before invoking a method on the model") { |v| options[:current_user] = v }
    opt.on("--skip-guard",
           "Don't add guards for the actions on this model") { |v| options[:add_guard] = v }
    opt.on("--i18n",
           "Use i18n keys instead of text") { |v| options[:i18n] = v }
  end
end
