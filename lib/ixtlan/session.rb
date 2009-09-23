require 'rack_datamapper/session/abstract/store'
module Ixtlan
  class Session < DataMapper::Session::Abstract::Session
 
    def data=(data)
      d = data.dup
      @user = d.delete(:user)
      @flash = d.delete(:flash)
      attribute_set(:data, ::Base64.encode64(Marshal.dump(d)))
    end
    
    def data
      Marshal.load(::Base64.decode64(attribute_get(:data))).merge({:user => @user, :flash => @flash})
    end
  end
end
