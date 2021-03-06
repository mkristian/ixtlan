# inspired by http://www.rowtheboat.com/archives/32
###################################################

# --------------
# HELPER METHODS
# --------------

def middleware(name, *args)
  log 'middleware', name
  environment "config.middleware.use '#{name}' '#{args.join('\',\'')}'"
end

def ixtlan_model(name)
  file "app/models/#{name}.rb", <<-CODE
class #{name.camelcase}
  include Ixtlan::Models::#{name.camelcase}
end
CODE
end

def ixtlan_controller(name, guards = {:actions => [:index,:show,:new,:create,:edit,:update,:destroy]}, singleton = nil)
  if guards
    file "app/guards/#{name}_guard.rb", <<-CODE
Ixtlan::Guard.initialize(:#{name}, {
#{guards[:actions].collect {|g| ":#{g} => []" }.join(",\n") }
})
CODE
  end
  file "app/controllers/#{name}_controller.rb", <<-CODE
class #{name.camelize}Controller < ApplicationController
  include Ixtlan::Controllers::#{name.camelize}Controller
end
CODE
  if singleton
    route "map.resource :#{singleton}"
  else
    route "map.resources :#{name}"
  end
end

def add_gem(name, version, scope = :compile, options = {})
  gem name, options
  unless scope.nil?
    scope = scope == :compile ? "" : "<scope>#{scope}</scope>\n"
    gsub_file 'pom.xml', /<\/dependencies>/, "<dependency>\n<groupId>rubygems</groupId>\n<artifactId>#{name}</artifactId>\n<version>#{version}</version>\n<type>gem</type>\n#{scope}</dependency>\n</dependencies>"
  end
end

def migration(model)
  @__index ||= 0
  @__index = @__index + 1
  file "db/migrate/#{@__index}_create_#{model}.rb", <<-CODE
require 'config/initializers/ixtlan.rb'
require 'ixtlan/rails/migrations'
migration #{@__index}, :create_#{model} do
  up do
    Ixtlan::Rails::Migrations.create_#{model}
  end

  down do
  end
end
CODE
end

# --------
# VERSIONS
# --------

JRUBY_PLUGINS_VERSION='0.20.0-SNAPSHOT'
DM_VERSION='1.0.0'
RESTFUL_GWT_VERSION='0.5.0-SNAPSHOT'

# -----------
# MAVEN SETUP
# -----------

# setup a pom.xml
File.delete('pom.xml') if File.exists?("pom.xml")
inside("..") do
  run("mvn archetype:generate -DarchetypeArtifactId=rails-maven-archetype -DarchetypeGroupId=de.saumya.mojo -DarchetypeVersion=#{JRUBY_PLUGINS_VERSION} -DartifactId=#{File.basename(root)} -DgroupId=com.example -Dversion=0.1.0-SNAPSHOT -B")
end
File.delete('lib/tasks/jdbc.rake')
File.delete('config/initializers/jdbc.rb')

# ---
# GIT
# ---
file '.gitignore', <<-CODE
target
root_*
tomcat
CODE
file 'log/.gitignore', <<-CODE
*log
CODE
file 'db/.gitignore', <<-CODE
*sqlite3
CODE

# ---------------------
# GWT AND ECLIPSE SETUP
# ---------------------

File.rename("src/main/webapp/WEB-INF/web.xml", 
            "src/main/webapp/WEB-INF/web.xml.rails")
inside("..") do
  run("mvn archetype:generate -DarchetypeArtifactId=gui -DarchetypeGroupId=de.saumya.gwt.translation -DarchetypeVersion=#{RESTFUL_GWT_VERSION} -DartifactId=#{File.basename(root)} -DgroupId=com.example -Dversion=0.1.0-SNAPSHOT -B")
end

File.delete('src/test/java/com/example/client/GwtTestSample.java')
File.rename("src/main/webapp/WEB-INF/web.xml.rails", 
            "src/main/webapp/WEB-INF/web.xml")
gsub_file 'pom.xml', /.*rspec -->.*/, ''
gsub_file 'pom.xml', /.*<!--.*rspec.*/, ''
gsub_file 'pom.xml', /<build>.*/, "<build>\n    <outputDirectory>war/WEB-INF/classes</outputDirectory>"

file 'war/WEB-INF/.gitignore', <<-CODE
classes
lib
CODE
file 'war/WEB-INF/web.xml', <<-CODE
<web-app>
  <!-- Proxy Servlets to the restful backend - i.e. rails ixtlan backend-->
  <servlet>
    <servlet-name>XMLProxyServlet</servlet-name>
    <servlet-class>de.saumya.gwt.persistence.server.ProxyServlet</servlet-class>
    <init-param>
      <param-name>base</param-name>
      <param-value>/com.example.Application</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>XMLProxyServlet</servlet-name>
    <url-pattern>*.xml</url-pattern>
  </servlet-mapping>
</web-app>
CODE
file '.classpath', <<-CODE
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" output="war/WEB-INF/classes" path="src/main/java"/>
	<classpathentry excluding="**" kind="src" output="war/WEB-INF/classes" path="src/main/resources"/>
	<classpathentry kind="src" output="target/test-classes" path="src/test/java"/>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.6"/>
	<classpathentry kind="con" path="org.maven.ide.eclipse.MAVEN2_CLASSPATH_CONTAINER"/>
	<classpathentry kind="con" path="com.google.gwt.eclipse.core.GWT_CONTAINER"/>
	<classpathentry kind="output" path="war/WEB-INF/classes"/>
</classpath>
CODE
file '.project', <<-CODE
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>#{File.basename(root)}</name>
	<comment></comment>
	<projects>
	</projects>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>org.maven.ide.eclipse.maven2Builder</name>
			<arguments>
			</arguments>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.maven.ide.eclipse.maven2Nature</nature>
		<nature>org.eclipse.jdt.core.javanature</nature>
		<nature>com.google.gwt.eclipse.core.gwtNature</nature>
	</natures>
</projectDescription>
CODE
file '.settings/org.maven.ide.eclipse.prefs', <<-CODE
activeProfiles=
eclipse.preferences.version=1
fullBuildGoals=process-test-resources
includeModules=false
resolveWorkspaceProjects=true
resourceFilterGoals=process-resources resources\:testResources
skipCompilerPlugin=true
version=1
CODE
file '.settings/org.eclipse.jdt.core.prefs', <<-CODE
eclipse.preferences.version=1
org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.6
org.eclipse.jdt.core.compiler.compliance=1.6
org.eclipse.jdt.core.compiler.problem.forbiddenReference=warning
org.eclipse.jdt.core.compiler.source=1.6
CODE
file '.settings/com.google.gwt.eclipse.core.prefs', <<-CODE
eclipse.preferences.version=1
entryPointModules=com.example.Application
filesCopiedToWebInfLib=gwt-servlet.jar
CODE
file "#{File.basename(root)}.launcher", <<-CODE
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<launchConfiguration type="com.google.gdt.eclipse.suite.webapp">
<stringAttribute key="com.google.gdt.eclipse.suiteMainTypeProcessor.PREVIOUSLY_SET_MAIN_TYPE_NAME" value="com.google.gwt.dev.HostedMode"/>
<booleanAttribute key="com.google.gdt.eclipse.suiteWarArgumentProcessor.IS_WAR_FROM_PROJECT_PROPERTIES" value="true"/>
<stringAttribute key="com.google.gwt.eclipse.core.URL" value="com.example.Application/Application.html"/>
<listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_PATHS">
<listEntry value="/#{File.basename(root)}"/>
</listAttribute>
<listAttribute key="org.eclipse.debug.core.MAPPED_RESOURCE_TYPES">
<listEntry value="4"/>
</listAttribute>
<stringAttribute key="org.eclipse.jdt.launching.CLASSPATH_PROVIDER" value="com.google.gwt.eclipse.core.moduleClasspathProvider"/>
<stringAttribute key="org.eclipse.jdt.launching.MAIN_TYPE" value="com.google.gwt.dev.HostedMode"/>
<stringAttribute key="org.eclipse.jdt.launching.PROGRAM_ARGUMENTS" value="-style OBFUSCATED -startupUrl com.example.Application/Application.html -logLevel INFO -port 8888 com.example.Application"/>
<stringAttribute key="org.eclipse.jdt.launching.PROJECT_ATTR" value="#{File.basename(root)}"/>
<stringAttribute key="org.eclipse.jdt.launching.VM_ARGUMENTS" value="-Xmx512m"/>
</launchConfiguration>
CODE

# --------------------
# ADD GEM DEPENDENCIES
# --------------------

# ixtlan gems
add_gem 'rack', '1.0.1'
#add_gem 'rack-datamapper', '0.3.2'
add_gem 'ixtlan', '0.4.0.pre4'
#add_gem 'slf4r', '0.3.2'

# assume sqlite3 to be database
add_gem 'dm-sqlite-adapter', DM_VERSION

# serialization, validations and timestamps in your models
add_gem 'dm-validations', DM_VERSION
add_gem 'dm-timestamps', DM_VERSION
add_gem 'dm-migrations', DM_VERSION
add_gem 'dm-aggregates', DM_VERSION
add_gem 'dm-transactions', DM_VERSION
add_gem 'dm-core', DM_VERSION
add_gem 'extlib', '0.9.15', nil #nil == needed to execute this template

# assume you prefer rspec over unit tests
add_gem 'rspec', '[1.3.0,1.4.0]', :test, :lib => false
add_gem 'rspec-rails', '1.3.2', :test, :lib => false

# install all gems
log 'gemhome', java.lang.System.getProperty('maven.home')
run("mvn gem:initialize -Djruby.gem.home=#{ENV['GEM_HOME']} -Djruby.gem.path=#{ENV['GEM_PATH']}")
#rake 'gems:install'

# install specs rake tasks
run("mvn rails2:generate -Djruby.gem.home=#{ENV['GEM_HOME']} -Djruby.gem.path=#{ENV['GEM_PATH']} -Dargs='rspec -f'")
#generate('rspec', '-f')

# install datamapper rake tasks
generate('datamapper_install')

# fix config files to work with datamapper instead of active_record
environment ''
environment 'config.frameworks -= [ :active_record, :active_resource ]'
environment '# deactive active_record'
gsub_file 'config/environment.rb', /.*config.gem\s+.rspec.*/, "\\0 if ENV['RAILS_ENV'] == 'test'"
gsub_file 'spec/spec_helper.rb', /^\s*config[.]/, '  #\0'
gsub_file 'test/test_helper.rb', /^[^#]*fixtures/, '  #\0'

file 'spec/support/datamapper.rb', <<-CODE
require 'datamapper4rails/rspec'
CODE

# ----------
# MIDDLEWARE
# ----------

environment ''
middleware 'DataMapper::RestfulTransactions'
middleware 'DataMapper::IdentityMaps'
middleware 'Ixtlan::AuditRack'
environment '# this is important to clean up the Thread.current when you set AUDIT'
middleware 'Rack::Deflater'
middleware 'Ixtlan::ChildPath', 'com.example.Application'
environment '# you need this to run the war file directly without copying the GWT application into the root directory'
environment '# add middleware'

# ------------
# INITIALIZERS
# ------------

# get all the ixtlan specific code initialized
initializer 'ixtlan.rb', <<-CODE
module Ixtlan
  module Models
    # overwrite configuration class
    # CONFIGURATION = "::MyConfiguration"
    # set this to nil to switch off Audit logs into the database
    # AUDIT = nil
  end
end
require 'ixtlan/models'
require 'ixtlan/modified_by'
if ENV['RAILS_ENV']
  require 'ixtlan/rails/error_handling'
  require 'ixtlan/rails/audit'
  require 'ixtlan/rails/session_timeout'
  require 'ixtlan/rails/unrestful_authentication'
  require 'ixtlan/rails/guard'
  require 'ixtlan/rails/timestamps_modified_by_filter'
  require 'ixtlan/optimistic_persistence'
end
require 'ixtlan/monkey_patches'

# auto require to load needed libraries . . .
require 'datamapper4rails'
require 'ixtlan/logger_config' if ENV['RAILS_ENV']

# cleanup can be a problem. jruby uses soft-references for the cache so
# memory cleanup with jruby is no problem.
require 'ixtlan/session'
ActionController::Base.session_store = :datamapper_store
ActionController::Base.session = {
  :cache       => true,
  :session_class => Ixtlan::Session
}

# load the guard config files from RAILS_ROOT/app/guards
Ixtlan::Guard.load(Slf4r::LoggerFacade.new(Ixtlan::Guard)) if ENV['RAILS_ENV']
CODE

# define the date/time pattern for the xml
initializer 'time_formats.rb', <<-CODE
Time::DATE_FORMATS[:xml] = lambda { |time| time.utc.strftime("%Y-%m-%d %H:%M:%S") }
CODE

# ----------
# MIGRATIONS
# ----------

migration("user")
migration("configuration")
migration("locale")
migration("domain")
migration("text")
migration("audit")

# -----
# VIEWS
# -----

#some small html pages
file "app/views/sessions/login.html.erb", <<-CODE
<p style="color: darkgreen"><%= @notice %></p>
<form method="post">
  <p>
    <label for="login">login</label><br />
    <input type="text" name="login" />
  </p>
  <p>
    <label for="password">password</label><br />
    <input type="password" name="password" />
  </p>
  <p>
    <input type="submit" value="login" />
  </p>
</form>
CODE

file 'app/views/errors/error.html.erb', <<-CODE
<h2>error</h2>
<h3><%= @notice %></h3>
CODE

file 'app/views/errors/error_with_session.html.erb', <<-CODE
<h2>error with session</h2>
<h3><%= @notice %></h3>
CODE

file 'app/views/errors/stale.html.erb', <<-CODE
<h2>stale resource</h2>
<p>please reload resource and change it again</p>
CODE


# ----------------------
# MODELS AND CONTROLLERS
# ----------------------

# setup permissions controller
ixtlan_controller("permissions", {:actions => [:index]})

# user model/controller
generate "ixtlan_datamapper_rspec_scaffold", '-f', '--skip-migration', 'User', 'login:string', 'name:string', 'email:string'
gsub_file 'spec/models/user_spec.rb', /.*:name => "sc'?r&?ipt".*/, ''
gsub_file 'spec/models/user_spec.rb', /value for login/, 'valueForLogin'
gsub_file 'spec/models/user_spec.rb', /value for email/, 'value@for.email'
gsub_file 'spec/controllers/users_controller_spec.rb', /\:errors\s+=>\s+{}/, ':reset_password => nil, :password => "pass", :email => "email", :login=> "login", :attributes= => nil, :update_all_children => nil, :errors => {}'
gsub_file 'spec/controllers/users_controller_spec.rb', /CONFIGURATION,\s*{}/, 'CONFIGURATION, {:password_sender_email => "email", :login_url => "url"}'
gsub_file 'spec/controllers/users_controller_spec.rb', /\:update\s+=>\s+false/, ':save => false'
gsub_file 'spec/controllers/users_controller_spec.rb', /\:update\s+=>\s+true/, ':save => true'
gsub_file 'spec/controllers/users_controller_spec.rb', /receive\(\:update\).with\(.*\)/, 'receive(:save)'
ixtlan_model 'user'
ixtlan_controller 'users'

# group model/controller
generate "ixtlan_datamapper_rspec_scaffold", '-f', '--skip-migration', 'Group', 'name:string'
gsub_file 'spec/controllers/groups_controller_spec.rb', /\:errors\s+=>\s+{}/, ':update_children => nil, :errors => {}'
ixtlan_model 'group'
ixtlan_controller 'groups'

# domain model/controller
generate "ixtlan_datamapper_rspec_scaffold", '-f', '--skip-migration', 'Domain', 'name:string'
gsub_file 'spec/models/domain_spec.rb', /value for name/, 'valueofname'
ixtlan_model 'domain'
ixtlan_controller 'domains'

# i18n stuff: i18n model, phrases, word_bundles controller
ixtlan_model 'i18n_text'
ixtlan_controller "phrases"
ixtlan_controller "word_bundles"

# locale model/controller
generate "ixtlan_datamapper_rspec_scaffold", '-f', '--skip-migration', '--skip-modified-by', '--add-current-user', 'Locale', 'code:string'
gsub_file 'spec/models/locale_spec.rb', /value for code/, 'vc'
ixtlan_model "locale"
ixtlan_controller "locales"

# configuration guard/model/controller
ixtlan_model "configuration"
ixtlan_controller 'configurations', {:actions => [:show, :edit, :update]}, 'configuration'

# authentication model/controller
ixtlan_model "authentication"
ixtlan_controller "authentications", {:actions => [:create]}, 'authentication'

# audit model/controller
ixtlan_model "audit"
ixtlan_controller "audits", {:actions => [:index]}

# modify application controller as needed
gsub_file 'app/controllers/application_controller.rb', /^\s*helper.*/, <<-CODE
  filter_parameter_logging :password, :login
  before_filter :check_session_expiry

  # override default to use nice User (without namespace)
  #def login_from_params
  #  User.authenticate(params[:login], params[:password])
  #end

  #def login_from_session
  #  User.get(session[:user_id])
  #end

  # overwrite default
  #def session_timeout
  #  3 # time out in 3 minutes
  #end

  def render_error_page_with_session(status)
    render :template => "errors/error_with_session", :status => status
  end

  def render_error_page(status)
    render :template => "errors/error", :status => status
  end

  include Ixtlan::Rails::RescueModule
  # you can overwrite a rescue directive here
  # rescue_from ::Ixtlan::StaleResourceError, :with => :stale_resource
  # rescue_from ::ActionView::MissingTemplate, :with => :internal_server_error

  protect_from_forgery # See ActionController::RequestForgeryProtection for details
CODE

# --------------------------------
# GLOBAL CONFIG VIA PREINITIALIZER
# --------------------------------

# configuration before starting rails
file 'config/preinitializer.rb', <<-CODE
require 'yaml'
require 'erb'
module Ixtlan
  class Configurator

    def self.symbolize_keys(h)
      result = {}

      h.each do |k, v|
        v = ' ' if v.nil?
        if v.is_a?(Hash)
          result[k.to_sym] = symbolize_keys(v) unless v.size == 0
        else
          result[k.to_sym] = v unless k.to_sym == v.to_sym
        end
      end

      result
    end

    def self.load(file)
      symbolize_keys(YAML::load(ERB.new(IO.read(file)).result)) if File.exists?(file)
    end
  end
end

CONFIG = Ixtlan::Configurator.load(File.join(File.dirname(__FILE__), 'global.yml')) || {}
CODE

file 'config/.gitignore', <<-CODE
global.yml
CODE

file 'config/global-exmaple.yml', <<-CODE
# possible example to have a file with all the (production) passwords
# outside GIT/SUBVERSION/CVS/etc !!!
mysql:
  database: example
  host: mysql.example.com
  username: rails
  password: run_it

smtp:
  address: smtp.gmail.com
  port: 587
  domain: example.com
  authentication: plain
  user_name: mail@example.com
  password: mail_it
CODE

append_file 'config/environments/production.rb', <<-CODE

#config.action_mailer.delivery_method = :smtp

#require "smtp_tls"

#ActionMailer::Base.smtp_settings = {
#:address => CONFIG[:smtp][:address],
#:port => CONFIG[:smtp][:port],
#:domain => CONFIG[:smtp][:domain],
#:authentication => CONFIG[:smtp][:authentication],
#:user_name => CONFIG[:smtp][:user_name],
#:password => CONFIG[:smtp][:password]
#}
CODE


# -----------------------
# SETUP AND SEED DATABASE
# -----------------------

# for some reason the rake needs a fork instead of calling it dircetly
run('rake db:migrate:down VERSION=0')
run('rake db:sessions:create')
run('rake db:migrate --trace')
run('rake db:autoupgrade RAILS_ENV=development')
logger.info
logger.info "start rails in one console"
logger.info "\tmvn rails2:server"
logger.info
logger.info "start the GWT gui in another console"
logger.info "\tmvn gwt:run"
logger.info
logger.info "you find the root password in the file 'root_development'"
logger.info
logger.info
