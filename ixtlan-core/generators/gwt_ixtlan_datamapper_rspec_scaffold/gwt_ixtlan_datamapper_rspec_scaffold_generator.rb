require 'datamapper4rails/overlay'
require 'datamapper4rails/rspec_default_values'

class String
  def javanize
    s = camelize
    s[0,1].downcase! + s[1,1000]
  end
end

class GwtIxtlanDatamapperRspecScaffoldGenerator < IxtlanDatamapperRspecScaffoldGenerator

  def manifest
    overlay_dirs.add_generator("ixtlan_datamapper_rspec_scaffold")
    m = super
    
    basedir = 'src/main/java/com/example/client/'
    
    m.directory(File.join(basedir, 'models', controller_class_path))
    m.template 'Model.java', File.join(basedir, 'models', controller_class_path, "#{class_name}.java")
    m.template 'ModelFactory.java', File.join(basedir, 'models', controller_class_path, "#{class_name}Factory.java")

    m.directory(File.join(basedir, 'views', controller_class_path, controller_file_name))

    m.template 'Panel.java', File.join(basedir, 'views', controller_class_path, controller_file_name, "#{class_name}Panel.java")
    m.template 'Screen.java', File.join(basedir, 'views', controller_class_path, controller_file_name, "#{class_name}Screen.java")

    m
  end

end
