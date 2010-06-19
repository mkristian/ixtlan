# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = %q{ixtlan}
  s.version = "0.4.0.pre3"

  s.required_rubygems_version = Gem::Requirement.new("> 1.3.1") if s.respond_to? :required_rubygems_version=
  s.authors = ["mkristian"]
  s.date = %q{2010-05-29}
  s.description = %q{this is set of rails and datamapper plugins for setting up a little more advanced rails application then the default rails generator does. the focus is on security and privacy as well a complete restful xml support.}
  s.email = ["m.kristian@web.de"]
  s.extra_rdoc_files = ["History.txt", "README.txt"]
  s.files = ["History.txt", "MIT-LICENSE", "README.txt", "Rakefile"]
  s.files = s.files +
    Dir.glob("generators/**/*.java") +
    Dir.glob("generators/**/*rb") +
    Dir.glob("lib/**/*rb")
  s.test_files = Dir.glob("spec/**/*.rb")
  s.homepage = %q{http://github.com/mkristian/ixtlan-core}
  s.rdoc_options = ["--main", "README.txt"]
  s.require_paths = ["lib"]
  s.rubyforge_project = %q{ixtlan}
  s.rubygems_version = %q{1.3.5}
  s.summary = %q{this is set of rails and datamapper plugins for setting up a little more advanced rails application then the default rails generator does}

  s.add_runtime_dependency(%q<dm-core>, ["~> 1.0.0"])
  s.add_runtime_dependency(%q<dm-validations>, ["~> 1.0.0"])
  s.add_runtime_dependency(%q<dm-timestamps>, ["~> 1.0.0"])
  s.add_runtime_dependency(%q<dm-migrations>, ["~> 1.0.0"])
  s.add_runtime_dependency(%q<slf4r>, ["~> 0.3.1"])
  s.add_runtime_dependency(%q<datamapper4rails>, ["~> 0.4.0"])
  s.add_runtime_dependency(%q<rack-datamapper>, ["~> 0.3.0"])
  s.add_runtime_dependency(%q<logging>, ["~> 1.2.3"])
  s.add_development_dependency(%q<rspec>, ["~> 1.3.0"])
end

