#!/usr/bin/env ruby
this_path = File.dirname(File.expand_path(__FILE__))

command =  "java -jar #{this_path}/jsdoc/jsrun.jar #{this_path}/jsdoc/app/run.js #{this_path}/../public/javascripts/krypsis/ "
command += "-r=2 "
command += "-t=#{this_path}/jsdoc/templates/jsdoc "
command += "-d=#{this_path}/../public/doc "
command += "-E='min|include' "
command += "-v "

system command