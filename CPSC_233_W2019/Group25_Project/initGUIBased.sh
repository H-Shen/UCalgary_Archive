#!/bin/bash

set -e

cd ./src/
javac -encoding utf8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar: *.java
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar: MenuGUI
rm -rf *.class
