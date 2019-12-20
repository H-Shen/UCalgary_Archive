import javafx.beans.property.SimpleStringProperty;

/**
 * a class simliar to class Course but for GUI purpose
 *
 * @author
 * @version 2019-03-25
 */
public class CourseForGUI implements Comparable<CourseForGUI> {

    /**
     * Fields
     */
    private SimpleStringProperty courseId                  = new SimpleStringProperty();
    private SimpleStringProperty courseName                = new SimpleStringProperty();
    private SimpleStringProperty courseDescription         = new SimpleStringProperty();
    private SimpleStringProperty courseUnits               = new SimpleStringProperty();
    private SimpleStringProperty prerequisites             = new SimpleStringProperty();
    private SimpleStringProperty antirequisites            = new SimpleStringProperty();
    private SimpleStringProperty canBeRepeated             = new SimpleStringProperty();
    private SimpleStringProperty studentsWhoAreTakingCount = new SimpleStringProperty();

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
     * 3 way-comparison by the course id's lexicographical order.
     *
     * @param o
     * @return -1 if o cannot be cast to User or its course id is smaller, 0 if the course ids are the same, 1 otherwise.
     */
    @Override
    public int compareTo(CourseForGUI o) {
        if (o != null) {
            return getCourseId().compareTo(o.getCourseId());
        }
        return -1;
    }

    public String getCourseId() {
        return courseId.get();
    }

    public void setCourseId(String courseId) {
        this.courseId.set(courseId);
    }

    public SimpleStringProperty courseIdProperty() {
        return courseId;
    }

    public String getCourseName() {
        return courseName.get();
    }

    public void setCourseName(String courseName) {
        this.courseName.set(courseName);
    }

    public SimpleStringProperty courseNameProperty() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription.get();
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription.set(courseDescription);
    }

    public SimpleStringProperty courseDescriptionProperty() {
        return courseDescription;
    }

    public String getCourseUnits() {
        return courseUnits.get();
    }

    public void setCourseUnits(String courseUnits) {
        this.courseUnits.set(courseUnits);
    }

    public SimpleStringProperty courseUnitsProperty() {
        return courseUnits;
    }

    public String getPrerequisites() {
        return prerequisites.get();
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites.set(prerequisites);
    }

    public SimpleStringProperty prerequisitesProperty() {
        return prerequisites;
    }

    public String getAntirequisites() {
        return antirequisites.get();
    }

    public void setAntirequisites(String antirequisites) {
        this.antirequisites.set(antirequisites);
    }

    public SimpleStringProperty antirequisitesProperty() {
        return antirequisites;
    }

    public String getCanBeRepeated() {
        return canBeRepeated.get();
    }

    public void setCanBeRepeated(String canBeRepeated) {
        this.canBeRepeated.set(canBeRepeated);
    }

    public SimpleStringProperty canBeRepeatedProperty() {
        return canBeRepeated;
    }

    public String getStudentsWhoAreTakingCount() {
        return studentsWhoAreTakingCount.get();
    }

    public void setStudentsWhoAreTakingCount(String studentsWhoAreTakingCount) {
        this.studentsWhoAreTakingCount.set(studentsWhoAreTakingCount);
    }

    public SimpleStringProperty studentsWhoAreTakingCountProperty() {
        return studentsWhoAreTakingCount;
    }
}
