migration 0, :setup_config do
  up do
    Configuration.create(:session_idle_timeout => 5, :id => 1)
  end

  down do
    Configuration.instance.destroy
  end
end
 
