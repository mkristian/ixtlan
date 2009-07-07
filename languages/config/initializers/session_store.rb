# Be sure to restart your server when you modify this file.

# Your secret key for verifying cookie session data integrity.
# If you change this key, all old sessions will become invalid!
# Make sure the secret is at least 30 characters and all random, 
# no regular words or you'll be exposed to dictionary attacks.
ActionController::Base.session = {
  :key         => '_languages_session',
  :secret      => 'c2a54398dc70da43b505548e5d1f179b83b466df37b8a98977a2760939b1c5a84834a0cf83a768550308a87fa6c85c4651fcd14e02475e9d5c6035b38e07415b'
}

# Use the database for sessions instead of the cookie-based default,
# which shouldn't be used to store highly confidential information
# (create the session table with "rake db:sessions:create")
# ActionController::Base.session_store = :active_record_store
