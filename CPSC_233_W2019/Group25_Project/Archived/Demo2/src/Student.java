import java.util.ArrayList;

public class Student extends User {

    private ArrayList<CourseAndGrade> takenCoursesList   = new ArrayList<>();
    private ArrayList<String>         currentCoursesList = new ArrayList<>();

    public Student() {
        setRole(Constants.ROLE[2]);
    }

    // getter
    public ArrayList<CourseAndGrade> getTakenCoursesList() {
        ArrayList<CourseAndGrade> result = new ArrayList<>();
        for (CourseAndGrade i : takenCoursesList) {
            result.add(new CourseAndGrade(i));
        }
        return result;
    }

    public ArrayList<String> getCurrentCoursesList() {
        ArrayList<String> result = new ArrayList<>();
        for (String i : currentCoursesList) {
            result.add(i);
        }
        return result;
    }

    // adder
    public void addTakenCourses(CourseAndGrade courseAndGrade) {
        if (takenCoursesList == null) {
            takenCoursesList = new ArrayList<>();
        }
        takenCoursesList.add(new CourseAndGrade(courseAndGrade));
    }

    public void addCurrentCourses(String courseId) {
        if (currentCoursesList == null) {
            currentCoursesList = new ArrayList<>();
        }
        currentCoursesList.add(courseId);
    }
}
