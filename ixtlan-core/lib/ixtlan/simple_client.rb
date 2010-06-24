require 'net/http'
module Ixtlan
  class NoAuthenticationError < StandardError; end

  # restful XML client using HTTP (no HTTPS support !!)
  class SimpleClient
    
    def initialize(options)
      @host = options[:host]
      @port = (options[:port] || 80).to_i # defaults to 80
      @username = options[:username]
      @password = options[:password]
    end

    # restful get for the given path (like '/users.xml' or '/users/1.xml').
    # which retrieves the data
    # usually there is no payload - maybe a query if the service accepts it
    def get(path, payload = nil)
      req = Net::HTTP::Get.new(path)
      action(req, payload)
    end

    # restful post for the given path (like '/users.xml').
    # which creates a new resource or new resources (if the underlying 
    # service provides bulk creation)
    def post(path, payload)
      req = Net::HTTP::Post.new(path)
      action(req, payload)
    end

    # restful put for the given path (like '/users/1.xml').
    # which updates a given resource or many resources (if the underlying 
    # service provides bulk updates - path like '/users.xml')
    def put(path, payload)
      req = Net::HTTP::Put.new(path)
      action(req, payload)
    end

    # restful delete for the given path (like '/users/1.xml').
    # delete a single resource (with no payload) or many resources 
    # (if the underlying service provides bulk updates - path like '/users.xml')
    def delete(path, payload = nil)
      req = Net::HTTP::Delete.new(path)
      action(req, payload)
    end

    private

    def action(req, payload)
      _action(req, payload)
    rescue NoAuthentiacionError
      # can happen after the session got an idle timeout
      # give it one other trial
      _action(req, payload)
    end

    def _action(req, payload)
      # authenticate if needed
      unless(@cookie && @token)
        authenticate
      end

      # setup request with session tracking
      req.content_length = payload.to_s.size 
      req.body = payload.to_s
      req['Content-Type'] = 'application/xml'
      req['Authentication-Token'] = @token
      req['Cookie'] = @cookie

      http = Net::HTTP.new(@host, @port)
      http.read_timeout = 999
      
      result = nil
      response = http.start do |h|
        h.request(req) do |response|
          if response.kind_of?(Net::HTTPSuccess)
            # in case there is a new token keep it for the next request
            @token = response['Authentication-Token'] || @token
            # read result
            result = response.read_body
          elsif response.kind_of?(Net::HTTPUnauthorized)
            # clear session tracking data
            @token = nil
            @cookie = nil
            # this exception indicates authentication problem and
            # can trigger retry
            raise NoAuthenticationError.new
          else
            # TODO maybe better error handling
            raise "http error: #{response.inspect}\n\t#{response.body}"
          end
        end 
      end
      result
    end

    def authenticate
      # setup request with authencitation.xml
      req = Net::HTTP::Post.new("/authentication.xml")
      req['Content-Type'] = 'application/xml'

      # the login payload
      req.body = "<authentication><login>#{@username}</login><password>#{@password}</password></authentication>"
      req.content_length = req.body.size 

      http = Net::HTTP.new(@host, @port)
      http.read_timeout = 999

      response = http.start do |h|
        h.request(req) do |response|
          if response.kind_of?(Net::HTTPSuccess)
            # read body to close connection - be nice client
            response.read_body
            # keep session tracking info for further request
            @token = response['Authentication-Token']
            # take only the session_id part of the cookie
            @cookie = response['Set-Cookie'].sub(/;.*/, '')
          else
            # TODO maybe better error handling
            raise "http error: #{response.inspect}\n\t#{response.body}"
          end
        end
      end
    end
  end
end
