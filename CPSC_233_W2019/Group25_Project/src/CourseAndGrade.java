/**
 * The {@code CourseAndGrade} class is defined to store a course and corresponding grade pair.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class CourseAndGrade implements Comparable<CourseAndGrade> {

    /**
     * @Fields Properties of the object
     */
    private String courseId;
    private String grade;

    /**
     * Constructor that sets the course id and course grade.
     *
     * @param courseId a String containing the course id.
     * @param grade    a String containing the course grade.
     * @date 2019/04/08
     */
    public CourseAndGrade(String courseId, String grade) {
        setCourseId(courseId);
        setGrade(grade);
    }

    /**
     * Copy Constructor
     *
     * @param other another CourseAndGrade object
     * @date 2019/04/08
     */
    public CourseAndGrade(CourseAndGrade other) {
        this(other.getCourseId(), other.getGrade());
    }

    /**
     * Getter of the course id.
     *
     * @return a string contains the course id
     * @date 2019/04/08
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Setter the course id. The course id must already exist in the database.
     *
     * @param courseId the course id to set
     * @date 2019/04/08
     */
    public void setCourseId(String courseId) {
        if (Database.courses.containsKey(courseId)) {
            this.courseId = courseId;
        }
    }

    /**
     * Getter of the course grade.
     *
     * @return a string contains the grade.
     * @date 2019/04/08
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Setter of the course grade.
     *
     * @param grade the course grade must already exist in the database.
     * @date 2019/04/08
     */
    public void setGrade(String grade) {
        if (Validation.isGradeValid(grade)) {
            this.grade = grade;
        }
    }

    /**
     * The method defines 3 way-comparison by the course id's lexicographical order.
     *
     * @param o another CourseAndGrade object
     * @return -1 if o cannot be cast to User or its course id is smaller, 0 if the course ids are the same, 1 otherwise.
     * @date 2019/04/08
     */
    @Override
    public int compareTo(CourseAndGrade o) {
        if (o != null) {
            return getCourseId().compareTo(o.getCourseId());
        }
        return -1;
    }

    /**
     * The method defines the result after a CourseAndGrade object is being printed.
     *
     * @return a string represents the CourseAndGrade object
     * @date 2019/04/08
     */
    @Override
    public String toString() {
        return getCourseId() + " : " + getGrade();
    }
}
