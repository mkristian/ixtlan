module Ixtlan
  module Rails
    class Migrations

      # assume no namespaces !!!
      USER = Object.const_get(Ixtlan::Models::USER.sub(/^::/, ''))
      GROUP = Object.const_get(Ixtlan::Models::GROUP.sub(/^::/, ''))
      LOCALE = Object.const_get(Ixtlan::Models::LOCALE.sub(/^::/, ''))
      DOMAIN = Object.const_get(Ixtlan::Models::DOMAIN.sub(/^::/, ''))
      CONFIGURATION = Object.const_get(Ixtlan::Models::CONFIGURATION.sub(/^::/, ''))
      TEXT = Object.const_get(Ixtlan::Models::TEXT.sub(/^::/, ''))

      def self.create_user
        USER.auto_migrate!
        LOCALE.auto_migrate!
        GROUP.auto_migrate!
        Ixtlan::Models::GroupUser.auto_migrate!
        Ixtlan::Models::GroupLocaleUser.auto_migrate!
        Ixtlan::Models::DomainGroupUser.auto_migrate!

        u = USER.first
        if u.nil?
          u = USER.new(:login => 'root', :email => 'root@example.com', :name => 'Superuser', :id => 1)
          u.created_at = DateTime.now
          u.updated_at = u.created_at
          u.created_by_id = 1
          u.updated_by_id = 1
          u.save!
        end
        u.reset_password
        u.current_user = u
        u.save

        g = GROUP.create(:name => 'root', :current_user => u)
        u.groups << g
        u.save
        
        a = USER.create(:login => 'admin', :email => 'admin@example.com', :name => 'Administrator', :id => 2, :current_user => u)
        a.reset_password
        a.save!      
        a.groups << GROUP.create(:name => 'admin', :current_user => u)
        a.save
 
        users = GROUP.create(:name => 'users', :current_user => u)
        locales = GROUP.create(:name => 'locales', :current_user => u)
        domains = GROUP.create(:name => 'domains', :current_user => u)
        File.open("root_#{RAILS_ENV}", 'w') { |f| f.puts "root\n#{u.password}\n\nadmin\n#{a.password}\n\n" }
      end

      def self.create_configuration
        Ixtlan::Models::ConfigurationLocale.auto_migrate!
        CONFIGURATION.auto_migrate!
        CONFIGURATION.create(:session_idle_timeout => 10, :keep_audit_logs => 3, :current_user => USER.first)
      end

      def self.create_locale
        LOCALE.auto_migrate!
        # get/create default locale
        LOCALE.create(:code => LOCALE::DEFAULT, :current_user => USER.first)
        # get/create "every" locale
        LOCALE.create(:code => LOCALE::ALL, :current_user => USER.first)
        
        # root user has access to ALL locales
        Ixtlan::Models::GroupLocaleUser.create(:group => Group.first, :user => USER.first, :locale => LOCALE.every)
      end

      def self.create_domain
        DOMAIN.auto_migrate!
        # get/create "every" domain
        DOMAIN.create(:name => DOMAIN::ALL, :current_user => User.first)
        
        Ixtlan::Models::DomainGroupUser.create(:group => Group.first, :user => USER.first, :domain => DOMAIN.every)
      end

      def self.create_text
        TEXT.auto_migrate!
      end
    end
  end
end
