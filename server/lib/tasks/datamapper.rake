namespace :db do

  desc "Perform automigration"
  task :automigrate => :environment do
    FileList["app/models/**/*.rb"].each do |model|
      load model
    end
    ::DataMapper.auto_migrate!
  end

  desc "Perform non destructive automigration"
  task :autoupgrade => :environment do
    FileList["app/models/**/*.rb"].each do |model|
      load model
    end
    ::DataMapper.auto_upgrade!
  end
end

namespace :dm do
  namespace :migrate do
    task :load => :environment do
      gem 'dm-migrations'
      require "migration_runner"
      FileList["db/migrate/**/*.rb"].each do |migration|
        load migration
      end
    end
    
    desc "Migrate up using migrations"
    task :up => :load do |t, args|
      version = args[:version]
      migrate_up!(version)
    end

    desc "Migrate down using migrations"
    task :down => :load do |t, args|
      version = args[:version]
      migrate_down!(version)
    end
  end

  desc "Migrate the database to the latest version"
  task :migrate => 'db:migrate:up'
end
