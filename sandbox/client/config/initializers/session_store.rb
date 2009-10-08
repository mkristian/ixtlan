# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_client_session',
  :secret      => 'aa612f887dfda7eecad5e462d5863a6aa8aacda4ea6644d18b1e1810fffe21c1279ebef1f57a3cd4bd08aa635fa95a1c0d77e4e07ad65d4deb474ade99568dc1'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store
