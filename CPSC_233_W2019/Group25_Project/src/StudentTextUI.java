import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * The {@code StudentTextUI} class inherits class StudentUI and contains static methods used specifically for students.
 *
 * @author Group 25
 * @date 2019/04/08
 */

public class StudentTextUI extends TextUI {

    /**
     * Constructor with uuid that initializes the user's information according to that uuid from the database.
     * The options that are available to a Student user is listed and the user can choose and the corresponding methods
     * the TextUI is called.
     *
     * @param uuid a String which contains the uuid of the user that logged in
     * @date 2019/04/08
     */
    public StudentTextUI(String uuid) {

        super(uuid);
        Student student = (Student) Database.accounts.get(uuid);
        String  select;

        while (true) {

            System.out.println("    +------------------------------------+");
            System.out.println("    |          " + Constants.colorWrapper("CONTROL  PANEL", Constants.WHITE_BOLD) + "            |");
            System.out.println("    +------------------------------------+");
            System.out.println("    |   1. List all available courses    |");
            System.out.println("    |   2. Academic requirements         |");
            System.out.println("    |   3. List the courses history      |");
            System.out.println("    |   4. Courses search                |");
            System.out.println("    |   5. Personal information          |");
            System.out.println("    |   6. Account setting               |");
            System.out.println("    |   7. Logout                        |");
            System.out.println("    +------------------------------------+");
            System.out.println();
            System.out.print(Constants.colorWrapper("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();

            switch (select) {
                case "1":
                    listAllCourses();
                    break;
                case "2":
                    coursesNeedToTakeBeforeGraduation(student);
                    System.out.println();
                    Constants.pressEnterToContinue();
                    System.out.println();
                    coursesNeedToTakeBeforeInternship(student);
                    System.out.println();
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "3":
                    System.out.println();
                    courseHistory(student);
                    System.out.println();
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "4":
                    searchCourses();
                    System.out.println();
                    break;
                case "5":
                    showAccountInformation(student);
                    break;
                case "6":
                    modifyAccountForNonAdmin(student);
                    break;
                case "7":
                    return;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
            }
        }
    }

    /**
     * The method prints items in the array list after sorting it in a pretty format.
     *
     * @param array the array list needs to be printed
     * @date 2019/04/08
     */
    private static void prettyPrint(ArrayList<String> array) {

        Collections.sort(array);
        int count = 0;
        for (String s : array) {
            System.out.printf("%8s", "    " + s);
            ++count;
            if (count == Constants.MAX_COURSES_ID_A_LINE) {
                System.out.println();
                count = 0;
            }
        }
        if (count != 0) {
            System.out.println();
        }
    }

    /**
     * This function displays all courses the student has taken and their grades, as well as courses the student is
     * taking
     *
     * @param student the student object
     * @date 2019/04/08
     */
    public static void courseHistory(Student student) {

        double overallGpa = 0.0;
        System.out.println(Constants.colorWrapper("Courses History:", Constants.WHITE_BOLD));
        System.out.println();
        ArrayList<CourseAndGrade> coursesTaken = student.getTakenCoursesList();
        for (CourseAndGrade i : coursesTaken) {
            System.out.println(Constants.colorWrapper("COURSE ID: ", Constants.WHITE_BOLD) + i.getCourseId());
            System.out.println(Constants.colorWrapper("    GRADE: ", Constants.WHITE_BOLD) + i.getGrade());
            System.out.println(Constants.colorWrapper("   STATUS: ", Constants.WHITE_BOLD) + "Closed");
            System.out.println();
            overallGpa += Double.parseDouble(i.getGrade());
        }

        ArrayList<String> coursesTaking = student.getCurrentCoursesList();
        for (String i : coursesTaking) {
            System.out.println(Constants.colorWrapper("COURSE ID: ", Constants.WHITE_BOLD) + i);
            System.out.println(Constants.colorWrapper("    GRADE: ", Constants.WHITE_BOLD) + "Grading");
            System.out.println(Constants.colorWrapper("   STATUS: ", Constants.WHITE_BOLD) + "Ongoing");
            System.out.println();
        }

        // stats
        if (!coursesTaken.isEmpty()) {
            overallGpa = overallGpa / coursesTaken.size();
        }
        System.out.println("Total courses taken: " + coursesTaken.size());
        System.out.println("        Overall GPA: " + String.format("%.2f", overallGpa));

    }

    /**
     * The method displays the courses the student needs to take before an internship.
     *
     * @param student the student user.
     * @date 2019/04/08
     */
    private static void coursesNeedToTakeBeforeInternship(Student student) {

        // initialization
        ArrayList<String> optionalCourses  = new ArrayList<>();
        HashSet<String>   coursesTakenSet  = new HashSet<>();
        ArrayList<String> mandatoryCourses = new ArrayList<>();

        // filter
        ArrayList<CourseAndGrade> tempArr0 = student.getTakenCoursesList();
        for (CourseAndGrade i : tempArr0) {
            coursesTakenSet.add(i.getCourseId());
        }
        ArrayList<String> tempArr1 = student.getCurrentCoursesList();
        coursesTakenSet.addAll(tempArr1);
        for (String i : Database.mandatoryCoursesForInternship) {
            if (!coursesTakenSet.contains(i)) {
                mandatoryCourses.add(i);
            }
        }
        for (String i : Database.optionalCoursesForInternship) {
            if (!coursesTakenSet.contains(i)) {
                optionalCourses.add(i);
            }
        }

        // sort
        Collections.sort(mandatoryCourses);
        Collections.sort(optionalCourses);

        // output
        System.out.println("You still need to take follow courses before applying for internship:");
        System.out.println();
        System.out.println("Mandatory courses:");
        System.out.println();

        prettyPrint(mandatoryCourses);

        System.out.println();
        System.out.println("You also need to take 18 units of courses below before applying for internship:");
        System.out.println();
        System.out.println("Optional courses:");
        System.out.println();

        prettyPrint(optionalCourses);

    }

    /**
     * The method displays the courses that the student needs to take before graduation.
     *
     * @param student the student user
     * @date 2019/04/08
     */
    private static void coursesNeedToTakeBeforeGraduation(Student student) {

        // initialization
        ArrayList<String> mandatoryCourses = new ArrayList<>();
        ArrayList<String> optionalCourses  = new ArrayList<>();
        HashSet<String>   coursesTakenSet  = new HashSet<>();

        // filter
        ArrayList<CourseAndGrade> tempArr0 = student.getTakenCoursesList();
        for (CourseAndGrade i : tempArr0) {
            coursesTakenSet.add(i.getCourseId());
        }
        ArrayList<String> tempArr1 = student.getCurrentCoursesList();
        coursesTakenSet.addAll(tempArr1);
        for (String i : Database.mandatoryCourses) {
            if (!coursesTakenSet.contains(i)) {
                mandatoryCourses.add(i);
            }
        }
        for (String i : Database.optionalCourses) {
            if (!coursesTakenSet.contains(i)) {
                optionalCourses.add(i);
            }
        }

        // sort
        Collections.sort(mandatoryCourses);
        Collections.sort(optionalCourses);

        // output
        System.out.println();
        System.out.println("You still need to take follow courses before applying for graduation:");
        System.out.println();
        System.out.println("Mandatory courses:");
        System.out.println();
        prettyPrint(mandatoryCourses);

        System.out.println();
        System.out.println("You also need to take 18 units of courses below before applying for graduation:");
        System.out.println();
        System.out.println("Optional courses (Out of field):");
        System.out.println();
        prettyPrint(optionalCourses);
    }
}
