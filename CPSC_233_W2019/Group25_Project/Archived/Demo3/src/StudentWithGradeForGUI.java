import javafx.beans.property.SimpleStringProperty;

/**
 * @author hshen
 * @version 2019-03-28
 */
public class StudentWithGradeForGUI {

    private SimpleStringProperty uuid  = new SimpleStringProperty();
    private SimpleStringProperty grade = new SimpleStringProperty();

    public StudentWithGradeForGUI(String uuid) {
        setUuid(uuid);
        setGrade("Not graded");
    }

    public String getUuid() {
        return uuid.get();
    }

    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    public SimpleStringProperty uuidProperty() {
        return uuid;
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
}
