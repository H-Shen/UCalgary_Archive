/**
 * The class defined to store a course and corresponding grade pair.
 */
public class CourseAndGrade {

    private String courseId;
    private String grade;

    public CourseAndGrade(String courseId, String grade) {
        this.courseId = courseId;
        this.grade = grade;
    }

    public CourseAndGrade(CourseAndGrade other) {
        this(other.getCourseId(), other.getGrade());
    }

    public String getCourseId() {
        return courseId;
    }

    public String getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return getCourseId() + " : " + getGrade();
    }
}
