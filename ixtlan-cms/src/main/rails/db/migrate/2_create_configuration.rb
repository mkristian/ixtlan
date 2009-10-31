migration 2, :create_configuration do
  up do
    Ixtlan::Configuration.auto_migrate!
    Ixtlan::Configuration.create(:session_idle_timeout => 10, :keep_audit_logs => 3, :current_user => Ixtlan::User.first)
  end

  down do
  end
end
