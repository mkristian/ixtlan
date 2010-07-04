module Ixtlan
  class AuditRack
    AUDIT = Ixtlan::Models::AUDIT.nil? ? nil : Object.full_const_get(Ixtlan::Models::AUDIT)

    def initialize(app)
      @app = app
    end

    def call(env)
      result = @app.call(env)
      if AUDIT
        AUDIT.pop_all.each do |audit|
          audit.save
        end
      end
      result
    end

  end
end
