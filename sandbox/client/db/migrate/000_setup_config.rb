migration 0, :setup_config do
  up do
    raise "error" unless Configuration.create(:id => 1, :session_idle_timeout => 5, :keep_audit_logs => 1)
  end

  down do
    Configuration.instance.destroy
  end
end
 
