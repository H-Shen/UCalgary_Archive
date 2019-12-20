#!/bin/bash

set -e

GREEN='\033[0;32m'
GREEN_BOLD='\033[1;32m'
RESET='\033[0m'

cp -r ./test/*.* ./src
cd ./src

echo "Start testing..."

javac -encoding utf8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: *.java
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore ValidationTest 2>&1 >/dev/null
echo -e "${GREEN}Validation class tests PASSED!${RESET}"
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore CourseTest 2>&1 >/dev/null
echo -e "${GREEN}Course class tests PASSED!${RESET}"
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore CourseAndGradeTest 2>&1 >/dev/null
echo -e "${GREEN}CourseAndGrade class tests PASSED!${RESET}"
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore RandomUserGeneratorTest 2>&1 >/dev/null
echo -e "${GREEN}RandomUserGenerator class tests PASSED!${RESET}"
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore StudentTest 2>&1 >/dev/null
echo -e "${GREEN}Student class tests PASSED!${RESET}"
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore FacultyTest 2>&1 >/dev/null
echo -e "${GREEN}Faculty class tests PASSED!${RESET}"
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore DateOfBirthTest 2>&1 >/dev/null
echo -e "${GREEN}DateOfBirthTest class tests PASSED!${RESET}"
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore DateOfBirthTest 2>&1 >/dev/null
echo -e "${GREEN}AdminTextUITest class tests PASSED!${RESET}"
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore AdminTextUITest 2>&1 >/dev/null
echo -e "${GREEN}DatabaseTest class tests PASSED!${RESET}"
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore DatabaseTest 2>&1 >/dev/null
echo -e "${GREEN}DateOfBirthTest class tests PASSED!${RESET}"
java -Dfile.encoding=UTF8 -cp ../lib/json-simple-1.1.jar:../lib/jsoup-1.11.3.jar:../lib/sqlite-jdbc-3.27.2.1.jar:../lib/junit-4.12.jar:../lib/hamcrest-core-1.3.jar: org.junit.runner.JUnitCore UserTest 2>&1 >/dev/null
echo -e "${GREEN}UserTest class tests PASSED!${RESET}"

echo "Cleaning temporary files..."

rm -rf *.class
rm -rf ValidationTest.java
rm -rf CourseTest.java
rm -rf CourseAndGradeTest.java
rm -rf RandomUserGeneratorTest.java
rm -rf StudentTest.java
rm -rf FacultyTest.java
rm -rf DateOfBirthTest.java
rm -rf AdminTextUITest.java
rm -rf DatabaseTest.java
rm -rf UserTest.java

echo ""
echo -e "${GREEN_BOLD}All tests PASSED!${RESET}"
