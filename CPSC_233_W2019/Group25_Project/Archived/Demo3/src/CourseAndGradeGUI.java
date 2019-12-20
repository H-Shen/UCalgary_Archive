import javafx.beans.property.SimpleStringProperty;

/**
 * @author hshen
 * @version 2019-03-27
 */
public class CourseAndGradeGUI {

    private SimpleStringProperty courseId = new SimpleStringProperty();
    private SimpleStringProperty grade    = new SimpleStringProperty();
    private SimpleStringProperty status   = new SimpleStringProperty();

    public CourseAndGradeGUI(String courseId, String grade, String status) {
        setCourseId(courseId);
        setGrade(grade);
        setStatus(status);
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

    public String getGrade() {
        return grade.get();
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }

    public SimpleStringProperty gradeProperty() {
        return grade;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }
}
