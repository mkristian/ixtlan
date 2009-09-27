# inspired by http://www.rowtheboat.com/archives/32
###################################################

# get all datamapper related gems
gem 'addressable', :lib => 'addressable/uri'

# assume sqlite3 to be database
gem 'do_sqlite3'

# serialization, validations and timestamps in your models
gem 'dm-validations'
gem 'dm-timestamps'
gem 'dm-serializer'
gem 'dm-migrations'

# assume you prefer rspec over unit tests
gem 'rspec', :lib => false
gem 'rspec-rails', :lib => false

# this pulls in rails_datamapper and rack_datamapper
gem 'datamapper4rails'

# logging
gem 'logging'

# ixtlan gems
gem 'ixtlan', :lib => 'guard'
gem 'ixtlan', :lib => 'unrestful_authentication'
gem 'ixtlan', :lib => 'session_timeout'
gem 'ixtlan', :lib => 'audit'

# install all gems
rake 'gems:install'

# install specs rake tasks
generate 'rspec', '-f'

# install datamapper rake tasks
generate 'datamapper_install'

# fix config files to work with datamapper instead of active_record
environment ''
environment 'config.frameworks -= [ :active_record ]'
environment '# deactive active_record'
gsub_file 'spec/spec_helper.rb', /^\s*config[.]/, '  #\0'
gsub_file 'test/test_helper.rb', /^[^#]*fixtures/, '  #\0'

# add middleware
def middleware(name)
  log "middleware", name
  environment "config.middleware.use '#{name}'"
end

environment ''
middleware 'DataMapper::RestfulTransactions'
middleware 'DataMapper::IdentityMaps'
middleware 'Rack::Deflater'
environment '# add middleware'

# init a session store
initializer 'datamapper_store.rb', <<-CODE
# init a session store which uses a memory cache and drops the user object 
# and the flash which results into a very thin session and hardly any 
# database updates !
# cleanup can be a problem. jruby uses soft-references for the cache so
# memory cleanup with jruby is no problem.
require 'ixtlan/session'
ActionController::Base.session_store = :datamapper_store
ActionController::Base.session = {
  :cache       => true,
  :session_class => Ixtlan::Session
}
CODE

# logger config
initializer '01_loggers.rb', <<-CODE
require 'ixtlan/logger_config'
CODE

# load the guard config
initializer '02_guard.rb', <<-CODE
# load the guard config files from RAILS_ROOT/app/guards
Ixtlan::Guard.load(Slf4r::LoggerFacade.new(Ixtlan::Guard))
CODE

# setup permissions controller
file "app/controllers/permissions_controller.rb", <<-CODE
class PermissionsController < ApplicationController

  def index
     render :xml => Ixtlan::Guard.export_xml
  end

end
CODE
route "map.resources :permissions"

file "db/migrate/1_create_root_user.rb", <<-CODE
migration 1, :create_root_user do
  up do
    Ixtlan::User.auto_migrate!
    Ixtlan::Locale.auto_migrate!
    Ixtlan::Group.auto_migrate!
    Ixtlan::GroupUser.auto_migrate!

    u = Ixtlan::User.create(:login => 'root', :email => 'root@exmple.com', :name => 'Superuser', :language => 'en')
    u.reset_password
    g = Ixtlan::Group.create(:name => 'root')
    u.groups << g
    u.save
    STDERR.puts "\#{u.login} \#{u.password}"
  end

  down do
  end
end
CODE

file "db/migrate/2_create_configuration.rb", <<-CODE
migration 1, :create_configuration do
  up do
    Ixtlan::Configuration.auto_migrate!
    Ixtlan::Configuration.create(:session_idle_timeout => 10, :keep_audit_logs => 3)
  end

  down do
  end
end
CODE

file "app/views/sessions/login.html.erb", <<-CODE
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

file 'app/guards/permissions_guard.rb', <<-CODE
Ixtlan::Guard.initialize(:permissions, {:index => []})
CODE

gsub_file 'app/controllers/application_controller.rb', /^\s*helper.*/, <<-CODE
  filter_parameter_logging :password, :login
  before_filter :check_session_expiry

  def new_session_timeout
    Ixtlan::Configuration.instance.session_idle_timeout.minutes.from_now
  end
CODE

rake 'dm:automigrate'
rake 'dm:migrate:down VERSION=0'
rake 'dm:migrate:up VERSION=2'
