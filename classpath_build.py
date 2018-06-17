
# Read the maven-generated classpath file and reformat it with one line per jar.
#
# $ mvn dependency:build-classpath -Dmdep.outputFile=classpath.txt
# $ python3 classpath.py classpath > classpath
# $ python3 classpath.py jar_contents > list_jar_contents.sh
#
# Chris Joakim, 2018/06/17

import sys

classpath_txt = 'doc/classpath.txt'
jar_file      = 'target/spring-boot-docker1-0.1.0.jar'
classes_dir   = 'target/classes'
asof_date     = '2018/06/17'

if __name__ == "__main__":

    if len(sys.argv) > 1:
      func = sys.argv[1].lower()

      if func == 'classpath':
        with open(classpath_txt, 'rt', encoding='utf-8') as f:
            print('')
            print('# This script was generated by classpath.py.')
            print('# It can be "sourced" from within a bash shell script to set')
            print('# and export the CP environment variable; the Java CLASSPATH.')
            print('# Chris Joakim, {}'.format(asof_date))
            print('')
            print('CP=.:{}'.format(classes_dir))
            print('CP=$CP:{}'.format(jar_file))
            for idx, line in enumerate(f):
                jars = line.split(':')
                for jar in jars:
                    print('CP=$CP:{}'.format(jar))
            print('')
            print('export CP')
            print('')

      elif func == 'jar_contents':
        with open(classpath_txt, 'rt', encoding='utf-8') as f:
            print('#!/bin/bash')
            print('# This script was generated by classpath.py.')
            print('# It can be executed to do a "jar -xvf" for each jar in the')
            print('# CLASSPATH, so as to produce a list of all dependent classes for analysis.')
            print('# Chris Joakim, {}'.format(asof_date))
            print('')
            script_file = 'doc/jar_contents.txt'
            for idx, line in enumerate(f):
                jars = line.split(':')
                for jar in jars:
                    print('')
                    print('echo "JAR-FILE: {}" >> {}'.format(jar, script_file))
                    print('jar -tvf {} >> {}'.format(jar, script_file))
            print('')

      else:
        print('unknown command-line function: ' + func)

    else:
      print('error; no command-line function given')
