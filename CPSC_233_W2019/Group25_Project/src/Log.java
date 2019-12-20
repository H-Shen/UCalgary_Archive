import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The {@code Log} class Saving all successful and attempting login operations on history log.txt
 *
 * @author Group 25
 * @date 2019/04/08
 */

public class Log {

    /**
     * The method creates a queue log array as a linked list for all login/logout activities.
     *
     * @date 2019/04/08
     */
    public static Queue<String> logArray = new LinkedList<>();

    /**
     * The method adds log activities to the array.
     *
     * @date 2019/04/08
     */
    private static void addToLogArray(String s) {
        if (logArray.size() == Constants.MAX_HISTORY_LINES) {
            logArray.remove();
        }
        logArray.add(s);
        saveLogToFile();
    }

    /**
     * The method writes new log items to the log file from the array.
     *
     * @date 2019/04/08
     */
    private static void saveLogToFile() {
        try (PrintWriter pw = new PrintWriter(Constants.LOG_FILEPATH)) {
            for (String i : logArray) {
                pw.println(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The method reads the log file and adds to the array.
     *
     * @date 2019/04/08
     */
    public static void readLogFromFile() {
        logArray = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Constants.LOG_FILEPATH))) {
            String content;
            while ((content = br.readLine()) != null) {
                logArray.add(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The method logs all attempts to login, and whether it what successfully or not with a timestamp.
     *
     * @param tm        current timestamp
     * @param ipAddress ipAddress of user attempting to log in
     * @param username  username of user
     * @param status    the status of the login (successful or not)
     * @date 2019/04/08
     */
    public static void login(Timestamp tm, String ipAddress, String username, boolean status) {
        String s;
        if (!status) {
            s = tm + " " + ipAddress + " failed to login with USERNAME = " + username;
        } else {
            s = tm + " " + ipAddress + " successfully login with USERNAME = " + username;
        }
        addToLogArray(s);
    }

    /**
     * The method logs when and which user logs in and adds to array
     *
     * @param tm   current timestamp
     * @param uuid UUID of user
     * @date 2019/04/08
     */
    public static void userLogin(Timestamp tm, String uuid) {
        String s = tm + " " + uuid + " ROLE = " + Database.accounts.get(uuid).getRole() + " login";
        addToLogArray(s);
    }

    /**
     * The method adds to the array when a user logs out with a timestamp.
     *
     * @param tm   current timestamp
     * @param uuid UUID of user
     * @date 2019/04/08
     */
    public static void userLogout(Timestamp tm, String uuid) {
        String s = tm + " " + uuid + " ROLE = " + Database.accounts.get(uuid).getRole() + " logout";
        addToLogArray(s);
    }

    /**
     * The method adds to the log array when a new course is added.
     *
     * @param tm       current timestamp
     * @param uuid     UUID of user
     * @param courseId course Id that has been added
     * @date 2019/04/08
     */
    public static void addACourse(Timestamp tm, String uuid, String courseId) {
        String s = tm + " " + uuid + " ROLE = ADMIN" + " added a course whose" +
                " COURSE ID = " + courseId +
                " COURSE NAME = " + "\'" + Database.courses.get(courseId).getCourseName() + "\'" +
                " COURSE DESCRIPTION = " + "\'" + Database.courses.get(courseId).getCourseDescription() + "\'" +
                " COURSE UNITS = " + "\'" + Database.courses.get(courseId).getCourseUnits() + "\'" +
                " COURSE PREREQUISITES = " + "\'" + Database.courses.get(courseId).getPrerequisitesAsString() + "\'" +
                " COURSE ANTI-REQUISITES = " + "\'" + Database.courses.get(courseId).getAntirequisitesAsString() + "\'";
        addToLogArray(s);
    }

    /**
     * The method adds to the log array when a course is removed.
     *
     * @param tm       current timestamp
     * @param uuid     UUID of user
     * @param courseId course Id that has been removed
     * @date 2019/04/08
     */
    public static void removeACourse(Timestamp tm, String uuid, String courseId) {
        String s = tm + " " + uuid + " ROLE = ADMIN" + " removed a course whose COURSE ID = " + courseId;
        addToLogArray(s);
    }

    /**
     * The method adds to the log array when a course is updated.
     *
     * @param tm       current timestamp
     * @param uuid     UUID of user
     * @param courseId course Id that has been changed
     * @date 2019/04/08
     */
    public static void updateACourse(Timestamp tm, String uuid, String courseId) {
        String s = tm + " " + uuid + " ROLE = ADMIN" + " updated a course whose COURSE ID = " + courseId;
        addToLogArray(s);
    }

    /**
     * The method adds tp the log array when an account is added
     *
     * @param tm          current timestamp
     * @param uuid        UUID of operator
     * @param uuidBeAdded UUID of account be added
     * @date 2019/04/08
     */
    public static void addAnAccount(Timestamp tm, String uuid, String uuidBeAdded) {
        String s = tm + " " + uuid + " ROLE = ADMIN" + " added an account whose UUID = " + uuidBeAdded +
                " ROLE = " + Database.accounts.get(uuidBeAdded).getRole();
        addToLogArray(s);
    }

    /**
     * The method adds to the log array when an account is removed
     *
     * @param tm            current timestamp
     * @param uuid          UUID of operator
     * @param uuidBeRemoved UUID of account be removed
     * @date 2019/04/08
     */
    public static void removeAnAccount(Timestamp tm, String uuid, String uuidBeRemoved) {
        String s = tm + " " + uuid + " ROLE = ADMIN" + " removed an account whose UUID = " + uuidBeRemoved;
        addToLogArray(s);
    }

    /**
     * The method adds to the log array when an account it updated
     *
     * @param tm            current timestamp
     * @param uuid          UUID of operator
     * @param uuidBeUpdated UUID of account be updated
     * @date 2019/04/08
     */
    public static void updateAnAccount(Timestamp tm, String uuid, String uuidBeUpdated) {
        String s = tm + " " + uuid + " ROLE = ADMIN" + " updated an account whose UUID = " + uuidBeUpdated;
        addToLogArray(s);
    }

    /**
     * The method adds to log array when a student has a grade added
     *
     * @param tm           current timestamp
     * @param uuid         UUID of operator
     * @param uuidBeGraded UUID of student account be graded
     * @date 2019/04/08
     */
    public static void gradedAStudent(Timestamp tm, String uuid, String uuidBeGraded) {
        String s = tm + " " + uuid + " ROLE = " + Database.accounts.get(uuid).getRole() + " removed an account whose UUID = " + uuidBeGraded;
        addToLogArray(s);
    }

    /**
     * The method adds to log array when a user updates personal information
     *
     * @param tm   current timestamp
     * @param uuid UUID of account
     * @date 2019/04/08
     */
    public static void updateOwnAccount(Timestamp tm, String uuid) {
        String s = tm + " " + uuid + " ROLE = " + Database.accounts.get(uuid).getRole() + " updated the account " +
                "information";
        addToLogArray(s);
    }

    /**
     * The method adds to log array when a student enrolls a course
     *
     * @param tm          current timestamp
     * @param uuid        UUID of account
     * @param courseId    course id to enroll
     * @param facultyUuid the uuid of faculty
     * @date 2019/04/12
     */
    public static void enrollACourse(Timestamp tm, String uuid, String courseId, String facultyUuid) {
        String s = tm + " " + uuid + " ROLE = STUDENT" + " enrolled a course whose COURSE ID = " + courseId + " being" +
                " taught by the faculty whose UUID = " + facultyUuid;
    }

    /**
     * The method logs the ip address when someone attempts to login, if the ip address cannot be recognized, return
     * "Unknown user"
     *
     * @return ipAddress when user is unknown in the system
     * @date 2019/04/08
     */
    public static String getIpAddress() {
        String ipAddress = "Unknown user";
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipAddress;
    }
}
