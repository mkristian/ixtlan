migration 1, :create_root_user do
  up do
    Ixtlan::User.auto_migrate!
    Ixtlan::Locale.auto_migrate!
    Ixtlan::Group.auto_migrate!
    Ixtlan::GroupUser.auto_migrate!

    u = Ixtlan::User.new(:login => 'root', :email => 'root@exmple.com', :name => 'Superuser', :language => 'en', :id => 1)
    #u.current_user = u
    u.created_at = DateTime.now
    u.updated_at = u.created_at
    u.created_by_id = 1
    u.updated_by_id = 1
    u.reset_password
    u.save!
    g = Ixtlan::Group.create(:name => 'root', :current_user => u)
    u.groups << g
    u.save
    STDERR.puts "#{u.login} #{u.password}"
  end

  down do
  end
end
