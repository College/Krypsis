#!/usr/bin/env ruby

# Determine the current path and the target
this_path = File.dirname(File.expand_path(__FILE__))
krypsis_path = "../public/javascripts/krypsis"
path = "#{this_path}/#{krypsis_path}"

# These files will be mapped. The key is the source and
# the value is the targeted minified file
js_files = {
  "/krypsis.js" => "/min/krypsis.min.js",
  "/include/krypsis-all.js" => "/min/krypsis-all.min.js",
  "/include/krypsis-all-mock.js" => "/min/krypsis-all-mock.min.js"
}

# Merge the files
js_files.each do |source, target|
  system "juicer merge -i #{path}#{source} -o #{path}#{target} --force"
end