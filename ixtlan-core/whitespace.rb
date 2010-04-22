#!/usr/bin/env ruby

require 'pathname'
require 'zlib'

# files and extensions to process
FILES = %w[ capfile CHANGELOG LICENSE Manifest MIT-LICENSE README QUICKLINKS README_FOR_APP RUNNING_UNIT_TESTS Rakefile SPECS TODO USAGE .autotest .gitignore .htaccess ].freeze
EXTENSIONS = %w[ builder cgi conf css deploy erb example fcgi feature gemspec haml htc htm html java js key markdown opts php rake ratom rb rcsv rdf rhtml rjs rpdf ru rxml sake sass sh sql thor txt vcf xml yml ].freeze

Pathname.glob(ARGV).each do |path_in|
  start_path = path_in.directory? ? path_in + '**/*' : path_in

  Pathname.glob((start_path).to_s).each do |path|
    unless path.file? && path.size? && path.readable? && path.writable? && (FILES.include?(path.basename.to_s) || EXTENSIONS.include?(path.extname[1..-1]))
# puts "Skipping #{path}" if path.file?
      next
    end

    # replace leading whitespace (including tabs) with spaces
    # replace trailing whitespace with a newline
    document = path.open('r') do |f|
      f.collect { |line| line.gsub(/\G\s/, ' ').rstrip + "\n" }.join.rstrip
    end + "\n"

    # skip it if the file was not modified
    next if Zlib.crc32(document) == Zlib.crc32(path.read)

    puts "Modifying #{path}"
    path.open('w') { |f| f.write document }
  end
end
