This readme file has been adapted from the readme file template from PurpleBooth: https://gist.github.com/PurpleBooth/109311bb0361f32d87a2

# Student / Faculty Information Manager

### Important updates in Demo2

1. Part of individual components
2. Keyword highlighting in the function of searching accounts and searching courses
3. Users can search courses by keywords of its Id and name now
4. Faculty

### Remaining logic to implements
1. Administrators can create a course schedule and assign a course id and a faculty
2. Students can drop and enroll courses.
3. Faculty can grade students.
4. A history to log every critical behaviors, which could be check by administrators.

This is a form based application which allows students, faculty and admin of an university institution to create and mannage accounts. Functionality for students students, adding courses, dropping courses, checking GPA, checking courses already taken and their GPA, and change personal information. For faculty, users will be able to access courses they are currently teaching, look at students enrolled in the courses, change and add grades, as well as change their own perso nal information. Finally the admin users will be able to look at all users enrolled in the institutions, change personal information in other users, delete accounts, add courses, edit courses, delete courses, as well as edit their own personal information.

### Prerequisites

To run this program you will need:
1. An updated version of JDK (>= 1.8.0_161)
2. An updated version of the repository

### Instructions of setup

Execute the commands as below in a Linux terminal:

```
~$ git clone https://github.com/alextenney/Group25_Project.git
~$ cd ./Group25_Project/Project/Demo2/
~$ bash init.sh
```

If **init.sh** has no permissions, you can adjust by typing this before execution:

```
~$ chmod +x init.sh
```
## Testing environment

We have tested on:

Linux csx.cs.ucalgary.ca 4.17.4-200.fc28.x86_64 GNU/Linux<br/>
Fedora release 28 (Twenty Eight)

## Versions

Java "1.8.0_161"<br/>
Java(TM) SE Runtime Environment (build 1.8.0_161-b12)<br/>
Java HotSpot(TM) 64-Bit Server VM (build 25.161-b12, mixed mode)<br/>
sqlite 3.22.0<br/>
sqlite-jdbc-3.23.1 from https://github.com/xerial/sqlite-jdbc

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

* **Haohu Shen**
* **Alexandra Tenney**
* **Dwight Pittman**
* **Lan Jin**

See also the list of [contributors](https://github.com/alextenney/Group25_Project/graphs/contributors) who participated in this project

