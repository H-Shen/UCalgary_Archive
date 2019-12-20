#!/bin/bash

cp -r ./test/*.java ./src
cd ./src

javac -encoding utf8 -cp ../lib/sqlite-jdbc-3.23.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: *.java
java -Dfile.encoding=UTF8 -cp ../lib/sqlite-jdbc-3.23.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore ValidationTest
java -Dfile.encoding=UTF8 -cp ../lib/sqlite-jdbc-3.23.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore CourseTest
java -Dfile.encoding=UTF8 -cp ../lib/sqlite-jdbc-3.23.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore CourseAndGradeTest
java -Dfile.encoding=UTF8 -cp ../lib/sqlite-jdbc-3.23.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore RandomUserGeneratorTest
java -Dfile.encoding=UTF8 -cp ../lib/sqlite-jdbc-3.23.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore StudentTest
java -Dfile.encoding=UTF8 -cp ../lib/sqlite-jdbc-3.23.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore FacultyTest
java -Dfile.encoding=UTF8 -cp ../lib/sqlite-jdbc-3.23.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore DateOfBirthTest

rm -rf *.class
rm -rf *Test.java
echo "Test completed!"
