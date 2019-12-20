#!/bin/bash

cd ./src/
javac -cp ../lib/sqlite-jdbc-3.23.1.jar: *.java
java -cp ../lib/sqlite-jdbc-3.23.1.jar: MenuTextUI
rm -rf *.class
