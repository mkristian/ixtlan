require 'datamapper4rails/datamapper_store'
ActionController::Base.session = { :key => "_server_session" , :cache => true }
ActionController::Base.session_store = :datamapper_store

module DatamapperStore
  class Session
 
    def data=(data)
      d = data.dup
      @user = d.delete(:user)
      attribute_set(:data, ::Base64.encode64(Marshal.dump(d)))
    end

    def data
      Marshal.load(::Base64.decode64(attribute_get(:data))).merge({:user => @user})
    end
  end
end
