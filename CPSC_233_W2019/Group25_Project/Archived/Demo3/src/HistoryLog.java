import java.net.InetAddress;

/**
 * Saving all successful and attempting login operationas on history log.txt
 */

public class HistoryLog {

    public static final int MAX_HISTORY_LINES = 1000;

    private static String ipAdress;

    public static void initialization() {
        try {
            ipAdress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // User login successfully
    public static void userLoginSuccessfully(String uuid, String username) {
        //System.out.println(uuid + " " + Database.accounts.get(uuid).getRole() + " login successfully as USERNAME =
        // " + username);
    }

    // User login failed
    public static void userLoginFailed(String username) {
        //System.out.println(ipAdress + " login failed as USERNAME = " + username);
    }

    // User Logout
    public static void userLogout(String uuid) {
        //System.out.println(uuid + " " + Database.accounts.get(uuid).getRole() + " logout successfully");
    }

    // Admin modify a course
    public static void modifyACourse(String uuid, String courseId) {
        //System.out.println(uuid + " updated a course whose COURSE ID = " + courseId);
    }

    // Admin modify an account
    public static void modifyAnAccount(String uuid, String accountUuid) {
        //System.out.println(uuid + " updated an account whose UUID = " + accountUuid);
    }

    // Admin modify status of enrollment
    public static void modifyStatusOfEnrollment(String uuid, String updatedStatus) {
        //System.out.println(uuid + " updated enrollment status to " + updatedStatus);
    }

    // Admin remove an account
    public static void removeAnAccount(String uuid, String accountUuid) {
        //System.out.println(uuid + " removed an account whose UUID = " + accountUuid);
    }

    // Admin remove a course
    public static void removeACourse(String uuid, String courseId) {
        //System.out.println(uuid + " removed a course whose COURSE ID = " + courseId);
    }

    // Admin create an account
    public static void createAnAccount(String uuid, String accountUuid) {
        //System.out.println(uuid + " created an account whose UUID = " + accountUuid);
    }

    // Admin create a course
    public static void createACourse(String uuid, String courseId) {
        //System.out.println(uuid + " created a course whose COURSE ID = " + courseId);
    }

    // Student/Faculty update personal information except password
    public static void updatePersonalInformation(String uuid, Constants.USER_FIELD property) {
        //System.out.println(uuid + " " + Database.accounts.get(uuid).getRole() + " updated " + property);
    }

    // Student/Faculty update personal password
    public static void updatePersonalPassword(String uuid) {
        //System.out.println(uuid + " " + Database.accounts.get(uuid).getRole() + " updated the password");
    }

    // Student enroll/drop courses
    public static void enrollOrDropCourses(String uuid, String courseId, boolean enroll) {
        //System.out.println(uuid + ((enroll) ? " enrolled " : " dropped ") + courseId);
    }

    // Faculty grade a course for a student
    public static void gradeAStudent(String uuid, String studentUuid, String courseId, String grade) {
        //System.out.println(uuid + " graded " + studentUuid + " " + grade + " on " + courseId);
    }


}
