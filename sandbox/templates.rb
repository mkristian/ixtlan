# get all datamapper related gems (assume sqlite3 to be database)
gem "addressable", :lib => "addressable/uri"
gem "do_sqlite3"
gem 'dm-validations'
gem 'dm-timestamps'
gem 'dm-constraints'
gem 'dm-serializer'
gem 'dm-ldap-adapter', :lib => 'ldap/version'
gem "rspec", :lib => false
gem "rspec-rails", :lib => false
gem 'datamapper4rail', :lib => 'datamapper4rails/adapters/restful_adapter'
gem "datamapper4rail", :lib => 'datamapper4rails' # excuse the missing 's'

rake "gems:install"

# have specs
generate("rspec")

# install datamapper rake tasks
generate("dm_install")

# fix config files to work with datamapper instead of active_record
run "sed -i config/environment.rb -e 's/#.*config.frameworks.*/config.frameworks -= [ :active_record ]/'"
run "sed -i spec/spec_helper.rb -e 's/^\\s*config[.]/#\\0/'"
run "sed -i test/test_helper.rb -e 's/^[^#]*fixtures/#\\0/'"

# fix a problem with missing class constants for models woth relations
initializer 'preload_models.rb', <<-CODE
require 'datamapper4rails/preload_models'
CODE

# generators
gem 'erector_widgets'
gem 'erector'
gem 'guard'

rake "gems:install"

initializer 'datamapper_store.rb', <<-CODE
require 'datamapper4rails/datamapper_store'
ActionController::Base.session = { :key => "_client_session" , :cache => true }
ActionController::Base.session_store = :datamapper_store
CODE

rake "db:automigrate"

# set up git
#git :init
git :add => '.'
git :commit => "-a -m 'Initial commit'"
