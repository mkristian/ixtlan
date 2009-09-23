# -*- ruby -*-

require 'rubygems'
require 'hoe'
require './lib/ixtlan/version.rb'

require 'spec'
require 'spec/rake/spectask'
require 'pathname'
require 'yard'

Hoe.new('ixtlan', Ixtlan::VERSION) do |p|
  p.developer('mkristian', 'm.kristian@web.de')
end

desc 'Install the package as a gem.'
task :install => [:clean, :package] do
  gem = Dir['pkg/*.gem'].first
  sh "gem install --local #{gem} --no-ri --no-rdoc"
end

desc 'Run specifications'
Spec::Rake::SpecTask.new(:spec) do |t|
  if File.exists?('spec/spec.opts')
    t.spec_opts << '--options' << 'spec/spec.opts'
  end
  t.spec_files = Pathname.glob('./spec/**/*_spec.rb')
end

YARD::Rake::YardocTask.new

# vim: syntax=Ruby
