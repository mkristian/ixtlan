Time::DATE_FORMATS[:xml] = lambda { |time| time.utc.strftime("%Y-%m-%d %H:%M:%S") }

