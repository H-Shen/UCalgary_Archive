#!/bin/bash

cd ./src/
javac -encoding utf8 -cp ../lib/sqlite-jdbc-3.23.1.jar: *.java
java -Dfile.encoding=UTF8 -cp ../lib/sqlite-jdbc-3.23.1.jar: MenuTextUI
rm -rf *.class
