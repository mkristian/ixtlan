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

    m.template 'Fields.java', File.join(basedir, 'views', controller_class_path, controller_file_name, "#{class_name}Fields.java")
    m.template 'Screen.java', File.join(basedir, 'views', controller_class_path, controller_file_name, "#{class_name}Screen.java")

    m.directory(File.join(basedir_test, 'models', controller_class_path))
    m.template 'AbstractApplicationResourceTestGwt.java', File.join(basedir_test, 'models', controller_class_path, "AbstractApplicationResourceTestGwt.java")
    m.template 'TestGwt.java', File.join(basedir_test, 'models', controller_class_path, "#{class_name}TestGwt.java")
    suiteFile = File.join(basedir_test, controller_class_path, "GwtTestSuite.java")
    unless File.exists?(suiteFile)
      m.template 'GwtTestSuite.java', suiteFile
    end
    m.add_testcase @package, suiteFile, class_name
    m.add_screen File.join(basedir, "Application.java"), @package, class_name, singular_name, plural_name

    m
  end

end

module Rails
  module Generator
    module Commands
      class Create < Base

        def route_resources(*resources)
          unless options[:pretend]
            resource_list = resources.map { |r| r.to_sym.inspect }.join(', ')
            skip_route = false
            gsub_file 'config/routes.rb', /(map.resources #{resource_list})/mi do |match|
              skip_route = true
              "#{match}"
            end
            sentinel = 'ActionController::Routing::Routes.draw do |map|'

            logger.route "map.resources #{resource_list}" +  (skip_route ? " - skipped" : " - added")
            unless skip_route
              gsub_file 'config/routes.rb', /(#{Regexp.escape(sentinel)})/mi do |match|
                "#{match}\n  map.resources #{resource_list}\n"
              end
            end
          end
        end

        def add_testcase(package, suite_file, class_name)
          unless options[:pretend]
            skip_testcase = false
            gsub_file suite_file, /(import #{package}.models.#{class_name}TestGwt;)/mi do |match|
              skip_testcase = true
              "#{match}"
            end
            logger.testcase(class_name + "TestGwt" + (skip_testcase ? " - skipped" : " - added"))
            unless skip_testcase
              gsub_file suite_file, /(^package\s+[a-z.]*;)/mi do |match|
                "#{match}\n\nimport #{package}.models.#{class_name}TestGwt;"
              end
              gsub_file suite_file, /(^\s+return suite;)/mi do |match|
                "        suite.addTestSuite(#{class_name}TestGwt.class);\n#{match}"
              end
            end
          end
        end

        def add_screen(application_file, java_package, class_name, singular_name, plural_name)
          unless options[:pretend]
            variable = "#{class_name[0,1].downcase}#{class_name[1,100]}"
            skip_screen = false
            gsub_file application_file, /(import #{java_package}.models.#{class_name}Factory;)/mi do |match|
              skip_screen = true
              "#{match}"
            end
            logger.screen(class_name + "Screen" + (skip_screen ? " - skipped" : " - added"))
            unless skip_screen
              skip_binding = false
              gsub_file application_file, /(import de.saumya.gwt.translation.common.client.widget.ResourceBindings;)/mi do |match|
                skip_binding = true
                "#{match}"
              end
              unless skip_binding
                gsub_file application_file, /(^package\s+[a-z.]*;)/mi do |match|
                  "#{match}\n\nimport de.saumya.gwt.translation.common.client.widget.ResourceBindings;"
                end
              end
              gsub_file application_file, /(^package\s+[a-z.]*;)/mi do |match|
                "#{match}\n\nimport #{java_package}.models.#{class_name}Factory;\nimport #{java_package}.models.#{class_name};\nimport #{java_package}.views.#{plural_name}.#{class_name}Screen;"
              end
              gsub_file application_file, /(screenController = [a-zA-Z.]+;)/mi do |match|
                "#{match}\n\n        #{class_name}Factory #{variable}Factory = new #{class_name}Factory(container.repository,\n                container.notifications,\n                container.userFactory);\n        #{class_name}Screen #{variable}Screen = new #{class_name}Screen(container.loadingNotice,\n                 container.getTextController,\n                 #{variable}Factory,\n                 container.session,\n                 new ResourceBindings<#{class_name}>(),\n                 container.listeners,\n                 container.hyperlinkFactory);\n        screenController.addScreen(#{variable}Screen, \"#{plural_name}\");"
              end
            end
          end
        end
      end
    end
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
          when :string, :text               then first ? "first valule of #{@name}" : "second value of #{name}"
          when :boolean                     then first ? "false" : "true"
          else
            ""
        end
      end
    end
  end
end
