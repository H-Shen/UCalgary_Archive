import java.util.ArrayList;

/**
 * The {@code Student} class inherits class User and contains methods that initializes a student user.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class Student extends User {

    /**
     * @Fields the properties of the object
     */
    private ArrayList<CourseAndGrade> takenCoursesList   = new ArrayList<>();
    private ArrayList<String>         currentCoursesList = new ArrayList<>();

    /**
     * The default constructor of Student class, it sets the property of role to 'STUDENT'.
     *
     * @date 2019/04/08
     */
    public Student() {
        setRole(Constants.ROLE[2]);
    }

    /**
     * The method removes the current courses from the list.
     *
     * @param courseId a string contains the course id that is removed
     * @date 2019/04/08
     */
    public void removeFromCurrentCoursesList(String courseId) {
        currentCoursesList.remove(courseId);
    }

    /**
     * The method gets the courses the Student has taken as a string.
     *
     * @return a string contains all the courses that the student taken, separated by single space character
     * @date 2019/04/08
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
     * The method gets the courses the student is currently taking as a string.
     *
     * @return a string contains all the courses the student is taking
     * @date 2019/04/08
     */
    public String getCurrentCoursesListAsString() {
        return String.join(" ", currentCoursesList);
    }

    /**
     * The method gets the courses the student has taken as an arrayList.
     *
     * @return an arrayList that contains the courses taken
     * @date 2019/04/08
     */
    public ArrayList<CourseAndGrade> getTakenCoursesList() {
        ArrayList<CourseAndGrade> result = new ArrayList<>();
        for (CourseAndGrade i : takenCoursesList) {
            result.add(new CourseAndGrade(i));
        }
        return result;
    }

    /**
     * The method gets the courses the student is taking as an arrayList.
     *
     * @return an arrayList that contains the courses taking.
     * @date 2019/04/08
     */
    public ArrayList<String> getCurrentCoursesList() {
        return new ArrayList<>(currentCoursesList);
    }

    /**
     * The method adds the courses that the student have taken into an arrayList.
     *
     * @param courseAndGrade the course(with grade) that the student has taken
     * @date 2019/04/08
     */
    public void addTakenCourses(CourseAndGrade courseAndGrade) {
        if (takenCoursesList == null) {
            takenCoursesList = new ArrayList<>();
        }
        takenCoursesList.add(new CourseAndGrade(courseAndGrade));
    }

    /**
     * The method adds the courses that the student have are taking into an arrayList.
     *
     * @param courseId the course that the student is taking
     * @date 2019/04/08
     */
    public void addCurrentCourses(String courseId) {
        if (currentCoursesList == null) {
            currentCoursesList = new ArrayList<>();
        }
        currentCoursesList.add(courseId);
    }
}
