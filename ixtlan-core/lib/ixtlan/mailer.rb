module Ixtlan
  class Mailer < ActionMailer::Base

    require 'pathname'
    path = Pathname(__FILE__).parent.dirname.to_s
    view_paths << path unless view_paths.member? path

    def password(email_to, email_from, password)
      @subject    = ''
      @body       = {:password => password}
      @recipients = email_to
      @from       = email_from
      @sent_on    = Time.now
      @headers    = {}
    end

    def new_user(email_to, email_from, login, url)
      @subject    = "details for #{url}"
      @body       = {:username => login, :url => url}
      @recipients = email_to
      @from       = email_from
      @sent_on    = Time.now
      @headers    = {}
    end

    def error_notification(email_from, email_to, exception, error_file)
        @subject    = exception.message
        @body       = {:text => "#{error_file}"}
        @recipients = email_to
        @from       = email_from
        @sent_on    = Time.now
        @headers    = {}
      end
  end
end
