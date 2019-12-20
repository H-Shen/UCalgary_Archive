import java.util.ArrayList;

/**
 * The {@code Student} class inherits class User and contains methods that initializes a student user.
 */
public class Student extends User {

    private ArrayList<CourseAndGrade> takenCoursesList   = new ArrayList<>();
    private ArrayList<String>         currentCoursesList = new ArrayList<>();

    /**
     * Default constructor of the student class.
     */
    public Student() {
        setRole(Constants.ROLE[2]);
    }

    /**
     * Remove the current courses from teh list.
     *
     * @param courseId a String containing the course id that is removed.
     */
    public void removeFromCurrentCoursesList(String courseId) {
        currentCoursesList.remove(courseId);
    }

    /**
     * Gets the courses the Student has taken as a String.
     *
     * @return a String containing all the courses taken.
     */
    public String getTakenCoursesListAsString() {
        ArrayList<String> result = new ArrayList<>();
        for (CourseAndGrade i : takenCoursesList) {
            result.add(i.getCourseId());
            result.add(i.getGrade());
        }
        return String.join(" ", result);
    }

    /**
     * Gets the courses the student is currently taking as a String.
     *
     * @return a String containing all the courses the student is taking.
     */
    public String getCurrentCoursesListAsString() {
        return String.join(" ", currentCoursesList);
    }

    /**
     * Gets the courses the student has taken as an arrayList.
     *
     * @return an arrayList that contains the courses taken.
     */
    public ArrayList<CourseAndGrade> getTakenCoursesList() {
        ArrayList<CourseAndGrade> result = new ArrayList<>();
        for (CourseAndGrade i : takenCoursesList) {
            result.add(new CourseAndGrade(i));
        }
        return result;
    }

    /**
     * Gets the courses the student is taking as an arrayList.
     *
     * @return an arrayList that contains the courses taking.
     */
    public ArrayList<String> getCurrentCoursesList() {
        ArrayList<String> result = new ArrayList<>();
        for (String i : currentCoursesList) {
            result.add(i);
        }
        return result;
    }

    /**
     * Adds the courses that the student have taken into an arrayList.
     *
     * @param courseAndGrade the course(with grade) that the student has taken.
     */
    public void addTakenCourses(CourseAndGrade courseAndGrade) {
        if (takenCoursesList == null) {
            takenCoursesList = new ArrayList<>();
        }
        takenCoursesList.add(new CourseAndGrade(courseAndGrade));
    }

    /**
     * Adds the courses that the student have are taking into an arrayList.
     *
     * @param courseId the course that the student is taking.
     */
    public void addCurrentCourses(String courseId) {
        if (currentCoursesList == null) {
            currentCoursesList = new ArrayList<>();
        }
        currentCoursesList.add(courseId);
    }
}
