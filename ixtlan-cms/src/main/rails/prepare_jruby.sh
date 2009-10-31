#!/bin/bash

echo
echo "shall freeze rails and fix a bug which prevents rails to use certain"
echo "java gems like the dataobjects drivers !!"
echo

mvn --version
if [ $? -ne 0 ] ; then

        echo "please install maven >= 2.0.9 from maven.apache.org"
        exit -1
fi

mvn de.saumya.mojo:rails-maven-plugin:rails-freeze-gems de.saumya.mojo:rails-maven-plugin:gems-install

echo
echo "you can run rails with (no need to install jruby !!)"
echo
echo "	mvn de.saumya.mojo:rails-maven-plugin:server"
echo
echo "more info on"
echo "	http://github.org/mkristian/rails-maven-plugin"
echo
echo
