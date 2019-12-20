import javafx.beans.property.SimpleStringProperty;

/**
 * The {@code CourseEnrollmentGUI} class is defined to store a course and a faculty's full name who is teaching it as a
 * pair for GUI purpose
 *
 * @author Group 25
 * @date 2019/04/12
 */
public class CourseEnrollmentGUI {

    /**
     * @Fields Properties of the object
     */
    private final SimpleStringProperty courseId        = new SimpleStringProperty();
    private final SimpleStringProperty facultyFullName = new SimpleStringProperty();
    private final SimpleStringProperty facultyUuid     = new SimpleStringProperty();

    /**
     * Constructor which takes a string of course id, a string of faculty full name, a string of faculty
     * uuid.
     *
     * @param courseId        the course id
     * @param facultyFullName the full name of faculty
     * @param facultyUuid     the uuid of faculty
     * @date 2019/04/12
     */
    public CourseEnrollmentGUI(String courseId, String facultyFullName, String facultyUuid) {
        setCourseId(courseId);
        setFacultyFullName(facultyFullName);
        setFacultyUuid(facultyUuid);
    }

    /**
     * Getter method for course id
     *
     * @return courseID
     * @date 2019/04/12
     */
    public String getCourseId() {
        return courseId.get();
    }

    /**
     * Setter for courseID
     *
     * @param courseId the courseID to be set
     * @date 2019/04/12
     */
    public void setCourseId(String courseId) {
        this.courseId.set(courseId);
    }

    /**
     * The method returns the courseID as a simple string so it can be displayed in table view
     *
     * @return courseID as a simple string
     * @date 2019/04/12
     */
    public SimpleStringProperty courseIdProperty() {
        return courseId;
    }

    /**
     * Getter method for the full name of the faculty
     *
     * @return a string represents the full name of the faculty
     * @date 2019/04/12
     */
    public String getFacultyFullName() {
        return facultyFullName.get();
    }

    /**
     * Setter for the full name of the faculty
     *
     * @param facultyFullName the full name of the faculty
     * @date 2019/04/12
     */
    public void setFacultyFullName(String facultyFullName) {
        this.facultyFullName.set(facultyFullName);
    }

    /**
     * The method returns the full name of the faculty as a simple string so it can be displayed in table view
     *
     * @return the full name of the faculty as a simple string
     * @date 2019/04/12
     */
    public SimpleStringProperty facultyFullNameProperty() {
        return facultyFullName;
    }

    /**
     * Getter method of the uuid of the faculty
     *
     * @return the uuid of the faculty
     * @date 2019/04/12
     */
    public String getFacultyUuid() {
        return facultyUuid.get();
    }

    /**
     * Setter for the full name of the faculty
     *
     * @param facultyUuid a string represents the uuid of the faculty
     * @date 2019/04/12
     */
    public void setFacultyUuid(String facultyUuid) {
        this.facultyUuid.set(facultyUuid);
    }

    /**
     * The method returns the uuid of the faculty as a simple string so it can be displayed in table view
     *
     * @return the uuid of the faculty as a simple string
     * @date 2019/04/12
     */
    public SimpleStringProperty facultyUuidProperty() {
        return facultyUuid;
    }
}
