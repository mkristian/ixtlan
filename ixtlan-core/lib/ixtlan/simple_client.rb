require 'net/http'
module Ixtlan
  class NoAuthenticationError < StandardError; end

  class SimpleClient
    
    def initialize(options)
      @host = options[:host]
      @port = (options[:port] || 80).to_i
      @username = options[:username]
      @password = options[:password]
    end

    # load resources has not payload 
    # maybe a query which depends on the service
    def get(path, payload = nil)
      req = Net::HTTP::Get.new(path)
      action(req, payload)
    end

    def post(path, payload)
      req = Net::HTTP::Post.new(path)
      action(req, payload)
    end

    def put(path, payload)
      req = Net::HTTP::Put.new(path)
      action(req, payload)
    end

    # delete a single resource has no payload
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

      result = nil
      http = Net::HTTP.new(@host, @port)
      http.read_timeout = 999
      
      response = http.start do |h|
        h.request(req) do |response|
          if response.kind_of?(Net::HTTPSuccess)
            # in case there is a new token
            @token = response['Authentication-Token'] || @token
            # read result
            result = response.read_body
          elsif response.kind_of?(Net::HTTPUnauthorized)
            @token = nil
            @cookie = nil
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
      req.body = "<authentication><login>#{@username}</login><password>#{@password}</password></authentication>"
      req.content_length = req.body.size 

      http = Net::HTTP.new(@host, @port)
      http.read_timeout = 999

      response = http.start do |h|
        h.request(req) do |response|
          if response.kind_of?(Net::HTTPSuccess)
            # read body to close connection
            response.read_body
            # keep session tracking info for further request
            @token = response['Authentication-Token']
            # take on the session_id part of the cookie
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
