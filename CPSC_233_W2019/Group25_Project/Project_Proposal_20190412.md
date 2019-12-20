# A Student & Faculty Information Manager

**CPSC 233 Group 25 Project Proposal**

April 12, 2019

## Brief Description

This application is in the category Form-Based Application for CPSC 233 Winter2019. The app simulates a system used by academic institutions that allow for curriculum, grades, courses enrollment and accounts management. The system will consist of three modules, which are used by students, faculty, and administrators separately. Three modules will be communicated mutually but not disrupt each other. The front end will be written in pure Java, while the backend database (SQLite) will also be simulated by files with a pre-defined formation (JSON). The connection between the backend, frontend, as well as all related operations, would also be written in pure Java. To ensure safety from [***SQL Injection***](https://en.wikipedia.org/wiki/SQL_injection), all insertions into the database are made through [***preparedStatement***](https://en.wikipedia.org/wiki/Prepared_statement). JavaFX and CSS will be used to create and decorate the GUI of this application. All public data about courses are originally collected from UCalgary websites (https://www.ucalgary.ca/pubs/calendar/current/course-desc-main.html).

## Structure

A web scraper which scrapes the University of Calgary Course Calendar and adds the courses to the COURSES.db
All of the modules will include the following features:

* Query of information of courses (such as title, course descriptions, prerequisites)
* Account management (editing personal information such as name, address, phone number, password)
* Keyword search capabilities through course offerings

In the student module, major features we decide to implement includes:

* Query of individual grades and status of the course (displayed in a table)
* Stats of all past courses and grades
* Courses enrollment
* Academic requirements, inlcuding requirements for internship

For the faculty module, major features are listed below:

* Management of courses teaching, including input of student's grades

For the administrators’ module, these main functions will be considered:

* User management, including adding, remove and change information of a user
* Curriculum management, including the addition and deletion of courses
* Internship management, including setting the requirement of the internship
* Management of academic requirements for all majors
* Keyword search capibilities through accounts
* History Log access of all interaction of the application by all users which can only be accessed by administrator

## Some scenarios when the app is used

* Administrators of this app can edit the database files and create a course then decide who are going to teach the course in an academic term. Meanwhile, a student can select the course from its panel if and only if all courses that the student is taking and has taken satisfy all pre-requisites, also the course the student plans to enroll does not violate any anti-requisites.
* At the end of an academic term, the faculty can grade the students course by course, and the feedback will be shown on the student's panel after grading.
* Administrators can check the history log in their panel to see when and what kind of user did what kind of operations. It would be helpful to avoid being attacked by hackers who are going to break the database since the IP of any user who attempts to login will be saved.
