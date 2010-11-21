module Ixtlan
  module Models
    AUTHENTICATION = "::Authentication" unless const_defined? "AUTHENTICATION"
    AUDIT = "::Audit" unless const_defined? "AUDIT"
    USER = "::User" unless const_defined? "USER"
    GROUP = "::Group" unless const_defined? "GROUP"
    GROUP_USER = "::Ixtlan::Models::GroupUser" unless const_defined? "GROUP_USER"
    ROLE = "::Role" unless const_defined? "ROLE"
    PERMISSION = "::Permission" unless const_defined? "PERMISSION"
    LOCALE = "::Locale" unless const_defined? "LOCALE"
    DOMAIN = "::Domain" unless const_defined? "DOMAIN"
    CONFIGURATION = "::Configuration" unless const_defined? "CONFIGURATION"
    TRANSLATION = "::Translation" unless const_defined? "TRANSLATION"
    TEXT = "::I18nText" unless const_defined? "TEXT"
  end
end
