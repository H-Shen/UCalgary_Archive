import java.util.ArrayList;
import java.util.HashSet;

/**
 * The {@code FacultyTextUi} class inherits class TextUI and contains static methods used specifically for Faculty.
 */
public class FacultyTextUI extends TextUI {

    /**
     * Constructor with uuid that initializes the user's information according to that uuid from the database.
     * The options that are available to a Faculty user is listed and the user can choose and the corresponding methods
     * the TextUI is called.
     *
     * @param uuid a String which contains the uuid of the user that logged in.
     */
    public FacultyTextUI(String uuid) {

        super(uuid);
        Database.initializeFacultyInformation(uuid);
        Faculty faculty = (Faculty) Database.accounts.get(uuid);

        String select;

        while (true) {

            System.out.println("    +------------------------------------+");
            System.out.println("    |          " + Constants.COLOR_WRAPPER("CONTROL  PANEL", Constants.WHITE_BOLD) +
                    "            |");
            System.out.println("    +------------------------------------+");
            System.out.println("    |   1. List all available courses    |");
            System.out.println("    |   2. Courses management            |");
            System.out.println("    |   3. Search a course               |");
            System.out.println("    |   4. Personal information          |");
            System.out.println("    |   5. Account setting               |");
            System.out.println("    |   6. Logout                        |");
            System.out.println("    +------------------------------------+");

            System.out.println();
            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();

            switch (select) {
                case "1":
                    listAllCourses();
                    break;
                case "2":
                    coursesCurrentlyTeaching(faculty);
                    // courseGrading(faculty);
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
     * @param faculty the faculty user.
     */
    public static void coursesCurrentlyTeaching(Faculty faculty) {

        ArrayList<String> temp = faculty.getCoursesTeaching();
        System.out.println(Constants.COLOR_WRAPPER("Courses currently teaching:", Constants.WHITE_BOLD));
        System.out.println();
        for (String i : temp) {
            System.out.println("    " + i + "   Students: " + Database.courses.get(i).getStudentsWhoAreTaking().size());
        }
        System.out.println();

    }

    /**
     * The arrayList created from getCoursesTeaching method must not be empty.
     *
     * @param faculty the fauculty user.
     */
    public static void courseGrading(Faculty faculty) {

        // pre-cond: getCoursesTeaching() must be not empty
        ArrayList<String> temp = faculty.getCoursesTeaching();
        if (temp.isEmpty()) {
            return;
        }
        System.out.println();

        // get courseId
        String courseId;
        while (true) {
            System.out.print("Course ID to grade: ");
            courseId = Constants.IN.nextLine();
            if (temp.contains(courseId)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        HashSet<String> uuidList = Database.courses.get(courseId).getStudentsWhoAreTaking();
        // must at least 1 student is currently taking the course
        if (uuidList.isEmpty()) {
            System.out.println(Constants.COLOR_WRAPPER("No one is taking the course!", Constants.RED));
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();
            return;
        }

        System.out.println();
        // list all students who are taking the course
        ArrayList<String> studentUuidList = new ArrayList<>();
        for (String i : uuidList) {
            studentUuidList.add(i);
        }

        for (int i = 0; i < studentUuidList.size(); ++i) {

            System.out.println("INDEX NUMBER: " + (i + 1));
            System.out.println();

            StringBuilder sb = new StringBuilder();
            sb.append(Constants.COLOR_WRAPPER("         UUID : ", Constants.WHITE_BOLD) + studentUuidList.get(i) + "\n");
            sb.append(Constants.COLOR_WRAPPER("     FULLNAME : ", Constants.WHITE_BOLD) + Database.accounts.get(studentUuidList.get(i)).getFullName() + "\n");

            System.out.println(sb);
        }

        // prompt the faculty choose the index number
        System.out.println();
        String selectIndex;
        int    selectIndexToInt;
        while (true) {

            System.out.print(Constants.COLOR_WRAPPER("Select index: ", Constants.WHITE_BOLD));
            selectIndex = Constants.IN.nextLine();

            // judge if select index is valid
            try {
                // can convert to integer
                selectIndexToInt = Integer.parseInt(selectIndex);
                // no leading zeros
                if (!String.valueOf(selectIndexToInt).equals(selectIndex)) {
                    throw new IllegalArgumentException();
                }
                // must >= 1 and <= studentUuidList.size()
                if (selectIndexToInt < 1 || selectIndexToInt > studentUuidList.size()) {
                    throw new IllegalArgumentException();
                }
                break;
            } catch (Exception e) {
                System.out.println(Constants.INVALID_INPUT);
            }
        }
        // convert the number to correct index in the array by decreasing 1
        --selectIndexToInt;
        Student student = (Student) Database.accounts.get(courseId);

        System.out.println();
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.COLOR_WRAPPER("         UUID : ", Constants.WHITE_BOLD) + student.getUuid() + "\n");
        sb.append(Constants.COLOR_WRAPPER("     FULLNAME : ", Constants.WHITE_BOLD) + student.getFullName() + "\n");
        System.out.println(sb);

        // grade the student
        String grade;
        while (true) {
            System.out.print("Grade: ");
            grade = Constants.IN.nextLine();

            if (!Validation.isGradeValid(grade)) {
                System.out.println(Constants.INVALID_INPUT);
            } else {
                break;
            }
        }

        // archive the grade in the student account
        student.addTakenCourses(new CourseAndGrade(courseId, grade));
        // delete from CurrentCoursesList of the student
        student.removeFromCurrentCoursesList(courseId);

        // update STUDENT.db the grade to TAKEN_COURSES and trim
        Database.updateStudentTakenCourses(student.getUuid(), student.getTakenCoursesListAsString());

        // update STUDENT.db 1. update the course Id from CURRENT_COURSES and trim
        Database.updateStudentCurrentCourses(student.getUuid(), student.getCurrentCoursesListAsString());

        // removeStudentsWhoAreTaking from the Database.courses
        Database.courses.get(courseId).removeStudentsWhoAreTaking(student.getUuid());


    }
}
