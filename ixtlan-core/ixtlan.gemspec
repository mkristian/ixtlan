# -*- encoding: utf-8 -*-

Gem::Specification.new do |s|
  s.name = %q{ixtlan}
  s.version = "0.4.0.pre2"

  s.required_rubygems_version = Gem::Requirement.new("> 1.3.1") if s.respond_to? :required_rubygems_version=
  s.authors = ["mkristian"]
  s.date = %q{2010-05-29}
  s.description = %q{this is set of rails and datamapper plugins for setting up a little more advanced rails application then the default rails generator does. the focus is on security and privacy as well a complete restful xml support.}
  s.email = ["m.kristian@web.de"]
  s.extra_rdoc_files = ["History.txt", "Manifest.txt", "README.txt"]
  s.files = ["History.txt", "MIT-LICENSE", "Manifest.txt", "README.txt", "Rakefile"]
  s.files << Dir.glob("generators/**/*.java")
  s.files << Dir.glob("generators/**/*.rb")
  s.files << Dir.glob("lib/**/*.rb")
  s.test_files = Dir.glob("spec/**/*.rb")
  s.homepage = %q{http://github.com/mkristian/ixtlan-core}
  s.rdoc_options = ["--main", "README.txt"]
  s.require_paths = ["lib"]
  s.rubyforge_project = %q{ixtlan}
  s.rubygems_version = %q{1.3.5}
  s.summary = %q{this is set of rails and datamapper plugins for setting up a little more advanced rails application then the default rails generator does}

  s.add_runtime_dependency(%q<dm-core>, ["~> 0.10.1"])
  s.add_runtime_dependency(%q<dm-validations>, ["~> 0.10.1"])
  s.add_runtime_dependency(%q<dm-timestamps>, ["~> 0.10.1"])
  s.add_runtime_dependency(%q<dm-migrations>, ["~> 0.10.1"])
  s.add_runtime_dependency(%q<slf4r>, ["~> 0.2.0"])
  s.add_runtime_dependency(%q<datamapper4rails>, ["~> 0.4.0"])
  s.add_runtime_dependency(%q<rack-datamapper>, ["~> 0.2.5"])
  s.add_runtime_dependency(%q<logging>, ["~> 1.2.3"])
  s.add_development_dependency(%q<hoe>, [">= 2.3.3"])
end

