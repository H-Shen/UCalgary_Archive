import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * The {@code StudentextUI} class inherits class StudentUI and contains static methods used specifically for Students.
 */

public class StudentTextUI extends TextUI {

    /**
     * Constructor with uuid that initializes the user's information according to that uuid from the database.
     * The options that are available to a Student user is listed and the user can choose and the corresponding methods
     * the TextUI is called.
     *
     * @param uuid a String which contains the uuid of the user that logged in.
     */
    public StudentTextUI(String uuid) {

        super(uuid);
        Student student = (Student) Database.accounts.get(uuid);
        String  select;

        while (true) {

            System.out.println("    +------------------------------------+");
            System.out.println("    |          " + Constants.COLOR_WRAPPER("CONTROL  PANEL", Constants.WHITE_BOLD) + "            |");
            System.out.println("    +------------------------------------+");
            System.out.println("    |   1. List all available courses    |");
            System.out.println("    |   2. Academic requirements         |");
            System.out.println("    |   3. List the courses history      |");
            System.out.println("    |   4. Courses search                |");
            System.out.println("    |   5. Courses enrollment            |");
            System.out.println("    |   6. Personal information          |");
            System.out.println("    |   7. Account setting               |");
            System.out.println("    |   8. Logout                        |");
            System.out.println("    +------------------------------------+");
            System.out.println();
            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();

            switch (select) {

                case "1":
                    listAllCourses();
                    break;
                case "2":
                    coursesNeedToTakeBeforeGraduation(student);
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    coursesNeedToTakeBeforeInternship(student);
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "3":
                    System.out.println();
                    courseHistory(student);
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                case "4":

                    searchCourses();
                    System.out.println();
                    break;

                case "5":

                    courseEnrollment(student);
                    break;

                case "6":

                    showAccountInformation(student);
                    break;

                case "7":

                    modifyAccountForNonAdmin(student);
                    break;

                case "8":
                    return;

                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
            }
        }

    }

    /**
     * Print in a pretty format of items in the array list after sorting it
     *
     * @param A
     */
    public static void prettyPrint(ArrayList<String> A) {

        Collections.sort(A);

        int count = 0;
        for (int i = 0; i < A.size(); ++i) {
            System.out.printf("%8s", "    " + A.get(i));
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
     * @param student
     */
    public static void courseHistory(Student student) {

        double overallGpa = 0.0;
        System.out.println(Constants.COLOR_WRAPPER("Courses History:", Constants.WHITE_BOLD));
        System.out.println();
        ArrayList<CourseAndGrade> coursesTaken = student.getTakenCoursesList();
        for (CourseAndGrade i : coursesTaken) {
            System.out.println(Constants.COLOR_WRAPPER("COURSE ID: ", Constants.WHITE_BOLD) + i.getCourseId());
            System.out.println(Constants.COLOR_WRAPPER("    GRADE: ", Constants.WHITE_BOLD) + i.getGrade());
            System.out.println(Constants.COLOR_WRAPPER("   STATUS: ", Constants.WHITE_BOLD) + "Closed");
            System.out.println();
            overallGpa += Double.parseDouble(i.getGrade());
        }

        ArrayList<String> coursesTaking = student.getCurrentCoursesList();
        for (String i : coursesTaking) {
            System.out.println(Constants.COLOR_WRAPPER("COURSE ID: ", Constants.WHITE_BOLD) + i);
            System.out.println(Constants.COLOR_WRAPPER("    GRADE: ", Constants.WHITE_BOLD) + "Grading");
            System.out.println(Constants.COLOR_WRAPPER("   STATUS: ", Constants.WHITE_BOLD) + "Ongoing");
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
     * Displays the courses the student needs to take before an internship.
     *
     * @param student the student user.
     */
    public static void coursesNeedToTakeBeforeInternship(Student student) {

        // initialization
        HashSet<String>   coursesTakenSet  = new HashSet<>();
        ArrayList<String> mandatoryCourses = new ArrayList<>();
        ArrayList<String> optionalCourses  = new ArrayList<>();

        // filter
        ArrayList<CourseAndGrade> tempArr0 = student.getTakenCoursesList();
        for (CourseAndGrade i : tempArr0) {
            coursesTakenSet.add(i.getCourseId());
        }
        ArrayList<String> tempArr1 = student.getCurrentCoursesList();
        for (String i : tempArr1) {
            coursesTakenSet.add(i);
        }
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
     * Displays the courses that the student needs to take before graduation.
     *
     * @param student the student user.
     */
    public static void coursesNeedToTakeBeforeGraduation(Student student) {

        // initialization
        HashSet<String>   coursesTakenSet  = new HashSet<>();
        ArrayList<String> mandatoryCourses = new ArrayList<>();
        ArrayList<String> optionalCourses  = new ArrayList<>();

        // filter
        ArrayList<CourseAndGrade> tempArr0 = student.getTakenCoursesList();
        for (CourseAndGrade i : tempArr0) {
            coursesTakenSet.add(i.getCourseId());
        }
        ArrayList<String> tempArr1 = student.getCurrentCoursesList();
        for (String i : tempArr1) {
            coursesTakenSet.add(i);
        }
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

    /**
     * The main logic of the process of a student's enrollment
     *
     * @param student the student user.
     */
    public static void courseEnrollment(Student student) {

        if (User.getEnrollmentStatus().equals("OPEN")) {

            // list all courses opened
            System.out.println("CURRENT OPENED COURSES:");
            System.out.println();

            // print all items in Database.coursesOpen, format: courseId fullNameOfFaculty
            System.out.println("COURSE ID:     INSTRUCTOR:");
            System.out.println();

            // sorting the course Id before printing
            ArrayList<String> keyList = new ArrayList<>(Database.coursesOpen.keySet());
            Collections.sort(keyList);

            for (String i : keyList) {
                System.out.println(" " + i + "      " + Database.accounts.get(Database.coursesOpen.get(i)).getFullName());
            }

            System.out.println();

            System.out.println(Constants.COLOR_WRAPPER("WARNING: If you want to drop a course after successful " +
                    "enrollment, you must consult the adminstrators for help!", Constants.YELLOW_BOLD));
            System.out.println();

            // enter the number of courses want to enroll
            int     numberOfCourses = 0;
            boolean isInputValid    = false;
            while (!isInputValid) {
                System.out.print("Number of courses: (must > 0 and must <= " + Constants.MAX_COURSES_TO_SELECT +
                        ") ");
                String temp = Constants.IN.nextLine();
                for (int i = 1; i <= Constants.MAX_COURSES_TO_SELECT; ++i) {
                    if (String.valueOf(i).equals(temp)) {
                        numberOfCourses = Integer.parseInt(temp);
                        isInputValid = true;
                        break;
                    }
                }
                if (!isInputValid) {
                    System.out.println(Constants.INVALID_INPUT);
                }
            }

            HashSet<String> courseSelected = new HashSet<>();
            // for loop it
            for (int i = 1; i <= numberOfCourses; ++i) {

                while (true) {

                    switch (i) {
                        case 1:
                            System.out.print("Enter the 1st course id: ");
                            break;
                        case 2:
                            System.out.print("Enter the 2nd course id: ");
                            break;
                        case 3:
                            System.out.print("Enter the 3rd course id: ");
                            break;
                        default:
                            System.out.print("Enter the " + i + "th course id: ");
                    }

                    String courseId = Constants.IN.nextLine();
                    // if the courseId does not exist in Database.coursesOpen, then it's invalid
                    if (!Database.coursesOpen.containsKey(courseId)) {
                        System.out.println(Constants.INVALID_INPUT);
                    }
                    // if the student does not satisfy all the prerequisites of the courseId, then it's invalid
                    else if (!Validation.satisfyPrerequisites(student, courseId)) {
                        System.out.println(Constants.INVALID_INPUT);
                    }
                    // if the student has taken or is taking any courses in the anti-requisites of the courseId, then
                    // it's invalid
                    else if (!Validation.satisfyAntirerequisites(student, courseId)) {
                        System.out.println(Constants.INVALID_INPUT);
                    }
                    // if the course is selected, then it's invalid
                    else if (courseSelected.contains(courseId)) {
                        System.out.println(Constants.INVALID_INPUT);
                    } else {
                        // update the corresponding course
                        Database.courses.get(courseId).addStudentsWhoAreTaking(student.getUuid());
                        // update courseSelected
                        courseSelected.add(courseId);
                        // update student account
                        student.addCurrentCourses(courseId);
                        // update STUDENT.db
                        Database.appendCurrentCourses(student.getUuid(), courseId);
                        // successfully enroll
                        System.out.println(Constants.COLOR_WRAPPER("Enrollment successfully!", Constants.GREEN_BOLD));
                        break;
                    }
                }
            }
        } else {
            System.out.println(Constants.COLOR_WRAPPER("Enrollment not open yet!", Constants.RED));
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();
        }
    }

}
