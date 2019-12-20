import javafx.beans.property.SimpleStringProperty;

/**
 * The {@code StudentWithGradeForGUI} class is defined to store a student and a grade of one of its courses as a pair
 * for GUI purpose.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class StudentWithGradeForGUI {

    /**
     * @Fields Properties and components used in GUI
     */
    private final SimpleStringProperty uuid  = new SimpleStringProperty();
    private final SimpleStringProperty grade = new SimpleStringProperty();

    /**
     * Setter method for grades with uuid for GUI
     *
     * @param uuid the uuid of the student
     * @date 2019/04/08
     */
    public StudentWithGradeForGUI(String uuid) {
        setUuid(uuid);
        setGrade("Not graded");
    }

    /**
     * Getter method for uuid
     *
     * @return uuid
     * @date 2019/04/08
     */
    public String getUuid() {
        return uuid.get();
    }

    /**
     * Setter method for uuid
     *
     * @param uuid the uuid to set
     * @date 2019/04/08
     */
    private void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    /**
     * Turns uuid into a simple string so it can be displayed in the GUI.
     *
     * @return uuid
     * @date 2019/04/08
     */
    public SimpleStringProperty uuidProperty() {
        return uuid;
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
     * @param grade the grade of the course as a string
     * @date 2019/04/08
     */
    public void setGrade(String grade) {
        this.grade.set(grade);
    }

    /**
     * Turns grade into a simple string so it can be displayed by the GUI
     *
     * @return grade
     * @date 2019/04/08
     */
    public SimpleStringProperty gradeProperty() {
        return grade;
    }
}
