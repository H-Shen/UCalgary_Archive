import java.util.ArrayList;
import java.util.HashSet;

/**
 * The {@code FacultyTextUI} class inherits class TextUI and contains static methods used specifically for Faculty.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class FacultyTextUI extends TextUI {

    /**
     * Constructor with uuid that initializes the user's information according to that uuid from the database.
     * The options that are available to a Faculty user is listed and the user can choose and the corresponding methods
     * the TextUI is called.
     *
     * @param uuid a String which contains the uuid of the user that logged in.
     * @date 2019/04/08
     */
    public FacultyTextUI(String uuid) {

        super(uuid);
        Database.initializeFacultyInformation(uuid);
        Faculty faculty = (Faculty) Database.accounts.get(uuid);
        String  select;

        while (true) {

            System.out.println("    +------------------------------------+");
            System.out.println("    |          " + Constants.colorWrapper("CONTROL  PANEL", Constants.WHITE_BOLD) +
                    "            |");
            System.out.println("    +------------------------------------+");
            System.out.println("    |   1. List all available courses    |");
            System.out.println("    |   2. Courses currently taking      |");
            System.out.println("    |   3. Search a course               |");
            System.out.println("    |   4. Personal information          |");
            System.out.println("    |   5. Account setting               |");
            System.out.println("    |   6. Logout                        |");
            System.out.println("    +------------------------------------+");
            System.out.println();
            System.out.print(Constants.colorWrapper("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();

            switch (select) {
                case "1":
                    listAllCourses();
                    break;
                case "2":
                    coursesCurrentlyTeaching(faculty);
                    break;
                case "3":
                    searchCourses();
                    System.out.println();
                    break;
                case "4":
                    showAccountInformation(faculty);
                    break;
                case "5":
                    modifyAccountForNonAdmin(faculty);
                    break;
                case "6":
                    return;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
            }
        }
    }

    /**
     * Allows for user's to search for courses that are currently teaching.
     * It creates a temporary arrayList from the results generated in the getCoursesTeaching method and displays it on
     * the screen.
     *
     * @param faculty the faculty user
     * @date 2019/04/08
     */
    public static void coursesCurrentlyTeaching(Faculty faculty) {
        // list courses currently teaching
        ArrayList<String> temp = faculty.getCoursesTeaching();
        System.out.println();
        System.out.println(Constants.colorWrapper("COURSES CURRENTLY TEACHING:", Constants.WHITE_BOLD));
        System.out.println();
        for (String courseId : temp) {
            HashSet<String> uuidList = Database.courses.get(courseId).getStudentsWhoAreTaking();
            System.out.println("    " + courseId + "   Students: " + uuidList.size());
            System.out.println();
            // list students who are taking these courses
            if (!uuidList.isEmpty()) {
                // list all students who are taking the course
                ArrayList<String> studentUuidList = new ArrayList<>(uuidList);
                for (String s : studentUuidList) {
                    String sb = Constants.colorWrapper("         UUID : ", Constants.WHITE_BOLD) + s + "\n" +
                            Constants.colorWrapper("    FULL NAME : ", Constants.WHITE_BOLD) + Database.accounts.get(s).getFullName() + "\n" +
                            Constants.colorWrapper("        EMAIL : ", Constants.WHITE_BOLD) + Database.accounts.get(s).getEmailAddress();
                    System.out.println(sb);
                    System.out.println();
                }
            }
        }
    }
}
