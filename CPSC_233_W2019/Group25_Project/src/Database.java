import java.io.File;
import java.sql.SQLException;

/******************************************************************************
 * The {@code Database} class contains all the static methods that are needed to
 * use to connect to the SQLite3.
 *
 * The create statement of each database file:
 *
 * ACCOUNT_DATA.db:
 *
 * CREATE TABLE "USER" (
 * 	"UUID"	TEXT NOT NULL UNIQUE,
 * 	"ROLE"	TEXT NOT NULL DEFAULT 'ADMIN' CHECK(ROLE='ADMIN' OR ROLE='STUDENT' OR ROLE='FACULTY'),
 * 	"USERNAME"	TEXT NOT NULL UNIQUE,
 * 	"FULLNAME"	TEXT NOT NULL,
 * 	"GENDER"	TEXT NOT NULL CHECK(GENDER='M' OR GENDER='F'),
 * 	"DATE_OF_BIRTH"	TEXT NOT NULL,
 * 	"EMAIL"	TEXT NOT NULL,
 * 	"PHONE"	TEXT NOT NULL,
 * 	"ADDRESS"	TEXT NOT NULL,
 * 	"PASSWORD"	TEXT NOT NULL,
 * 	PRIMARY KEY("UUID")
 * )
 *
 * COURSES.db:
 * CREATE TABLE "COURSES" (
 * 	"COURSE_ID"	TEXT NOT NULL UNIQUE,
 * 	"COURSE_NAME"	TEXT NOT NULL,
 * 	"COURSE_DESCRIPTION"	TEXT NOT NULL,
 * 	"COURSE_UNITS"	TEXT NOT NULL,
 * 	"PREREQUISITES"	TEXT,
 * 	"ANTIREQUISITES"	TEXT,
 * 	"CAN_BE_REPEATED"	TEXT NOT NULL DEFAULT 'NO' CHECK(CAN_BE_REPEATED='YES' OR CAN_BE_REPEATED='NO'),
 * 	PRIMARY KEY("COURSE_ID")
 * )
 *
 * STUDENTS.db:
 * CREATE TABLE "STUDENTS" (
 * 	"UUID"	TEXT NOT NULL UNIQUE,
 * 	"TAKEN_COURSES"	TEXT NOT NULL,
 * 	"CURRENT_COURSES"	TEXT NOT NULL,
 * 	PRIMARY KEY("UUID")
 * )
 *
 * FACULTY.db:
 * CREATE TABLE "FACULTY" (
 * 	"UUID"	TEXT NOT NULL UNIQUE,
 * 	"COURSES_TEACHING"	TEXT NOT NULL,
 * 	PRIMARY KEY("UUID")
 * )
 *
 * ACADEMIC_REQUIREMENTS.db:
 * CREATE TABLE "MANDATORY_COURSES" (
 * 	"COURSE_ID"	TEXT NOT NULL UNIQUE,
 * 	PRIMARY KEY("COURSE_ID")
 * )
 * CREATE TABLE "OPTIONAL_COURSES" (
 * 	"COURSE_ID"	TEXT NOT NULL UNIQUE,
 * 	PRIMARY KEY("COURSE_ID")
 * )
 *
 * INTERNSHIP_REQUIREMENTS.db:
 * CREATE TABLE "MANDATORY_COURSES" (
 * 	"COURSE_ID"	TEXT NOT NULL UNIQUE,
 * 	PRIMARY KEY("COURSE_ID")
 * )
 * CREATE TABLE "OPTIONAL_COURSES" (
 * 	"COURSE_ID"	TEXT NOT NULL UNIQUE,
 * 	PRIMARY KEY("COURSE_ID")
 * )
 *
 * COURSES_ORIGINAL.db:
 * CREATE TABLE "COURSES" (
 * 	"COURSE ID"	TEXT NOT NULL UNIQUE,
 * 	"COURSE TITLE"	TEXT,
 * 	"COURSE DESCRIPTION"	TEXT,
 * 	"COURSE CREDITS"	TEXT,
 * 	"COURSE PREREQUISITES"	TEXT,
 * 	"COURSE ANTIREQUISITES"	TEXT,
 * 	"COURSE COREREQUISITES"	TEXT,
 * 	"COURSE CAN BE REPEATED FOR GPA"	TEXT,
 * 	PRIMARY KEY("COURSE ID")
 * )
 *
 * @author Group 25
 * @date 2019/04/08
 ******************************************************************************/

public class Database {

    /**
     * @Fields Database filepath
     */
    public static final  String                  ACCOUNT_DATA                  = getDatabaseFilePath() + "ACCOUNT_DATA.db";
    public static final  String                  COURSES                       = getDatabaseFilePath() + "COURSES.db";
    public static final  String                  ACADEMIC_REQUIREMENTS         = getDatabaseFilePath() + "ACADEMIC_REQUIREMENTS.db";
    public static final  String                  INTERNSHIP_REQUIREMENTS       = getDatabaseFilePath() + "INTERNSHIP_REQUIREMENTS.db";
    public static final  String                  STUDENTS                      = getDatabaseFilePath() + "STUDENTS.db";
    public static final  String                  FACULTY                       = getDatabaseFilePath() + "FACULTY.db";
    public static final  String                  COURSES_ORIGINAL              = getDatabaseFilePath() + "COURSES_ORIGINAL.db";
    /**
     * @Fields Containers that store all items import from database
     */
    public static final  HashMap<String, User>   accounts                      = new HashMap<>();
    public static final  HashMap<String, String> usernameToUuid                = new HashMap<>();
    public static final  HashMap<String, Course> courses                       = new HashMap<>();
    public static final  HashSet<String>         mandatoryCourses              = new HashSet<>();
    public static final  HashSet<String>         optionalCourses               = new HashSet<>();
    public static final  HashSet<String>         courseCurrentlyTaught         = new HashSet<>();
    public static final  HashSet<String>         mandatoryCoursesForInternship = new HashSet<>();
    public static final  HashSet<String>         optionalCoursesForInternship  = new HashSet<>();
    public static final  ArrayList<CourseForGUI> coursesForGUI                 = new ArrayList<>();
    public static final  ArrayList<UserForGUI>   accountsForGUI                = new ArrayList<>();
    private static final String                  CLASS_NAME                    = "org.sqlite.JDBC";
    private static final String                  PREFIX                        = "jdbc:sqlite:";

    /**
     * Getter the database file path.
     *
     * @return the file path.
     * @date 2019/04/11
     */
    private static String getDatabaseFilePath() {
        return new File(System.getProperty("user.dir")).getParent() + "/db/";
    }

    /**
     * Initializes the courses currently being taught.
     *
     * @date 2019/04/11
     */
    private static void initializeCoursesCurrentlyTaught() {
        courseCurrentlyTaught.clear();
        if (isValid(FACULTY)) {
            String select = "SELECT * FROM FACULTY;";
            try (Connection connection = createConnection(FACULTY, true);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(select)
            ) {
                while (resultSet.next()) {
                    String[] temp = resultSet.getString("COURSES_TEACHING").split(" ");
                    courseCurrentlyTaught.addAll(Arrays.asList(temp));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initializes internship requirements.
     *
     * @date 2019/04/11
     */
    private static void initializeInternshipRequirements() {
        mandatoryCoursesForInternship.clear();
        optionalCoursesForInternship.clear();
        if (isValid(INTERNSHIP_REQUIREMENTS)) {
            String select;
            select = "SELECT * FROM OPTIONAL_COURSES;";
            try (Connection connection = createConnection(INTERNSHIP_REQUIREMENTS, true);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(select)
            ) {
                while (resultSet.next()) {
                    optionalCoursesForInternship.add(resultSet.getString("COURSE_ID"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            select = "SELECT * FROM MANDATORY_COURSES;";
            try (Connection connection = createConnection(INTERNSHIP_REQUIREMENTS, true);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(select)
            ) {
                while (resultSet.next()) {
                    mandatoryCoursesForInternship.add(resultSet.getString("COURSE_ID"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Creates the connection to database
     *
     * @param dataBaseFilePath the filepath of the database
     * @param readOnly         set the property if the database can be read only during the action
     * @return a Connection object linking to dataBaseFilePath
     * @throws ClassNotFoundException an exception if the class is not found
     * @throws SQLException           an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    public static Connection createConnection(String dataBaseFilePath, boolean readOnly) throws ClassNotFoundException,
            SQLException {
        Class.forName(CLASS_NAME);
        if (readOnly) {
            Properties temp = new Properties();
            temp.setProperty("open_mode", "1");
            return DriverManager.getConnection(PREFIX + dataBaseFilePath, temp);
        }
        return DriverManager.getConnection(PREFIX + dataBaseFilePath);
    }

    /**
     * Tests if the database can be correctly found and open.
     *
     * @param dataBaseFilePath a String containing the database file path.
     * @return a boolean of whether connection is valid.
     * @date 2019/04/11
     */
    public static boolean isValid(String dataBaseFilePath) {

        boolean result = false;
        File    file   = new File(dataBaseFilePath);
        if (file.exists() && !file.isDirectory()) {
            try (Connection connection = createConnection(dataBaseFilePath, true)) {
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * A helper function takes a sql connection, a username and a password to generate a query prepared statement
     * for login.
     *
     * @param connection a Connection object
     * @param username   a string represents the username
     * @param password   a string represents the password
     * @return a PreparedStatement with username and password
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement userAuthenticationHelperFunc(Connection connection, String username,
                                                                  String password) throws SQLException {
        String            sql               = "SELECT * FROM USER WHERE USERNAME = ? AND PASSWORD = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        return preparedStatement;
    }

    /**
     * Authenticates if the username and the password match, if it fails, a null value will be return, otherwise the
     * corresponding user object will be initialized and returned.
     *
     * @param username a String containing the username.
     * @param password a String containing the password.
     * @return the user object or null if the username and password did not match
     * @date 2019/04/11
     */
    public static User userAuthentication(String username, String password) {

        if (!isValid(ACCOUNT_DATA)) {
            return null;
        }

        User user = null;
        try (Connection connection = createConnection(ACCOUNT_DATA, true);
             PreparedStatement statement = userAuthenticationHelperFunc(connection, username, password);
             ResultSet resultSet = statement.executeQuery()
        ) {
            if (resultSet.next()) {
                String role = resultSet.getString("ROLE");
                switch (role) {
                    case "FACULTY":
                        user = new Faculty();
                        break;
                    case "ADMIN":
                        user = new Admin();
                        break;
                    default:
                        user = new Student();
                        break;
                }
                user.setUsername(resultSet.getString("USERNAME"));
                user.setUuid(resultSet.getString("UUID"));
                user.setFullName(resultSet.getString("FULLNAME"));
                user.setGender(resultSet.getString("GENDER"));
                user.setEmailAddress(resultSet.getString("EMAIL"));
                user.setDateOfBirth(new DateOfBirth(resultSet.getString("DATE_OF_BIRTH")));
                user.setPhoneNumber(resultSet.getString("PHONE"));
                user.setAddress(resultSet.getString("ADDRESS"));
                user.setPassword(resultSet.getString("PASSWORD"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Imports all accounts from ACCOUNT_DATA to accounts and usernameToUuid.
     * The ACCOUNT_DATA must e valid.
     *
     * @date 2019/04/11
     */
    public static void initializeAccounts() {
        accounts.clear();
        if (isValid(ACCOUNT_DATA)) {
            String select = "SELECT * FROM USER;";
            try (Connection connection = createConnection(ACCOUNT_DATA, true);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(select)
            ) {
                String role;
                while (resultSet.next()) {
                    role = resultSet.getString("ROLE");
                    User user;
                    switch (role) {
                        case "ADMIN":
                            user = new Admin();
                            break;
                        case "FACULTY":
                            user = new Faculty();
                            break;
                        default:
                            user = new Student();
                            break;
                    }
                    user.setFullName(resultSet.getString("FULLNAME"));
                    user.setGender(resultSet.getString("GENDER"));
                    user.setUsername(resultSet.getString("USERNAME"));
                    user.setUuid(resultSet.getString("UUID"));
                    user.setDateOfBirth(new DateOfBirth(resultSet.getString("DATE_OF_BIRTH")));
                    user.setAddress(resultSet.getString("ADDRESS"));
                    user.setPassword(resultSet.getString("PASSWORD"));
                    user.setEmailAddress(resultSet.getString("EMAIL"));
                    user.setPhoneNumber(resultSet.getString("PHONE"));

                    accounts.put(user.getUuid(), user);
                    usernameToUuid.put(user.getUsername(), user.getUuid());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Import all courses from COURSES to courses.
     *
     * @date 2019/04/11
     */
    private static void initializeCourses() {
        courses.clear();
        if (isValid(COURSES)) {
            String select;
            // initialize courses
            select = "SELECT * FROM COURSES;";
            try (Connection connection = createConnection(COURSES, true);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(select)
            ) {
                while (resultSet.next()) {

                    Course course = new Course();
                    course.setCourseId(resultSet.getString("COURSE_ID"));
                    course.setCourseName(resultSet.getString("COURSE_NAME"));
                    course.setCourseName(resultSet.getString("COURSE_NAME"));
                    course.setCourseDescription(resultSet.getString("COURSE_DESCRIPTION"));
                    course.setCourseUnits(resultSet.getString("COURSE_UNITS"));
                    course.setPrerequisites(Course.parser(resultSet.getString("PREREQUISITES")));
                    course.setAntirequisites(Course.parser(resultSet.getString("ANTIREQUISITES")));
                    course.setCanBeRepeated(resultSet.getString("CAN_BE_REPEATED"));
                    courses.put(course.getCourseId(), course);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // calculate the number of students of each course and update to the corresponding Course object.
            select = "SELECT * FROM STUDENTS;";
            String uuid;
            try (Connection connection = createConnection(STUDENTS, true);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(select)
            ) {
                while (resultSet.next()) {
                    ArrayList<String> currentCourses = parseCurrentCourses(resultSet.getString("CURRENT_COURSES"));
                    uuid = resultSet.getString("UUID");
                    for (String i : currentCourses) {
                        courses.get(i).addStudentsWhoAreTaking(uuid);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initialize elements in coursesForGUI by wrapping Course objects.
     *
     * @date 2019/04/11
     */
    private static void initializeCoursesForGUI() {
        coursesForGUI.clear();
        for (Course i : courses.values()) {
            coursesForGUI.add(new CourseForGUI(i));
        }
        Collections.sort(coursesForGUI);
    }

    /**
     * Import all academic requirements from ACADEMIC_REQUIREMENTS and categorize into mandatoryCourses and
     * optionalCourses.
     *
     * @date 2019/04/11
     */
    private static void initializeAcademicRequirements() {
        String select = "SELECT * FROM MANDATORY_COURSES;";
        try (Connection connection = createConnection(ACADEMIC_REQUIREMENTS, true);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(select)
        ) {
            while (resultSet.next()) {
                mandatoryCourses.add(resultSet.getString("COURSE_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        select = "SELECT * FROM OPTIONAL_COURSES;";
        try (Connection connection = createConnection(ACADEMIC_REQUIREMENTS, true);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(select)
        ) {
            while (resultSet.next()) {
                optionalCourses.add(resultSet.getString("COURSE_ID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Initializes information of all faculty.
     *
     * @date 2019/04/11
     */
    private static void initializeAllFacultyInformation() {
        for (User i : Database.accounts.values()) {
            if (i.getRole().equals(Constants.ROLE[1])) {
                initializeFacultyInformation(i.getUuid());
            }
        }
    }

    /**
     * Initializes information of all students.
     *
     * @date 2019/04/11
     */
    private static void initializeAllStudentsInformation() {
        for (User i : Database.accounts.values()) {
            if (i.getRole().equals(Constants.ROLE[2])) {
                initializeStudentInformation(i.getUuid());
            }
        }
    }

    /**
     * A helper function takes a sql connection, a uuid of student to generate a query prepared statement
     * for initialization of the student's information
     *
     * @param connection a Connection object
     * @param uuid       a uuid of the student
     * @return a PreparedStatement with uuid
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement initializeStudentInformationHelperFunc(Connection connection,
                                                                            String uuid)
            throws SQLException {
        String            sql               = "SELECT * FROM STUDENTS WHERE UUID = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uuid);
        return preparedStatement;
    }

    /**
     * Initializes student information.
     *
     * @param uuid a String contain the uuid of the student.
     * @date 2019/04/11
     */
    private static void initializeStudentInformation(String uuid) {

        // precondition : Database.account.get(uuid) must exist and must be a student
        // add takenCoursesList and currentCoursesList to Database.account.get(uuid)
        try (Connection connection = createConnection(STUDENTS, true);
             PreparedStatement statement = initializeStudentInformationHelperFunc(connection, uuid);
             ResultSet resultSet = statement.executeQuery()
        ) {
            Student student = (Student) Database.accounts.get(uuid);
            // get the courseAndGrade array list
            ArrayList<CourseAndGrade> takenCourses = parseTakenCourses(resultSet.getString("TAKEN_COURSES"));
            // import to Database.account.get(uuid)
            for (CourseAndGrade i : takenCourses) {
                student.addTakenCourses(i);
            }
            // get the currentCourses array list
            ArrayList<String> currentCourses = parseCurrentCourses(resultSet.getString("CURRENT_COURSES"));
            // import to Database.account.get(uuid)
            for (String i : currentCourses) {
                student.addCurrentCourses(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * A helper function takes a sql connection, a uuid of faculty to generate a query prepared statement
     * for initialization of the faculty's information
     *
     * @param connection a Connection object
     * @param uuid       the uuid of the faculty
     * @return a PreparedStatement with uuid
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement initializeFacultyInformationHelperFunc(Connection connection, String uuid)
            throws SQLException {
        String            sql               = "SELECT * FROM FACULTY WHERE UUID = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uuid);
        return preparedStatement;
    }

    /**
     * Import the corresponding faculty information from FACULTY by the uuid, the precondition is Database.account.get
     * (uuid) must exist and must be a faculty
     *
     * @param uuid the uuid of the faculty
     * @date 2019/04/11
     */
    public static void initializeFacultyInformation(String uuid) {

        // add coursesId to Database.account.get(uuid).coursesTeaching
        try (Connection connection = createConnection(FACULTY, true);
             PreparedStatement statement = initializeFacultyInformationHelperFunc(connection, uuid);
             ResultSet resultSet = statement.executeQuery()
        ) {
            Faculty faculty = (Faculty) Database.accounts.get(uuid);
            // get the courseAndGrade array list
            ArrayList<String> coursesTeaching = parseCurrentCourses(resultSet.getString("COURSES_TEACHING"));
            // import to Database.account.get(uuid)
            for (String i : coursesTeaching) {
                if (!i.isEmpty()) {
                    faculty.addCoursesTeaching(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * The method parses taken courses and returns an arrayList that contains all of them.
     *
     * @param s the string of course Ids.
     * @return an arrayList containing the results.
     * @date 2019/04/11
     */
    private static ArrayList<CourseAndGrade> parseTakenCourses(String s) {
        ArrayList<CourseAndGrade> result = new ArrayList<>();
        if (s != null && !s.trim().isEmpty()) {
            String[] temp = s.split(" ");
            for (int i = 0; i < temp.length; i += 2) {
                result.add(new CourseAndGrade(temp[i], temp[i + 1]));
            }
        }
        return result;
    }

    /**
     * The method parses a string of courses and taking into an arrayList.
     *
     * @param s a string of course Ids.
     * @return an arrayList containing the results.
     * @date 2019/04/11
     */
    private static ArrayList<String> parseCurrentCourses(String s) {
        ArrayList<String> result = new ArrayList<>();
        if (s != null && !s.trim().isEmpty()) {
            String[] temp = s.split(" ");
            result.addAll(Arrays.asList(temp));
        }
        return result;
    }

    /**
     * A helper function takes a sql connection, a user object to generate a 'CREATE' prepared statement
     * for inserting a new item.
     *
     * @param connection a Connection object
     * @param user       a User object
     * @return a PreparedStatement with username and password
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement createAccountCommitHelperFunc(Connection connection,
                                                                   User user)
            throws SQLException {

        String            sql               = "INSERT INTO USER VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUuid());
        preparedStatement.setString(2, user.getRole());
        preparedStatement.setString(3, user.getUsername());
        preparedStatement.setString(4, user.getFullName());
        preparedStatement.setString(5, user.getGender());
        preparedStatement.setString(6, user.getDateOfBirth().toString());
        preparedStatement.setString(7, user.getEmailAddress());
        preparedStatement.setString(8, user.getPhoneNumber());
        preparedStatement.setString(9, user.getAddress());
        preparedStatement.setString(10, user.getPassword());
        return preparedStatement;
    }

    /**
     * Commit a new account created by the admin to database
     *
     * @param user the user of the account that is committed.
     * @date 2019/04/11
     */
    public static void createAccountCommit(User user) {

        try (Connection connection = createConnection(ACCOUNT_DATA, false);
             PreparedStatement statement = createAccountCommitHelperFunc(connection, user)
        ) {
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if the user is a student, update STUDENT.db as well
        if (user.getRole().equals(Constants.ROLE[2])) {
            createStudentCommit(user.getUuid());
        }

        // if the user is a faculty, update FACULTY.db as well
        else if (user.getRole().equals(Constants.ROLE[1])) {
            createFacultyCommit(user.getUuid());
        }
    }

    /**
     * A helper function takes a sql connection, a uuid of faculty to generate a prepared statement
     * for creating a new item.
     *
     * @param connection a Connection object
     * @param uuid       the uuid of the faculty
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement createFacultyCommitHelperFunc(Connection connection, String uuid)
            throws SQLException {

        String            sql               = "INSERT INTO FACULTY VALUES (?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uuid);
        preparedStatement.setString(2, "");
        return preparedStatement;
    }

    /**
     * Creates a new item with uuid only in FACULTY.db
     *
     * @param uuid the uuid of the faculty
     * @date 2019/04/11
     */
    private static void createFacultyCommit(String uuid) {

        try (Connection connection = createConnection(FACULTY, false);
             PreparedStatement statement = createFacultyCommitHelperFunc(connection, uuid)
        ) {
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * A helper function takes a sql connection, a uuid of student to generate a prepared statement
     * for creating a new item.
     *
     * @param connection a Connection object
     * @param uuid       the uuid of the student
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement createStudentCommitHelperFunc(Connection connection, String uuid)
            throws SQLException {

        String            sql               = "INSERT INTO STUDENTS VALUES (?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uuid);
        preparedStatement.setString(2, "");
        preparedStatement.setString(3, "");
        return preparedStatement;
    }

    /**
     * Creates a new item with uuid only in STUDENT.db
     *
     * @param uuid a string contains the uuid of the student
     * @date 2019/04/11
     */
    private static void createStudentCommit(String uuid) {
        try (Connection connection = createConnection(STUDENTS, false);
             PreparedStatement statement = createStudentCommitHelperFunc(connection, uuid)
        ) {
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper function takes a sql connection, a course object to generate a prepared statement
     * for creating a new course.
     *
     * @param connection a Connection object
     * @param course     the Course object
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement createCourseCommitHelperFunc(Connection connection, Course course)
            throws SQLException {

        String            sql               = "INSERT INTO COURSES VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, course.getCourseId());
        preparedStatement.setString(2, course.getCourseName());
        preparedStatement.setString(3, course.getCourseDescription());
        preparedStatement.setString(4, course.getCourseUnits());
        preparedStatement.setString(5, course.prerequisitesToString());
        preparedStatement.setString(6, course.antirequisitesToString());
        preparedStatement.setString(7, course.getCanBeRepeated());
        return preparedStatement;
    }

    /**
     * Commit a new course created by the admin to database
     *
     * @param course the course to be committed
     * @date 2019/04/11
     */
    public static void createCourseCommit(Course course) {
        try (Connection connection = createConnection(COURSES, false);
             PreparedStatement statement = createCourseCommitHelperFunc(connection, course)
        ) {
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * A helper function takes a sql connection, a course object to generate a prepared statement
     * for creating a new course.
     *
     * @param connection an exception if any error occurs during the SQL operations
     * @param uuid       the uuid of the account
     * @param userField  the property of the user that is changed
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement accountUpdateCommitHelperFunc(Connection connection, String uuid,
                                                                   Constants.USER_FIELD userField)
            throws SQLException {

        String            update;
        PreparedStatement preparedStatement = null;
        switch (userField) {
            case USERNAME:
                update = "UPDATE USER SET USERNAME = ? WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, accounts.get(uuid).getUsername());
                preparedStatement.setString(2, uuid);
                break;
            case FULL_NAME:
                update = "UPDATE USER SET FULLNAME = ? WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, accounts.get(uuid).getFullName());
                preparedStatement.setString(2, uuid);
                break;
            case ROLE:
                update = "UPDATE USER SET ROLE = ? WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, accounts.get(uuid).getRole());
                preparedStatement.setString(2, uuid);
                break;
            case GENDER:
                update = "UPDATE USER SET GENDER = ? WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, accounts.get(uuid).getGender());
                preparedStatement.setString(2, uuid);
                break;
            case DATE_OF_BIRTH:
                update = "UPDATE USER SET DATE_OF_BIRTH = ? WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, accounts.get(uuid).getDateOfBirth().toString());
                preparedStatement.setString(2, uuid);
                break;
            case EMAIL:
                update = "UPDATE USER SET EMAIL = ? WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, accounts.get(uuid).getEmailAddress());
                preparedStatement.setString(2, uuid);
                break;
            case PHONE:
                update = "UPDATE USER SET PHONE = ? WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, accounts.get(uuid).getPhoneNumber());
                preparedStatement.setString(2, uuid);
                break;
            case ADDRESS:
                update = "UPDATE USER SET ADDRESS = ? WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, accounts.get(uuid).getAddress());
                preparedStatement.setString(2, uuid);
                break;
            case PASSWORD:
                update = "UPDATE USER SET PASSWORD = ? WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, accounts.get(uuid).getPassword());
                preparedStatement.setString(2, uuid);
                break;
            default:
                break;
        }
        return preparedStatement;
    }

    /**
     * Commit a change of a property of a user to database
     *
     * @param uuid      the uuid of the account
     * @param userField the property of user that is changed
     * @date 2019/04/11
     */
    public static void accountUpdateCommit(String uuid, Constants.USER_FIELD userField) {
        try (Connection connection = createConnection(ACCOUNT_DATA, false);
             PreparedStatement statement = accountUpdateCommitHelperFunc(connection, uuid, userField)
        ) {
            if (statement != null) {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper function takes a sql connection, a course id and a courseField to generate a prepared statement
     * for updating the course
     *
     * @param connection  a Connection object
     * @param courseId    the course id
     * @param courseField the property of the course that is changed
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement courseUpdateCommitHelperFunc(Connection connection,
                                                                  String courseId,
                                                                  Constants.COURSE_FIELD courseField)
            throws SQLException {

        String            update;
        PreparedStatement preparedStatement = null;
        String            temp;
        switch (courseField) {
            case COURSE_NAME:
                update = "UPDATE COURSES SET COURSE_NAME = ? WHERE COURSE_ID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, courses.get(courseId).getCourseName());
                preparedStatement.setString(2, courseId);
                break;
            case COURSE_DESCRIPTION:
                update = "UPDATE COURSES SET COURSE_DESCRIPTION = ? WHERE COURSE_ID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, courses.get(courseId).getCourseDescription());
                preparedStatement.setString(2, courseId);
                break;
            case COURSE_UNITS:
                update = "UPDATE COURSES SET COURSE_UNITS = ? WHERE COURSE_ID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, courses.get(courseId).getCourseUnits());
                preparedStatement.setString(2, courseId);
                break;
            case PREREQUISITES:
                update = "UPDATE COURSES SET PREREQUISITES = ? WHERE COURSE_ID = ?";
                preparedStatement = connection.prepareStatement(update);

                temp = courses.get(courseId).prerequisitesToString();
                if (temp.isEmpty()) {
                    preparedStatement.setString(1, null);
                } else {
                    preparedStatement.setString(1, temp);
                }
                preparedStatement.setString(2, courseId);

                break;
            case ANTIREQUISITES:
                update = "UPDATE COURSES SET ANTIREQUISITES = ? WHERE COURSE_ID = ?";
                preparedStatement = connection.prepareStatement(update);

                temp = courses.get(courseId).antirequisitesToString();
                if (temp.isEmpty()) {
                    preparedStatement.setString(1, null);
                } else {
                    preparedStatement.setString(1, temp);
                }
                preparedStatement.setString(2, courseId);

                break;
            case CAN_BE_REPEATED:
                update = "UPDATE COURSES SET CAN_BE_REPEATED = ? WHERE COURSE_ID = ?";
                preparedStatement = connection.prepareStatement(update);
                preparedStatement.setString(1, courses.get(courseId).getCanBeRepeated());
                preparedStatement.setString(2, courseId);
                break;
            default:
                break;
        }
        return preparedStatement;
    }

    /**
     * Commit a change of a property of a course to database
     *
     * @param courseId    the course id
     * @param courseField the property of the course that is changed
     * @date 2019/04/11
     */
    public static void courseUpdateCommit(String courseId, Constants.COURSE_FIELD courseField) {
        try (Connection connection = createConnection(COURSES, false);
             PreparedStatement statement = courseUpdateCommitHelperFunc(connection, courseId, courseField)
        ) {
            if (statement != null) {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper function takes a sql connection, a course object and a boolean to generate a prepared statement
     * for creating a new course.
     *
     * @param connection  a Connection object
     * @param courseId    the course id
     * @param isMandatory a boolean shows that if the course is a mandatory of the requirements
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement createCourseCommitToRequirementsHelperFunc(Connection connection, String courseId,
                                                                                boolean isMandatory)
            throws SQLException {

        String            sql;
        PreparedStatement preparedStatement;
        if (isMandatory) {
            sql = "INSERT INTO MANDATORY_COURSES VALUES (?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, courseId);
        } else {
            sql = "INSERT INTO OPTIONAL_COURSES VALUES (?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, courseId);
        }
        return preparedStatement;
    }

    /**
     * A function takes a course object and insert to ACADEMIC_REQUIREMENTS.db or INTERNSHIP_REQUIREMENTS.db
     *
     * @param courseId               the course id
     * @param isAcademicRequirements a boolean shows that if the course is a part of academic requirement
     * @param isMandatory            a boolean shows that if the course is a mandatory of the requirements
     * @date 2019/04/11
     */
    public static void createCourseCommitToRequirements(String courseId, boolean isAcademicRequirements, boolean isMandatory) {
        String filepath;
        if (isAcademicRequirements) {
            filepath = ACADEMIC_REQUIREMENTS;
        } else {
            filepath = INTERNSHIP_REQUIREMENTS;
        }
        try (Connection connection = createConnection(filepath, false);
             PreparedStatement statement = createCourseCommitToRequirementsHelperFunc(connection,
                     courseId, isMandatory)
        ) {
            if (statement != null) {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper function takes a sql connection, a course object to generate a prepared statement
     * for removing a course.
     *
     * @param connection  a Connection object
     * @param courseId    the course id
     * @param isMandatory a boolean shows that if the course is a mandatory of the requirements
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement createPreparedStatementForCourseRemovalCommitToRequirements(
            Connection connection
            , String courseId, boolean isMandatory)
            throws SQLException {
        String            sql;
        PreparedStatement preparedStatement;
        if (isMandatory) {
            sql = "DELETE FROM MANDATORY_COURSES WHERE COURSE_ID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, courseId);
        } else {
            sql = "DELETE FROM OPTIONAL_COURSES WHERE COURSE_ID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, courseId);
        }
        return preparedStatement;
    }

    /**
     * The method removes the course from ACADEMIC_REQUIREMENTS.db or INTERNSHIP_REQUIREMENTS.db depending the
     * arguments passed.
     *
     * @param courseId               the course id
     * @param isAcademicRequirements a boolean shows that if the course is a part of academic requirement
     * @param isMandatory            a boolean shows that if the course is a mandatory of the requirements
     * @date 2019/04/11
     */
    public static void courseRemovalCommitToRequirements(String courseId, boolean isAcademicRequirements, boolean isMandatory) {
        String filepath;
        if (!isAcademicRequirements) {
            filepath = INTERNSHIP_REQUIREMENTS;
        } else {
            filepath = ACADEMIC_REQUIREMENTS;
        }

        try (Connection connection = createConnection(filepath, false);
             PreparedStatement statement = createPreparedStatementForCourseRemovalCommitToRequirements(connection,
                     courseId, isMandatory)
        ) {
            if (statement != null) {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper function takes a sql connection, a course object to generate a prepared statement
     * for removing a course.
     *
     * @param connection a Connection object
     * @param uuid       a uuid of the account
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement createPreparedStatementForAccountRemovalCommit(
            Connection connection
            , String tableName, String uuid)
            throws SQLException {
        String            sql;
        PreparedStatement preparedStatement = null;

        switch (tableName) {
            case "USER":
                sql = "DELETE FROM USER WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, uuid);
                break;
            case "STUDENTS":
                sql = "DELETE FROM STUDENTS WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, uuid);
                break;
            case "FACULTY":
                sql = "DELETE FROM FACULTY WHERE UUID = ?";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, uuid);
                break;
            default:
                break;
        }
        return preparedStatement;
    }

    /**
     * The method removes an account from the ACCOUNT_DATA.db, if the account is a student, removes it from STUDENT.db
     * as well, if the account is a faculty, removes it from FACULTY.db as well.
     *
     * @param uuid a string contains the uuid of the account.
     * @date 2019/04/11
     */
    public static void accountRemovalCommit(String uuid) {

        try (Connection connection = createConnection(ACCOUNT_DATA, false);
             PreparedStatement statement = createPreparedStatementForAccountRemovalCommit(connection, "USER", uuid)
        ) {
            if (statement != null) {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // update STUDENT.db as well
        try (Connection connection = createConnection(STUDENTS, false);
             PreparedStatement statement = createPreparedStatementForAccountRemovalCommit(connection, "STUDENTS", uuid)
        ) {
            if (statement != null) {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // update FACULTY.db as well
        try (Connection connection = createConnection(FACULTY, false);
             PreparedStatement statement = createPreparedStatementForAccountRemovalCommit(connection, "FACULTY", uuid)
        ) {
            if (statement != null) {
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper function takes a sql connection, a course id to generate a prepared statement
     * for removing a course.
     *
     * @param connection a Connection object
     * @param courseId   the course id
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement createPreparedStatementForCourseRemovalCommit(Connection connection,
                                                                                   String courseId)
            throws SQLException {
        String            sql               = "DELETE FROM COURSES WHERE COURSE_ID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, courseId);
        return preparedStatement;
    }

    /**
     * The method removes a course in COURSES.db.
     *
     * @param courseId the course id
     * @date 2019/04/11
     */
    public static void courseRemovalCommit(String courseId) {
        try (Connection connection = createConnection(COURSES, false);
             PreparedStatement statement = createPreparedStatementForCourseRemovalCommit(connection, courseId)
        ) {
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper function takes a sql connection, uuid of student and a string to generate a prepared statement
     * for updating the value of TAKEN_COURSES
     *
     * @param connection   a Connection object
     * @param uuid         the uuid of the student
     * @param takenCourses the courses the student taken
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement createPreparedStatementForUpdateStudentTakenCourses(Connection connection,
                                                                                         String uuid, String takenCourses)
            throws SQLException {
        String            sql               = "UPDATE STUDENTS SET TAKEN_COURSES = ? WHERE UUID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, takenCourses);
        preparedStatement.setString(2, uuid);
        return preparedStatement;
    }

    /**
     * Update TAKEN_COURSES of a student in STUDENT.db, it will be used when a faculty is grading.
     *
     * @param uuid         a String contains the uuid of the student.
     * @param takenCourses a String contains the course the student has taken.
     * @date 2019/04/11
     */
    public static void updateStudentTakenCourses(String uuid, String takenCourses) {

        try (Connection connection = createConnection(STUDENTS, false);
             PreparedStatement statement = createPreparedStatementForUpdateStudentTakenCourses(connection, uuid, takenCourses)
        ) {
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A helper function takes a sql connection, uuid of student and a string to generate a prepared statement
     * for updating the value of CURRENT_COURSES
     *
     * @param connection     a Connection object
     * @param uuid           the uuid of the student
     * @param currentCourses a string of course Ids that the student is currently taking
     * @return a PreparedStatement
     * @throws SQLException an exception if any error occurs during the SQL operations
     * @date 2019/04/11
     */
    private static PreparedStatement createPreparedStatementForUpdateStudentCurrentCourses(Connection connection,
                                                                                           String uuid, String currentCourses)
            throws SQLException {
        String            sql               = "UPDATE STUDENTS SET CURRENT_COURSES = ? WHERE UUID = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, currentCourses);
        preparedStatement.setString(2, uuid);
        return preparedStatement;
    }

    /**
     * The method updates the courses that the student is currently taking, it will be used when a faculty is grading.
     *
     * @param uuid           a String containing the uuid of the student.
     * @param currentCourses a String containing the courses that the student is currently taking.
     * @date 2019/04/11
     */
    public static void updateStudentCurrentCourses(String uuid, String currentCourses) {
        try (Connection connection = createConnection(STUDENTS, false);
             PreparedStatement statement = createPreparedStatementForUpdateStudentCurrentCourses(connection, uuid, currentCourses)
        ) {
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the accounts for GUI.
     *
     * @date 2019/04/11
     */
    private static void initializeAccountsForGUI() {
        accountsForGUI.clear();
        for (User i : accounts.values()) {
            accountsForGUI.add(new UserForGUI(i));
        }
    }

    /**
     * The method releases all resources we stored in data structures and enforce a garbage collection
     *
     * @date 2019/04/11
     */
    public static void releaseAllResources() {
        accounts.clear();
        accountsForGUI.clear();
        courses.clear();
        coursesForGUI.clear();
        optionalCourses.clear();
        optionalCoursesForInternship.clear();
        mandatoryCourses.clear();
        mandatoryCoursesForInternship.clear();
        usernameToUuid.clear();
        System.gc();
    }

    /**
     * Initialization of all public information
     *
     * @date 2019/04/11
     */
    public static void initialization() {
        initializeAccounts();
        initializeAccountsForGUI();
        initializeCourses();
        initializeCoursesCurrentlyTaught();
        initializeCoursesForGUI();
        initializeAcademicRequirements();
        initializeInternshipRequirements();
        initializeAllStudentsInformation();
        initializeAllFacultyInformation();
    }
}
