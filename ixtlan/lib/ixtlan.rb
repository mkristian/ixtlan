require 'dm-core'
require 'dm-serializer'
require 'dm-validations'
require 'dm-timestamps'
require 'dummy_ldap_resource'

Dir[Pathname(__FILE__).dirname + "*/**/*.rb"].each do |model|
  require  model
end
