require 'rack_datamapper/session/abstract/store'
module Ixtlan
  class SessionWithCache < DataMapper::Session::Abstract::Session

    def data=(data)
      d = {}
      data.each{|k,v| d[k.to_sym] = v}
      @user = d.delete(:user)
      @flash = d.delete(:flash)
      @expires_at = d.delete(:expires_at)
      attribute_set(:raw_data, ::Base64.encode64(Marshal.dump(d)))
    end

    def data
      # use string key for flash entry to allow the rails flash to work properly !
      Marshal.load(::Base64.decode64(attribute_get(:raw_data))).merge({:user => @user, "flash" => @flash, :expires_at => @expires_at})
    end
  end
end
