# create by maven - leave it as is
Gem::Specification.new do |s|
  s.name = 'ixtlan'
  s.version = '0.4.0'

  s.summary = 'ixtlan plugins for rails and datamapper'
  s.description = 'this is set of rails and datamapper plugins for setting up a little more advanced rails application then the default rails generator does. the focus is on security and privacy as well a complete restful xml support.'
  s.homepage = 'http://github.com/mkristian/ixtlan/ixtlan-core'

  s.authors = ['mkristian']
  s.email = ['m.kristian@web.de']

  s.files = Dir['MIT-LICENSE']
  s.licenses << 'MIT-LICENSE'
  s.files += Dir['History.txt']
  s.files += Dir['README.txt']
  s.extra_rdoc_files = ['History.txt','README.txt']
  s.files += Dir['Rakefile']
  s.rdoc_options = ['--main','README.txt']
  s.files += Dir['lib/**/*']
  s.files += Dir['generators/**/*']
  s.files += Dir['spec/**/*']
  s.test_files += Dir['spec/**/*_spec.rb']
  s.add_dependency 'dm-core', '~> 1.0.0'
  s.add_dependency 'dm-validations', '~> 1.0.0'
  s.add_dependency 'dm-timestamps', '~> 1.0.0'
  s.add_dependency 'dm-migrations', '~> 1.0.0'
  s.add_dependency 'slf4r', '~> 0.3.0'
  s.add_dependency 'datamapper4rails', '~> 0.5.0'
  s.add_dependency 'rack-datamapper', '~> 0.3.0'
  s.add_dependency 'logging', '~> 1.2.3'
  s.add_development_dependency 'dm-sqlite-adapter', '~> 1.0.0'
  s.add_development_dependency 'rspec', '~> 1.3.0'
end