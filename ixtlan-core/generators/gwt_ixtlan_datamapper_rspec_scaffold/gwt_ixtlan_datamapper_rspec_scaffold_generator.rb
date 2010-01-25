require 'datamapper4rails/overlay'
require 'datamapper4rails/rspec_default_values'

class String
  def javanize
    s = camelize
    s[0,1].downcase! + s[1,1000]
  end
end

class GwtIxtlanDatamapperRspecScaffoldGenerator < IxtlanDatamapperRspecScaffoldGenerator

  attr_reader :package

  def find_client_dir(dir)
    dir.entries.each do |entry|
      path = File.join(dir.path, entry)
      if File.directory? path 
        if entry == "client"
          return path
        elsif entry != ".." && entry != "."
          return find_client_dir(Dir.new(path))
        end
      end
    end
    nil
  end

  def manifest
    overlay_dirs.add_generator("ixtlan_datamapper_rspec_scaffold")

    m = super

    base = "src/main/java/"
      
    basedir = find_client_dir(Dir.new(base))
    basedir_test = basedir.sub(/main/, 'test')

    @package = basedir.sub(/#{base}/, "").gsub(/\//, ".")

    m.directory(File.join(basedir, 'models', controller_class_path))
    m.template 'Model.java', File.join(basedir, 'models', controller_class_path, "#{class_name}.java")
    m.template 'ModelFactory.java', File.join(basedir, 'models', controller_class_path, "#{class_name}Factory.java")

    m.directory(File.join(basedir, 'views', controller_class_path, controller_file_name))

    m.template 'Panel.java', File.join(basedir, 'views', controller_class_path, controller_file_name, "#{class_name}Panel.java")
    m.template 'Screen.java', File.join(basedir, 'views', controller_class_path, controller_file_name, "#{class_name}Screen.java")

    m.directory(File.join(basedir_test, 'models', controller_class_path))
    m.template 'AbstractApplicationResourceTestGwt.java', File.join(basedir_test, 'models', controller_class_path, "AbstractApplicationResourceTestGwt.java")
    m.template 'TestGwt.java', File.join(basedir_test, 'models', controller_class_path, "#{class_name}TestGwt.java")
    
    m
  end

end

module Rails
  module Generator
    class GeneratedAttribute
      def sample_value(first = true)
        value = (first ? @first_value : @second_value)
        value ||= case type
          when :int, :integer               then first ? "1" :  "2"
          when :float                       then first ? "1.5" : "2.5"
          when :decimal, :big_decimal       then first ? "9.99" : "3.33"
          when :date_time, :datetime,
                          :timestamp, :time then first ? "2009-09-09 09:09:09.0" : "2010-10-10 10:10:10.0"
          when :date                        then first ? "2009-09-09" : "2010-10-10"
          when :string, :text               then first ? "first of #{@name}" : "second of #{name}"
          when :boolean                     then first ? "false" : "true"
          else
            ""
        end
      end
    end
  end
end
