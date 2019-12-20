A fork of the original project of Group25, will be updated inconsistently.

This readme file has been adapted from the readme file template from PurpleBooth: https://gist.github.com/PurpleBooth/109311bb0361f32d87a2

![](https://img.shields.io/badge/Form--based-App-https%3A%2F%2Fihttps%3A%2F%2Fimg.shields.io%2Fbadge%2F--brightgreen--brightgreen.svgmg.shields.io%2Fbadge%2F--brightgreen--brightgreen.svg.svg)
[![](https://img.shields.io/badge/SQLite3-JDBC-httpshttps%3A%2F%2Fimg.shields.io%2Fbadge%2F--informational--informational.svg%3A%2F%2Fimg.shields.io%2Fbadge%2F--brightgreen--brightgreen.svg.svg)](https://github.com/xerial/sqlite-jdbc)
[![](https://img.shields.io/badge/Web%20Scraper-Jsoup-https%3A%2F%2Fimg.shields.io%2Fbadge%2F--blue--blue.svg.svg)](https://jsoup.org/)
[![](https://img.shields.io/badge/UCalgary%20Calender-Course%20Description-https%3A%2F%2Fimg.shields.io%2Fbadge%2F--blue--blue.svg.svg)](https://www.ucalgary.ca/pubs/calendar/current/course-desc-main.html)

# Student & Faculty Information Manager

This is a form-based application which allows students, faculty members and administrators of a university institution to create and manage accounts. Functionality for adding students, adding courses, dropping courses, checking GPA, checking courses already taken and their GPA, and change personal information. For faculty, users will be able to access courses they are currently teaching, look at students enrolled in the courses, change and add grades, as well as change their own personal information. The admin users will be able to look at all users enrolled in the institutions, change personal information in other users, delete accounts, add courses, edit courses, delete courses, as well as edit their own personal information. </br>
The most important concept we introduce in the app is data interaction/streams between different objects. Meanwhile, we spent a lot of time to make sure that the interaction will not break the integrity of database or any containers, objects in the java code.</br>
Some strategies, such as parameterized sql and log files, are also introduced to protect the database.

### Screenshots

**All users are able to check information of a specific course. There are totally 2551 courses listed which are collected by a built-in web crawler.**

![](https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Search_A_Course.png)

**An administrator can edit a course except its credits.**

![](https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Edit_A_Course.png)

**An administrator can check who logged in when and what operations he did during his online.**

![](https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Check_Login_History.png)

**An administrator can add an account to the database.**

<center>
<img src="https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Add_an_account.png" width = "60%">
</center>

**The new password for a user should satisfy a set of basic rules for the purpose of security.**

<center>
<img src="https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Invalid_Password_Example.png" width = "60%">
</center>

**The password that satisfied the set of basic rules may not be considered as a strong one since there is another built-in algorithm to evaluate its intensity, and a message will be poped up to warn the user when such case happens if he edits his account.**

<center>
<img src="https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Weak_Password.png" width = "60%">
</center>

**An administrator can check members with a specific role by the keyword filter.**

![](https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Obtain_All_Faculty_Members.png)

**A student is able to check all its past and current grade.**

<center>
<img src="https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Check_Your_Grades.png" width = "60%">
</center>

**A student is able to check if a course is in his academic requirement. So CPSC355 is mandatory for both graduation and internship of a CPSC-major student.**

![](https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Check_req.png)

**All open courses are shown to students for enrollment.**

<center>
<img src="https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Check_Enrollment.png" width = "60%">
</center>

**A faculty member who is currently teaching PHIL279 can check how many students enrolled the course, as well as decide their final grade.**

![](https://raw.githubusercontent.com/H-Shen/CPSC_233_W2019/master/Group25_Project/res/Grading_a_student.png)

### Prerequisites

To run this program you will need:
*   An updated version of JDK (>= 1.8.0_161)
*   An updated version of the repository
*   All plugins with the correct version listed in [Versions pane](#versions-pane)
*   If you plan to run the app in Windows, SQLite3 and all plugins listed in the [Versions pane](#versions-pane) must be installed separately, meanwhile, make sure the convert statements in shell scripts in order to make them run in Command Prompt(CMD.exe) or PowerShell.

### Instructions of setup in *nix system

Execute the commands as below in a *nix terminal:

```bash
~$ git clone https://github.com/H-Shen/CPSC_233_W2019.git
~$ cd ./CPSC_233_W2019/Group25_Project
~$ bash initTest.sh                     # for running junit test samples
~$ bash initTextBased.sh                # for running text-based version
~$ bash initGUIBased.sh                 # for running GUI-based version
```

If one of initTest.sh, initTextBased.sh and initGUIBased.sh has no permissions, you can adjust by typing this before execution under *./CPSC_233_W2019/Group25_Project* :

```bash
~$ chmod +x initTest.sh
~$ chmod +x initTextBased.sh
~$ chmod +x initGUIBased.sh
```
## Testing environment

We have tested on:

*   Linux csx.cs.ucalgary.ca 4.17.4-200.fc28.x86_64 GNU/Linux Fedora release 28 (Twenty Eight)<br/>
*   macOS Mojave 10.14.4 18E226 x86_64
*   Windows 10 Home 1803 (OS Build 17132.706)

## <a name="versions-pane"></a>Versions

*   javac 1.8.0_161<br/>
*   Java(TM) SE Runtime Environment (build 1.8.0_161-b12)<br/>
*   Java HotSpot(TM) 64-Bit Server VM (build 25.161-b12, mixed mode)<br/>
*   sqlite 3.22.0<br/>
*   sqlite-jdbc-3.27.2.1 from https://github.com/xerial/sqlite-jdbc<br/>
*   junit-4.12 from https://github.com/junit-team/junit4<br/>
*   jsoup-1.11.3.jar https://jsoup.org/<br/>
*   json-simple-1.1.jar https://code.google.com/archive/p/json-simple/<br/>
*   hamcrest-core-1.3.jar https://github.com/hamcrest/JavaHamcrest
*   GNU bash, version 4.4.23(1)-release (x86_64-redhat-linux-gnu)

## Default accounts and passwords for testing or demo
Here we list some accounts and corresponding passwords in order to make sure the user can experience different roles:</br>

Username            | Password         | Role                  |
--------------------|------------------|:---------------------:|
demo		    | demodemo         | ADMIN   	       |
demoFaculty         | demodemo         | FACULTY   	       |
demoFaculty1        | demodemo         | FACULTY               |
demoFaculty3        | demodemo         | FACULTY               |
demoStudent         | demodemo         | STUDENT               |
demoStudent1        | demodemo         | STUDENT               |


## Project Structure

### File Structure
```bash
├── db
│   ├── ACADEMIC_REQUIREMENTS.db
│   ├── ACCOUNT_DATA.db
│   ├── COURSES.db
│   ├── COURSES_ORIGINAL.db
│   ├── FACULTY.db
│   ├── INTERNSHIP_REQUIREMENTS.db
│   └── STUDENTS.db
├── initGUIBased.sh
├── initTest.sh
├── initTextBased.sh
├── lib
│   ├── hamcrest-core-1.3.jar
│   ├── json-simple-1.1.jar
│   ├── jsoup-1.11.3.jar
│   ├── junit-4.12.jar
│   └── sqlite-jdbc-3.27.2.1.jar
├── res
│   ├── banner.txt  # a plain text file stores the ASCII-picture of banner for text-based of the app
│   └── log.txt     # a plain text file stores all history log of the users
├── src
│   ├── Admin.java
│   ├── AdminGUI.fxml
│   ├── AdminGUI.java
│   ├── AdminTextUI.java
│   ├── Constants.java
│   ├── Course.java
│   ├── CourseAddGUI.fxml
│   ├── CourseAddGUI.java
│   ├── CourseAndGrade.java
│   ├── CourseAndGradeGUI.java
│   ├── CourseEnrollmentGUI.java
│   ├── CourseForGUI.java
│   ├── CourseHistoryGUI.fxml
│   ├── CourseHistoryGUI.java
│   ├── CourseInfoEditGUI.fxml
│   ├── CourseInfoEditGUI.java
│   ├── CourseManagementGUI.fxml
│   ├── CourseManagementGUI.java
│   ├── Database.java
│   ├── DateOfBirth.java
│   ├── Faculty.java
│   ├── FacultyGUI.fxml
│   ├── FacultyGUI.java
│   ├── FacultyTextUI.java
│   ├── FontAndColorControl.css
│   ├── Log.java
│   ├── LogGUI.fxml
│   ├── LogGUI.java
│   ├── MenuGUI.css
│   ├── MenuGUI.fxml
│   ├── MenuGUI.java
│   ├── MenuTextUI.java
│   ├── RandomUserGenerator.java
│   ├── Student.java
│   ├── StudentEnrollment.fxml
│   ├── StudentEnrollmentGUI.java
│   ├── StudentGUI.fxml
│   ├── StudentGUI.java
│   ├── StudentTextUI.java
│   ├── StudentWithGradeForGUI.java
│   ├── TextUI.java
│   ├── User.java
│   ├── UserAddGUI.fxml
│   ├── UserAddGUI.java
│   ├── UserForGUI.java
│   ├── UserInfoEditGUI.fxml
│   ├── UserInfoEditGUI.java
│   ├── UserInfoGUI.fxml
│   ├── UserInfoGUI.java
│   ├── Utility.java
│   ├── Validation.java
│   └── WebScraper.java
└── test                        # This directory stores all Junit test classes
    ├── AdminTextUITest.java
    ├── CourseAndGradeTest.java
    ├── CourseTest.java
    ├── DatabaseTest.java
    ├── DateOfBirthTest.java
    ├── FacultyTest.java
    ├── RandomUserGeneratorTest.java
    ├── StudentTest.java
    ├── UserTest.java
    └── ValidationTest.java
```
### Structure of Main Database Files
```sql
// ACCOUNT_DATA.db
CREATE TABLE "USER" (
	"UUID"	TEXT NOT NULL UNIQUE,
	"ROLE"	TEXT NOT NULL DEFAULT 'ADMIN' CHECK(ROLE='ADMIN' OR ROLE='STUDENT' OR ROLE='FACULTY'),
	"USERNAME"	TEXT NOT NULL UNIQUE,
	"FULLNAME"	TEXT NOT NULL,
	"GENDER"	TEXT NOT NULL CHECK(GENDER='M' OR GENDER='F'),
	"DATE_OF_BIRTH"	TEXT NOT NULL,
	"EMAIL"	TEXT NOT NULL,
	"PHONE"	TEXT NOT NULL,
	"ADDRESS"	TEXT NOT NULL,
	"PASSWORD"	TEXT NOT NULL,
	PRIMARY KEY("UUID")
)

// COURSES.db
CREATE TABLE "COURSES" (
	"COURSE_ID"	TEXT NOT NULL UNIQUE,
	"COURSE_NAME"	TEXT NOT NULL,
	"COURSE_DESCRIPTION"	TEXT NOT NULL,
	"COURSE_UNITS"	TEXT NOT NULL,
	"PREREQUISITES"	TEXT,
	"ANTIREQUISITES"	TEXT,
	"CAN_BE_REPEATED"	TEXT NOT NULL DEFAULT 'NO' CHECK(CAN_BE_REPEATED='YES' OR CAN_BE_REPEATED='NO'),
	PRIMARY KEY("COURSE_ID")
)

// ACADEMIC_REQUIREMENTS.db and INTERNSHIP_REQUIREMENTS.db
CREATE TABLE "OPTIONAL_COURSES" (
	"COURSE_ID"	TEXT NOT NULL UNIQUE,
	PRIMARY KEY("COURSE_ID")
)
CREATE TABLE "MANDATORY_COURSES" (
	"COURSE_ID"	TEXT NOT NULL UNIQUE,
	PRIMARY KEY("COURSE_ID")
)

// FACULTY.db
CREATE TABLE "FACULTY" (
	"UUID"	TEXT NOT NULL UNIQUE,
	"COURSES_TEACHING"	TEXT NOT NULL,
	PRIMARY KEY("UUID")
)

// STUDENTS.db
CREATE TABLE "STUDENTS" (
	"UUID"	TEXT NOT NULL UNIQUE,
	"TAKEN_COURSES"	TEXT NOT NULL,
	"CURRENT_COURSES"	TEXT NOT NULL,
	PRIMARY KEY("UUID")
)

// COURSES_ORIGINAL.db
CREATE TABLE "COURSES" (
	"COURSE ID"	TEXT NOT NULL UNIQUE,
	"COURSE TITLE"	TEXT,
	"COURSE DESCRIPTION"	TEXT,
	"COURSE CREDITS"	TEXT,
	"COURSE PREREQUISITES"	TEXT,
	"COURSE ANTIREQUISITES"	TEXT,
	"COURSE COREREQUISITES"	TEXT,
	"COURSE CAN BE REPEATED FOR GPA"	TEXT,
	PRIMARY KEY("COURSE ID")
)
```

## Limitations of Project
* Since SQLite is not a client–server database engine, it is hard to simulate an environment where different users can access the database from different IPs, though in this project we can allow users to access by using SSH tunnel, which is overhead because we have to introduce policies on access control and security of connection.
* Pure SQLite does not support the feature of data encryption, thus we tried to introduce SQLite Encryption Extension at the first place, but we abandon it later because users must compile the extension in C, thus we cannot guarantee it can be successfully compiled in the user's environment.

## What did we do to avoid SQL injection?
* Make every field of the item in every database file as a string type, define a set of criteria about the correct format of user input, and use hash sets or regular expressions to validate.
* Use [***parameterized statement***](https://en.wikipedia.org/wiki/Prepared_statement) before updating the database.
* Every IP address came from every attempted login will be logged in [log.txt](https://github.com/H-Shen/CPSC_233_W2019/blob/master/Group25_Project/res/log.txt) in order to help administrators capture any suspect behavior.

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* **Haohu Shen**
* **Alexandra Tenney**
* **Dwight Pittman**
* **Lan Jin**
* **Tiffany Tang**

See also the list of [contributors](https://github.com/alextenney/Group25_Project/graphs/contributors) who participated in this project
