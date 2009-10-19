require 'rack_datamapper/session/abstract/store'
module Ixtlan
  class Session < DataMapper::Session::Abstract::Session
 
    def data=(data)
      d = {}
      data.each{|k,v| d[k.to_sym] = v}
      @user = d.delete(:user)
      @flash = d.delete(:flash)
      @expires_at = d.delete(:expires_at)
      attribute_set(:data, ::Base64.encode64(Marshal.dump(d)))
    end
    
    def data
      Marshal.load(::Base64.decode64(attribute_get(:data))).merge({:user => @user, :flash => @flash})
    end
  end
end
