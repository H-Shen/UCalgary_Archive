import javafx.beans.property.SimpleStringProperty;

/**
 * The {@code CourseAndGradeGUI} class is defined to store a course and corresponding grade pair for GUI purpose.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class CourseAndGradeGUI {

    /**
     * @Fields Properties of the object
     */
    private final SimpleStringProperty courseId = new SimpleStringProperty();
    private final SimpleStringProperty grade    = new SimpleStringProperty();
    private final SimpleStringProperty status   = new SimpleStringProperty();

    /**
     * Constructor which takes courseId, grade and status
     *
     * @param courseId the course Id used to initialize
     * @param grade    the grade that the student received in the course
     * @param status   whether the course is ongoing or has already been completed
     * @date 2019/04/08
     */
    public CourseAndGradeGUI(String courseId, String grade, String status) {
        setCourseId(courseId);
        setGrade(grade);
        setStatus(status);
    }

    /**
     * Getter for courseID
     *
     * @return courseID
     * @date 2019/04/08
     */
    public String getCourseId() {
        return courseId.get();
    }

    /**
     * Setter for courseID
     *
     * @param courseId the courseId to be set
     * @date 2019/04/08
     */
    private void setCourseId(String courseId) {
        this.courseId.set(courseId);
    }

    /**
     * Returns the courseID as a simple string so it can be displayed in table view
     *
     * @return courseID as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty courseIdProperty() {
        return courseId;
    }

    /**
     * Getter method for grades
     *
     * @return grade
     * @date 2019/04/08
     */
    public String getGrade() {
        return grade.get();
    }

    /**
     * Setter method for grades
     *
     * @param grade to be set
     * @date 2019/04/08
     */
    private void setGrade(String grade) {
        this.grade.set(grade);
    }

    /**
     * The method returns the grade as a simple string so it can be displayed in table view
     *
     * @return grade as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty gradeProperty() {
        return grade;
    }

    /**
     * Getter method for status
     *
     * @return status
     * @date 2019/04/08
     */
    public String getStatus() {
        return status.get();
    }

    /**
     * Setter method for status
     *
     * @param status to be set
     * @date 2019/04/08
     */
    private void setStatus(String status) {
        this.status.set(status);
    }

    /**
     * The method returns the status as a simple string so it can be displayed in table view
     *
     * @return status as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty statusProperty() {
        return status;
    }
}
