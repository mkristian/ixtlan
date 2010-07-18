require 'rack_datamapper/session/abstract/store'
module Ixtlan
  class Session < DataMapper::Session::Abstract::Session

    def data=(data)
      d = {}
      data.each{|k,v| d[k.to_sym] = v}
      d.delete(:user)
      attribute_set(:raw_data, ::Base64.encode64(Marshal.dump(d)))
    end

    def data
      # use string for flash entry to allow the rails flash to work properly !
      d = Marshal.load(::Base64.decode64(attribute_get(:raw_data)))
      d["flash"] = d.delete(:flash)
      d
    end
  end
end
