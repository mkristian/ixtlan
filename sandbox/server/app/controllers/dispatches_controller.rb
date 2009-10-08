class DispatchesController < ApplicationController
  
  include DispatchesControllerModule

  @@map.merge!({:demo =>  "http://localhost:3000/welcome"})
  
end
