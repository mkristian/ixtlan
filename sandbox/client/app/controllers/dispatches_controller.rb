class DispatchesController < ApplicationController
  
  include DispatchesControllerModule

  @@map.merge!({
    :users =>  "http://localhost:4000/users",
    :profile => "http://localhost:4000/profile/edit"
  })

end
