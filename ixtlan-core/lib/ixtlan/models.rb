module Ixtlan
  module Models
    AUTHENTICATION = "::Ixtlan::Models::Authentication" unless const_defined? "AUTHENTICATION"
    USER = "::Ixtlan::Models::User" unless const_defined? "USER"
    GROUP = "::Ixtlan::Models::Group" unless const_defined? "GROUP"
    ROLE = "::Ixtlan::Models::Role" unless const_defined? "ROLE"
    PERMISSION = "::Ixtlan::Models::Permission" unless const_defined? "PERMISSION"
    LOCALE = "::Ixtlan::Models::Locale" unless const_defined? "LOCALE"
    CONFIGURATION = "::Ixtlan::Models::Configuration" unless const_defined? "CONFIGURATION"
    TRANSLATION = "::Ixtlan::Models::Translation" unless const_defined? "TRANSLATION"
    TEXT = "::Ixtlan::Models::Text" unless const_defined? "TEXT"
  end
end
