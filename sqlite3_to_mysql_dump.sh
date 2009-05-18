cat dump.sql | sed -e s/\"/\`/g -e s/AUTOINCREMENT/AUTO_INCREMENT/g

