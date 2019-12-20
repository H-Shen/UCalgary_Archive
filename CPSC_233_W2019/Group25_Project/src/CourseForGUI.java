import javafx.beans.property.SimpleStringProperty;

/**
 * The {@code CourseForGUI} class wraps the class Course for GUI purpose
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class CourseForGUI implements Comparable<CourseForGUI> {

    /**
     * @Fields Properties of the object
     */
    private final SimpleStringProperty courseId                  = new SimpleStringProperty();
    private final SimpleStringProperty courseName                = new SimpleStringProperty();
    private final SimpleStringProperty courseDescription         = new SimpleStringProperty();
    private final SimpleStringProperty courseUnits               = new SimpleStringProperty();
    private final SimpleStringProperty prerequisites             = new SimpleStringProperty();
    private final SimpleStringProperty antirequisites            = new SimpleStringProperty();
    private final SimpleStringProperty canBeRepeated             = new SimpleStringProperty();
    private final SimpleStringProperty studentsWhoAreTakingCount = new SimpleStringProperty();

    /**
     * Constructor which takes a Course object
     *
     * @param course A Course object
     * @date 2019/04/08
     */
    public CourseForGUI(Course course) {
        setCourseId(course.getCourseId());
        setCourseName(course.getCourseName());
        setCourseDescription(course.getCourseDescription());
        setCourseUnits(course.getCourseUnits());
        setPrerequisites(course.getPrerequisitesAsString());
        setAntirequisites(course.getAntirequisitesAsString());
        setCanBeRepeated(course.getCanBeRepeated());
        setStudentsWhoAreTakingCount(String.valueOf(course.getStudentsWhoAreTaking().size()));
    }

    /**
     * The method defines a 3 way-comparison by the course id's lexicographical order.
     *
     * @param o another CourseForGUI object
     * @return -1 if o cannot be cast to User or its course id is smaller, 0 if the course ids are the same, 1 otherwise.
     * @date 2019/04/08
     */
    @Override
    public int compareTo(CourseForGUI o) {
        if (o != null) {
            return getCourseId().compareTo(o.getCourseId());
        }
        return -1;
    }

    /**
     * Getter method for course id
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
     * @param courseId the courseID to be set
     * @date 2019/04/08
     */
    private void setCourseId(String courseId) {
        this.courseId.set(courseId);
    }

    /**
     * The method returns the courseID as a simple string so it can be displayed in table view
     *
     * @return courseID as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty courseIdProperty() {
        return courseId;
    }

    /**
     * Getter method for course name
     *
     * @return courseName
     * @date 2019/04/08
     */
    public String getCourseName() {
        return courseName.get();
    }

    /**
     * Setter method for course name
     *
     * @param courseName the course name
     * @date 2019/04/08
     */
    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    /**
     * The method returns the courseName as a simple string so it can be displayed in table view
     *
     * @return courseName as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty courseNameProperty() {
        return courseName;
    }

    /**
     * Getter method for course description
     *
     * @return courseDescription
     * @date 2019/04/08
     */
    public String getCourseDescription() {
        return courseDescription.get();
    }

    /**
     * Setter method for course description
     *
     * @param courseDescription the course description
     * @date 2019/04/08
     */
    public void setCourseDescription(String courseDescription) {
        this.courseDescription.set(courseDescription);
    }

    /**
     * The method returns the courseDescription as a simple string so it can be displayed in table view
     *
     * @return courseDescription as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty courseDescriptionProperty() {
        return courseDescription;
    }

    /**
     * Getter method for course units
     *
     * @return courseUnits
     * @date 2019/04/08
     */
    public String getCourseUnits() {
        return courseUnits.get();
    }

    /**
     * Setter method for course units
     *
     * @param courseUnits the course units
     * @date 2019/04/08
     */
    private void setCourseUnits(String courseUnits) {
        this.courseUnits.set(courseUnits);
    }

    /**
     * The method returns the courseUnits as a simple string so it can be displayed in table view
     *
     * @return courseUnits as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty courseUnitsProperty() {
        return courseUnits;
    }

    /**
     * Getter method for prerequisites
     *
     * @return prerequisites
     * @date 2019/04/08
     */
    public String getPrerequisites() {
        return prerequisites.get();
    }

    /**
     * Setter method for prerequisites
     *
     * @param prerequisites the string of prerequisites of a course
     * @date 2019/04/08
     */
    public void setPrerequisites(String prerequisites) {
        this.prerequisites.set(prerequisites);
    }

    /**
     * The method returns the prerequisites as a simple string so it can be displayed in table view
     *
     * @return prerequisites as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty prerequisitesProperty() {
        return prerequisites;
    }

    /**
     * Getter method for antirequisites
     *
     * @return antirequisites
     * @date 2019/04/08
     */
    public String getAntirequisites() {
        return antirequisites.get();
    }

    /**
     * Setter method for antirequisites
     *
     * @param antirequisites the antirequisites of a course
     * @date 2019/04/08
     */
    public void setAntirequisites(String antirequisites) {
        this.antirequisites.set(antirequisites);
    }

    /**
     * The method returns the antirequisites as a simple string so it can be displayed in table view
     *
     * @return antirequisites as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty antirequisitesProperty() {
        return antirequisites;
    }

    /**
     * Getter method for canBeRepeated
     *
     * @return canBeRepeated
     * @date 2019/04/08
     */
    public String getCanBeRepeated() {
        return canBeRepeated.get();
    }

    /**
     * Setter method for canBeRepeated
     *
     * @param canBeRepeated a string shows if the course can be repeated for GPA
     * @date 2019/04/08
     */
    public void setCanBeRepeated(String canBeRepeated) {
        this.canBeRepeated.set(canBeRepeated);
    }

    /**
     * The method returns the canBeRepeated as a simple string so it can be displayed in table view
     *
     * @return canBeRepeated as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty canBeRepeatedProperty() {
        return canBeRepeated;
    }

    /**
     * Getter method for number of students taking a course
     *
     * @return number of students enrolled in course
     * @date 2019/04/08
     */
    public String getStudentsWhoAreTakingCount() {
        return studentsWhoAreTakingCount.get();
    }

    /**
     * Setter method for number of students taking a course
     *
     * @param studentsWhoAreTakingCount number of students enrolled in course
     * @date 2019/04/08
     */
    private void setStudentsWhoAreTakingCount(String studentsWhoAreTakingCount) {
        this.studentsWhoAreTakingCount.set(studentsWhoAreTakingCount);
    }

    /**
     * The method returns number of students taking a course as a simple string so it can be displayed in table view
     *
     * @return number of students enrolled in course as a simple string
     * @date 2019/04/08
     */
    public SimpleStringProperty studentsWhoAreTakingCountProperty() {
        return studentsWhoAreTakingCount;
    }
}
