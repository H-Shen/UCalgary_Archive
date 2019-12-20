/**
 * The {@code CourseAndGrade} class is defined to store a course and corresponding grade pair.
 */
public class CourseAndGrade implements Comparable<CourseAndGrade> {

    private String courseId;
    private String grade;

    /**
     * Constructor that sets the course id and course grade.
     *
     * @param courseId a String containing the course id.
     * @param grade    a String containing the course grade.
     */
    public CourseAndGrade(String courseId, String grade) {
        setCourseId(courseId);
        setGrade(grade);
    }

    /**
     * Copy Constructor
     *
     * @param other
     */
    public CourseAndGrade(CourseAndGrade other) {
        this(other.getCourseId(), other.getGrade());
    }

    /**
     * Gets the course id.
     *
     * @return A String containing the course id.
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Sets the course id.
     * The couse id must already exist in the database.
     *
     * @return a String containing the course id.
     */
    public void setCourseId(String courseId) {
        if (Database.courses.containsKey(courseId)) {
            this.courseId = courseId;
        }
    }

    /**
     * Gets the course grade.
     *
     * @return A String containing the grade.
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Sets the course grade.
     * The couse grade must already exist in the database.
     *
     * @return a String containing the course grade.
     */
    public void setGrade(String grade) {
        if (Validation.isGradeValid(grade)) {
            this.grade = grade;
        }
    }

    /**
     * 3 way-comparison by the course id's lexicographical order.
     *
     * @param o
     * @return -1 if o cannot be cast to User or its course id is smaller, 0 if the course ids are the same, 1 otherwise.
     */
    @Override
    public int compareTo(CourseAndGrade o) {
        if (o != null) {
            return getCourseId().compareTo(o.getCourseId());
        }
        return -1;
    }

    @Override
    public String toString() {
        return getCourseId() + " : " + getGrade();
    }
}
