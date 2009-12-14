Time::DATE_FORMATS[:default] = lambda { |time| time.utc.strftime("%Y-%m-%d %H:%M:%S") }

