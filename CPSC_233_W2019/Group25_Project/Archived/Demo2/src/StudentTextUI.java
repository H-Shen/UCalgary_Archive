import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * The part of TextUI for Students. The GUI design will refer its logic.
 */

public class StudentTextUI extends TextUI {

    public StudentTextUI(String uuid) {

        super(uuid);
        Database.initializeAcademicRequirements();
        Database.initializeStudentInformation(uuid);
        Database.initializeInternshipRequirements();

        Student user = (Student) Database.accounts.get(uuid);
        String  select;

        while (true) {

            System.out.println(Constants.COLOR_WRAPPER("STUDENT PANEL", Constants.WHITE_BOLD));
            System.out.println();

            System.out.println(" 1. List all available courses");
            System.out.println(" 2. List the academic requirement");
            System.out.println(" 3. List the courses currently take");
            System.out.println(" 4. List the grades of courses taken");
            System.out.println(" 5. Search a course");
            System.out.println(" 6. Courses enrollment");
            System.out.println(" 7. Modify personal information");
            System.out.println(" 8. Logout");

            System.out.println();
            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();
            System.out.println();
            switch (select) {

                case "1":
                    listAllCourses();
                    break;

                case "2":

                    coursesNeedToTakeBeforeGraduation(user);
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    coursesNeedToTakeBeforeInternship(user);
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();

                    break;

                case "3":

                    coursesTaking(user);
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                case "4":

                    coursesTaken(user);
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                case "5":

                    searchCourses();
                    System.out.println();
                    break;

                case "6":

                    courseEnrollment();
                    break;

                case "7":

                    modifyAccountForNonAdmin(user);
                    break;

                case "8":
                    return;

                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
            }
        }

    }

    public static void prettyPrint(ArrayList<String> A) {
        final int MAX_COURSE_ID_A_LINE = 6;
        int       count                = 0;
        for (int i = 0; i < A.size(); ++i) {
            System.out.printf("%8s", "    " + A.get(i));
            ++count;
            if (count == MAX_COURSE_ID_A_LINE) {
                System.out.println();
                count = 0;
            }
        }
        if (count != 0) {
            System.out.println();
        }
    }

    public static void coursesTaken(Student student) {

        ArrayList<CourseAndGrade> temp = student.getTakenCoursesList();
        System.out.println("These courses have been taken:");
        System.out.println();

        for (CourseAndGrade i : temp) {
            System.out.println("    Course ID: " + i.getCourseId());
            System.out.println("        Grade: " + i.getGrade());
            System.out.println();
        }
    }

    public static void coursesTaking(Student student) {

        ArrayList<String> temp = student.getCurrentCoursesList();
        System.out.println("You are currently taking these courses:");
        System.out.println();
        prettyPrint(temp);
        System.out.println();
    }

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

    // Control the logic that students enroll or drop courses
    public static void courseEnrollment() {
        // TODO
        if (User.getEnrollmentStatus().equals("OPEN")) {

            System.out.println();
            int numberOfCourses = 0;
            while (numberOfCourses == 0) {
                System.out.print("Number of courses: (must > 0 and must <= " + Constants.MAXIMUM_COURSES_TO_SELECT +
                        ")");
                String temp = Constants.IN.nextLine();
                for (int i = 1; i <= Constants.MAXIMUM_COURSES_TO_SELECT; ++i) {
                    if (String.valueOf(i).equals(temp)) {
                        numberOfCourses = Integer.parseInt(temp);
                        break;
                    }
                }
            }


        } else {
            System.out.println("Enrollment not open yet!");
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();
        }

        // string can be split into course ids. Like 'CPSC233 CPSC213 CPSC355'

        // must be legal course list

        // no duplicates

        // satisfy preq

        // no violations of antireq

    }

}
