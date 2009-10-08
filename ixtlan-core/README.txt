= ixtlan core

* http://github.com/mkristian/ixtlan-core

== DESCRIPTION:

this is set of rails and datamapper plugins for setting up a little more advanced rails application then the default rails generator does. the focus is on security and privacy as well a complete restful xml support.

=== features

* usermanagement: user, group, locale

* authentication: session based authentication for both html as well restful xml

* authorization: each controller/action pair has a set of allowed roles

* privacy: configurable duration for logfiles carrying user specific data. error logs are dumped (complete environment including user specific data as well) and the file location is sent as notification

* session idle timeout: configurable server side session idle timeout

* audit: a simple log file which documents the action of a user - one action per line

* optimistic transaction: through an exception on modification of stale resources

* easy add modified_by attributes to a resource and ensure the user gets set before saving such a resource

* logger configuration tries to unify logging

* basic scaffold like html interface for user,group,locale

* rails template setting up such an application - works for both ruby as well jruby

* http cache headers so that no data gets save on any proxy or filesystem (as long the user is logged in)

* global config: extra configuration file which can carry the all the production passwords and can be left out from the (public) version control system: config/preinitializer.rb for rails and config/global.yml

=== TODOs

* session timeout on html pages so the browser displays the login page after timetout (little modification of the layout.html.erb)

* user, group, locale to work also with ldap

* html interface for configuration, user profile

* setup database specific logger for data_objects - each driver has its own logger

* maintanance mode: allow only users who belong to the superuser group

* change the db config to have #{RAILS_ROOT} in front of relative filenames to work inside an servlet engine (ixtlan_rails_templates.rb)

* locale binding to user/group

== user management and authorization

each user can belong to one or more groups, each user/group pair can belong to one or more locales. this part can be configured by an admin by changing resource relationships.

=== authorization

each controller/action pair has a set of allowed roles. authorization is granted if on of roles match with one of the groups of the current user (logged in user). this is done by a before filter in rails: guard. the user interface can introspect the guard to allow the user only the actions which s/he actually can be performe. in case some actions needs to verify the locale binding a specialized before filter needs to be implemented.

the roles are currently hardcoded in app/guards/XYZ_guard.rb - one guard for each controller.

== LICENSE:

(The MIT License)

Copyright (c) 2009 Kristian Meier

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
'Software'), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
