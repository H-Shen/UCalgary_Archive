import java.util.ArrayList;

/**
 * The part of TextUI for Faculty. The GUI design will refer its logic.
 */

public class FacultyTextUI extends TextUI {

    public FacultyTextUI(String uuid) {

        super(uuid);
        Database.initializeFacultyInformation(uuid);
        Faculty user = (Faculty) Database.accounts.get(uuid);

        String select;

        while (true) {

            System.out.println(Constants.COLOR_WRAPPER("FACULTY PANEL", Constants.WHITE_BOLD));
            System.out.println();

            System.out.println(" 1. List all available courses");
            System.out.println(" 2. Manage the courses currently teaching");
            System.out.println(" 3. Search a course");
            System.out.println(" 4. Modify personal information");
            System.out.println(" 5. Logout");

            System.out.println();
            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();
            System.out.println();
            switch (select) {

                case "1":
                    listAllCourses();
                    break;
                case "2":
                    coursesCurrentlyTeaching(user);
                    // TODO add grading feature
                    break;

                case "3":
                    searchCourses();
                    System.out.println();
                    break;

                case "4":

                    modifyAccountForNonAdmin(user);
                    break;

                case "5":
                    return;

                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
            }
        }
    }

    public static void coursesCurrentlyTeaching(Faculty user) {

        ArrayList<String> temp = user.getCoursesTeaching();
        System.out.println(Constants.COLOR_WRAPPER("Courses currently teaching:", Constants.WHITE_BOLD));
        System.out.println();
        for (String i : temp) {
            System.out.println("    " + i + "   Students: " + Database.courses.get(i).getStudentsWhoAreTaking().size());
        }
        System.out.println();

    }
}
