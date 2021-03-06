import java.io.File;
import java.sql.SQLException;

/**
 * The class stores all functions we need to use to connect to the sqlite3.
 */

public class Database {

    private static final String CLASS_NAME = "org.sqlite.JDBC";
    private static final String PREFIX     = "jdbc:sqlite:";

    /**
     * Database filepath
     */
    public static String ACCOUNT_DATA            = getDatabaseFilePath(Constants.TEST_IN_IDE) + "ACCOUNT_DATA.db";
    public static String COURSES                 = getDatabaseFilePath(Constants.TEST_IN_IDE) + "COURSES.db";
    public static String ACADEMIC_REQUIREMENTS   = getDatabaseFilePath(Constants.TEST_IN_IDE) + "ACADEMIC_REQUIREMENTS.db";
    public static String INTERNSHIP_REQUIREMENTS = getDatabaseFilePath(Constants.TEST_IN_IDE) + "INTERNSHIP_REQUIREMENTS.db";
    public static String STUDENTS                = getDatabaseFilePath(Constants.TEST_IN_IDE) + "STUDENTS.db";
    public static String FACULTY                 = getDatabaseFilePath(Constants.TEST_IN_IDE) + "FACULTY.db";
    /**
     * Store all items import from database
     */
    public static HashMap<String, User>   accounts               = new HashMap<>();
    public static HashMap<String, String> usernameToUuid         = new HashMap<>();
    public static HashMap<String, Course> courses                = new HashMap<>();
    public static HashSet<String>         mandatoryCourses       = new HashSet<>();
    public static HashSet<String>         optionalCourses        = new HashSet<>();
    public static HashSet<String>         coursesCurrentlyTaught = new HashSet<>();
    public static HashSet<String> mandatoryCoursesForInternship = new HashSet<>();
    public static HashSet<String> optionalCoursesForInternship  = new HashSet<>();

    /**
     * @param testInIDE
     * @return
     */
    private static String getDatabaseFilePath(boolean testInIDE) {
        if (testInIDE) {
            return "/Users/hshen/IdeaProjects/Project/db/";
        }
        return new File(System.getProperty("user.dir")).getParent() + "/db/";
    }

    public static void initializeCoursesCurrentlyTaught() {

        coursesCurrentlyTaught.clear();

        if (isValid(FACULTY)) {

            String select = "SELECT * FROM FACULTY;";
            try (Connection connection = createConnection(FACULTY, true);
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(select)
            ) {
                while (resultSet.next()) {
                    String[] temp = resultSet.getString("COURSES_TEACHING").split(" ");
                    for (String i : temp) {
                        coursesCurrentlyTaught.add(i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void initializeInternshipRequirements() {

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
     * a function to create a connection to database
     *
     * @param dataBaseFilePath
     * @param readOnly
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
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
     * a function to test if the database can be correctly found and open
     *
     * @param dataBaseFilePath
     * @return
     */
    // Using try-with-resources to make sure that the resources could be closed before the catch statement
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
     * Authenticate if the username and the password match, if it fails, a null value will be return, otherwise the
     * corresponding user object will be initialzed and returned.
     *
     * @param username
     * @param password
     * @return
     */
    public static User userAuthentication(String username, String password) {

        User user = null;
        if (!isValid(ACCOUNT_DATA)) {
            return user;
        }

        String select = "SELECT * FROM USER WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "';";
        try (Connection connection = createConnection(ACCOUNT_DATA, true);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(select)
        ) {
            if (resultSet.next()) {
                String role = resultSet.getString("ROLE");
                switch (role) {
                    case "ADMIN":
                        user = new Admin();
                        break;
                    case "FACULTY":
                        user = new Faculty();
                        break;
                    default:
                        user = new Student();
                }
                user.setUuid(resultSet.getString("UUID"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setFullName(resultSet.getString("FULLNAME"));
                user.setGender(resultSet.getString("GENDER"));
                user.setDateOfBirth(new DateOfBirth(resultSet.getString("DATE_OF_BIRTH")));
                user.setEmailAddress(resultSet.getString("EMAIL"));
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
     * Import all accounts from ACCOUNT_DATA to accounts and usernameToUuid
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
                    }
                    user.setUuid(resultSet.getString("UUID"));
                    user.setUsername(resultSet.getString("USERNAME"));
                    user.setFullName(resultSet.getString("FULLNAME"));
                    user.setGender(resultSet.getString("GENDER"));
                    user.setDateOfBirth(new DateOfBirth(resultSet.getString("DATE_OF_BIRTH")));
                    user.setEmailAddress(resultSet.getString("EMAIL"));
                    user.setPhoneNumber(resultSet.getString("PHONE"));
                    user.setAddress(resultSet.getString("ADDRESS"));
                    user.setPassword(resultSet.getString("PASSWORD"));
                    accounts.put(user.getUuid(), user);
                    usernameToUuid.put(user.getUsername(), user.getUuid());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Import all courses from COURSES to courses
     */
    public static void initializeCourses() {

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
                    course.setPrerequisites(Course.purser(resultSet.getString("PREREQUISITES")));
                    course.setAntirequisites(Course.purser(resultSet.getString("ANTIREQUISITES")));
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
                    ArrayList<String> currentCourses = purseCurrentCourses(resultSet.getString("CURRENT_COURSES"));
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
     * Import all academic requirements from ACADEMIC_REQUIREMENTS and categorize into mandatoryCourses and
     * optionalCourses.
     */
    public static void initializeAcademicRequirements() {

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

    public static void initializeStudentInformation(String uuid) {

        // precondition : Database.account.get(uuid) must exist and must be a student
        // add takenCoursesList and currentCoursesList to Database.account.get(uuid)
        String select = "SELECT * FROM STUDENTS WHERE UUID = '" + uuid + "';";
        try (Connection connection = createConnection(STUDENTS, true);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(select)
        ) {
            Student student = (Student) Database.accounts.get(uuid);
            // get the courseAndGrade arraylist
            ArrayList<CourseAndGrade> takenCourses = purseTakenCourses(resultSet.getString("TAKEN_COURSES"));
            // import to Database.account.get(uuid)
            for (CourseAndGrade i : takenCourses) {
                student.addTakenCourses(i);
            }
            // get the currentCourses arraylist
            ArrayList<String> currentCourses = purseCurrentCourses(resultSet.getString("CURRENT_COURSES"));
            // import to Database.account.get(uuid)
            for (String i : currentCourses) {
                student.addCurrentCourses(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Import the corresponding faculty information from FACULTY by the uuid
     *
     * @param uuid
     */
    public static void initializeFacultyInformation(String uuid) {
        // precondition : Database.account.get(uuid) must exist and must be a faculty
        // add coursesId to Database.account.get(uuid).coursesTeaching
        String select = "SELECT * FROM FACULTY WHERE UUID = '" + uuid + "';";
        try (Connection connection = createConnection(FACULTY, true);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(select)
        ) {
            Faculty faculty = (Faculty) Database.accounts.get(uuid);
            // get the courseAndGrade arraylist
            ArrayList<String> coursesTeaching = purseCurrentCourses(resultSet.getString("COURSES_TEACHING"));
            // import to Database.account.get(uuid)
            for (String i : coursesTeaching) {
                faculty.addCoursesTeaching(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static ArrayList<CourseAndGrade> purseTakenCourses(String s) {
        // CPSC123 3.5
        // CPSC123 3.4
        ArrayList<CourseAndGrade> result = new ArrayList<>();
        if (s != null) {
            String[] temp = s.split(" ");
            for (int i = 0; i < temp.length; i += 2) {
                result.add(new CourseAndGrade(temp[i], temp[i + 1]));
            }
        }
        return result;
    }

    private static ArrayList<String> purseCurrentCourses(String s) {
        // CPSC233 CPSC112
        ArrayList<String> result = new ArrayList<>();
        if (s != null) {
            String[] temp = s.split(" ");
            for (int i = 0; i < temp.length; ++i) {
                result.add(temp[i]);
            }
        }
        return result;
    }

    /**
     * Commit a new account created by the admin to database
     *
     * @param user
     */
    public static void createAccountCommit(User user) {

        String insert = "INSERT INTO USER VALUES (" + "'" + user.getUuid() + "'" + ","
                + "'" + user.getRole() + "'" + ","
                + "'" + Constants.ESCAPE_CHARACTER(user.getUsername()) + "'" + ","
                + "'" + user.getFullName() + "'" + ","
                + "'" + user.getGender() + "'" + ","
                + "'" + user.getDateOfBirth().toString() + "'" + ","
                + "'" + Constants.ESCAPE_CHARACTER(user.getEmailAddress()) + "'" + ","
                + "'" + user.getPhoneNumber() + "'" + ","
                + "'" + Constants.ESCAPE_CHARACTER(user.getAddress()) + "'" + ","
                + "'" + Constants.ESCAPE_CHARACTER(user.getPassword()) + "'" + ");";

        try (Connection connection = createConnection(ACCOUNT_DATA, false);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(insert);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if the user is a student, update STUDENT.db as well
        if (user.getRole().equals("STUDENT")) {
            insert = "INSERT INTO STUDENTS (\"UUID\") VALUES (" + "'" + user.getUuid() + "'" + ");";
            try (Connection connection = createConnection(STUDENTS, false);
                 Statement statement = connection.createStatement()
            ) {
                statement.executeUpdate(insert);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // if the user is a faculty, update FACULTY.db as well
        else if (user.getRole().equals("FACULTY")) {
            insert = "INSERT INTO FACULTY (\"UUID\") VALUES (" + "'" + user.getUuid() + "'" + ");";
            try (Connection connection = createConnection(FACULTY, false);
                 Statement statement = connection.createStatement()
            ) {
                statement.executeUpdate(insert);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Commit a new course created by the admin to database
     *
     * @param course
     */
    public static void createCourseCommit(Course course) {

        String insert =
                "INSERT INTO COURSES VALUES (" + "'" + Constants.ESCAPE_CHARACTER(course.getCourseId()) + "'," +
                        "'" + Constants.ESCAPE_CHARACTER(course.getCourseName()) + "'," +
                        "'" + Constants.ESCAPE_CHARACTER(course.getCourseDescription()) + "'," +
                        "'" + Constants.ESCAPE_CHARACTER(course.getCourseUnits()) + "'," +
                        "'" + Constants.ESCAPE_CHARACTER(course.prerequisitesToString()) + "'," +
                        "'" + Constants.ESCAPE_CHARACTER(course.antirequisitesToString()) + "'," +
                        "'" + course.getCanBeRepeated() + "');";

        try (Connection connection = createConnection(COURSES, false);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Commit a change of a property of a user to database
     *
     * @param uuid
     * @param userField
     */
    public static void accountUpdateCommit(String uuid, Constants.USER_FIELD userField) {

        String update = "UPDATE USER SET ";
        switch (userField) {
            case USERNAME:
                update = update + "USERNAME = \'" + Constants.ESCAPE_CHARACTER(accounts.get(uuid).getUsername());
                break;
            case FULLNAME:
                update = update + "FULLNAME = \'" + accounts.get(uuid).getFullName();
                break;
            case ROLE:
                update = update + "ROLE = \'" + accounts.get(uuid).getRole();
                break;
            case GENDER:
                update = update + "GENDER = \'" + accounts.get(uuid).getGender();
                break;
            case DATE_OF_BIRTH:
                update = update + "DATE_OF_BIRTH = \'" + accounts.get(uuid).getDateOfBirth().toString();
                break;
            case EMAIL:
                update = update + "EMAIL = \'" + Constants.ESCAPE_CHARACTER(accounts.get(uuid).getEmailAddress());
                break;
            case PHONE:
                update = update + "PHONE = \'" + accounts.get(uuid).getPhoneNumber();
                break;
            case ADDRESS:
                update = update + "ADDRESS = \'" + Constants.ESCAPE_CHARACTER(accounts.get(uuid).getAddress());
                break;
            case PASSWORD:
                update = update + "PASSWORD = \'" + Constants.ESCAPE_CHARACTER(accounts.get(uuid).getPassword());
                break;
            default:
                break;
        }
        update = update + "\' WHERE UUID = \'" + uuid + "\';";
        try (Connection connection = createConnection(ACCOUNT_DATA, false);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Commit a change of a property of a course to database
     *
     * @param courseId
     * @param courseField
     */
    public static void courseUpdateCommit(String courseId, Constants.COURSE_FIELD courseField) {
        String update = "UPDATE COURSES SET ";
        String temp;
        switch (courseField) {
            case COURSE_NAME:
                update =
                        update + "COURSE_NAME = \'" + Constants.ESCAPE_CHARACTER(courses.get(courseId).getCourseName()) + "\'";
                break;
            case COURSE_DESCRIPTION:
                update =
                        update + "COURSE_DESCRIPTION = \'" + Constants.ESCAPE_CHARACTER(courses.get(courseId).getCourseDescription()) + "\'";
                break;
            case COURSE_UNITS:
                update = update + "COURSE_UNITS = \'" + courses.get(courseId).getCourseUnits() + "\'";
                break;
            case PREREQUISITES:
                temp = courses.get(courseId).prerequisitesToString();
                if (temp.isEmpty()) {
                    update = update + "PREREQUISITES = NULL";
                } else {
                    update = update + "PREREQUISITES = \'" + temp + "\'";
                }
                break;
            case ANTIREQUISITES:
                temp = courses.get(courseId).antirequisitesToString();
                if (temp.isEmpty()) {
                    update = update + "ANTIREQUISITES = NULL";
                } else {
                    update = update + "ANTIREQUISITES = \'" + temp + "\'";
                }
                break;
            case CAN_BE_REPEATED:
                update = update + "CAN_BE_REPEATED = \'" + courses.get(courseId).getCanBeRepeated() + "\'";
            default:
                break;
        }

        update = update + " WHERE COURSE_ID = \'" + courseId + "\';";
        try (Connection connection = createConnection(COURSES, false);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void courseUpdateCommitToRequirements(String courseId, boolean isAcademicRequirements, boolean isMandatory) {
        String insert;
        if (isMandatory) {
            insert = "INSERT INTO MANDATORY_COURSES VALUES ('" + Constants.ESCAPE_CHARACTER(courseId) + "');";
        } else {
            insert = "INSERT INTO OPTIONAL_COURSES VALUES ('" + Constants.ESCAPE_CHARACTER(courseId) + "');";
        }
        String filepath;
        if (isAcademicRequirements) {
            filepath = ACADEMIC_REQUIREMENTS;
        } else {
            filepath = INTERNSHIP_REQUIREMENTS;
        }
        try (Connection connection = createConnection(filepath, false);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(insert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void courseRemovalCommitToRequirements(String courseId, boolean isAcademicRequirements, boolean isMandatory) {
        String delete;
        if (isMandatory) {
            delete = "DELETE FROM MANDATORY_COURSES WHERE COURSE_ID = '" + Constants.ESCAPE_CHARACTER(courseId) + "';";
        } else {
            delete = "DELETE FROM OPTIONAL_COURSES WHERE COURSE_ID = '" + Constants.ESCAPE_CHARACTER(courseId) + "';";
        }
        String filepath;
        if (isAcademicRequirements) {
            filepath = ACADEMIC_REQUIREMENTS;
        } else {
            filepath = INTERNSHIP_REQUIREMENTS;
        }
        try (Connection connection = createConnection(filepath, false);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void facultyAddCoursesTeachingCommit(String uuid, String courseId) {
        String select = "SELECT * FROM FACULTY WHERE UUID = '" + uuid + "';";
        String coursesTeaching;
        try (Connection connection = createConnection(FACULTY, false);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(select)
        ) {
            while (resultSet.next()) {
                coursesTeaching = resultSet.getString("COURSES_TEACHING");
                // TODO
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove an account in the database
     *
     * @param uuid
     */
    public static void accountRemovalCommit(String uuid) {
        String delete = "DELETE FROM USER WHERE uuid = '" + uuid + "';";
        try (Connection connection = createConnection(ACCOUNT_DATA, false);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // if the user is a student, update STUDENT.db as well
        if (Database.accounts.get(uuid).getRole().equals("STUDENT")) {
            delete = "DELETE FROM STUDENTS WHERE uuid = '" + uuid + "';";
            try (Connection connection = createConnection(STUDENTS, false);
                 Statement statement = connection.createStatement()
            ) {
                statement.executeUpdate(delete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // if the user is a faculty, update FACULTY.db as well
        else if (Database.accounts.get(uuid).getRole().equals("FACULTY")) {
            delete = "DELETE FROM FACULTY WHERE uuid = '" + uuid + "';";
            try (Connection connection = createConnection(FACULTY, false);
                 Statement statement = connection.createStatement()
            ) {
                statement.executeUpdate(delete);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Remove a course in the database
     *
     * @param courseId
     */
    public static void courseRemovalCommit(String courseId) {
        String delete = "DELETE FROM COURSES WHERE COURSE_ID = '" + Constants.ESCAPE_CHARACTER(courseId) + "';";
        try (Connection connection = createConnection(COURSES, false);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Release all resources we stored in data structures and enforce a garbage collection
     */
    public static void releaseAllResources() {
        Database.accounts.clear();
        Database.courses.clear();
        Database.optionalCourses.clear();
        Database.optionalCoursesForInternship.clear();
        Database.mandatoryCourses.clear();
        Database.mandatoryCoursesForInternship.clear();
        Database.coursesCurrentlyTaught.clear();
        Database.usernameToUuid.clear();
        System.gc();
    }

}
