import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * The {@code CourseManagementGUI} class defines all the layout and logic of the window which the faculty can manage
 * grades of all students who are taking the faculty's courses.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class CourseManagementGUI implements Initializable {

    public static ObservableList<StudentWithGradeForGUI>                    result;
    /**
     * @Fields Properties and components used in GUI
     */
    @FXML
    private       TableView<CourseForGUI>                                   coursesCurrentlyTeachingTable;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty>           courseIdColumn;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty>           countOfStudentsColumn;
    @FXML
    private       TableView<StudentWithGradeForGUI>                         coursesGraderTable;
    @FXML
    private       TableColumn<StudentWithGradeForGUI, SimpleStringProperty> uuidColumn;
    @FXML
    private       TableColumn<StudentWithGradeForGUI, SimpleStringProperty> gradeColumn;
    @FXML
    private       TextArea                                                  fullNameTextArea;
    @FXML
    private       TextArea                                                  emailAddressTextArea;
    @FXML
    private       TextField                                                 gradeTextField;
    private       String                                                    grade;

    /**
     * The method gets text from the grade textfield in update Course.
     *
     * @date 2019/04/11
     */
    public void gradeTextFieldKeyReleased() {
        grade = gradeTextField.getText();
    }

    /**
     * The method validates grade text, and if invalid popups an error window and return, otherwise the new grade will
     * be set.
     *
     * @date 2019/04/11
     */
    public void gradeUpdateOnAction() {
        // validate grade input
        if (!Validation.isGradeValid(grade)) {
            Utility.showError("ERROR", "Invalid course units!", "Possible reasons:\n\n" +
                    "1. It is not a number.\n" +
                    "2. It contains redundant leading zeroes or trailing zeroes, such as '4.0', '3.0'\n" +
                    "3. It is not greater than 0.\n" +
                    "4. It is greater than 4.\n" +
                    "5. It contains more than one decimal, such as '3.91', '2.71'");
            return;
        }
        // check if a student is selected
        StudentWithGradeForGUI temp = coursesGraderTable.getSelectionModel().getSelectedItem();
        if (temp != null) {
            // update its grade
            temp.setGrade(grade);
        }
    }

    /**
     * The method gets text from the courseDescription textfield in update Course.
     *
     * @date 2019/04/11
     */
    public void resetSelectedOnAction() {
        StudentWithGradeForGUI temp = coursesGraderTable.getSelectionModel().getSelectedItem();
        if (temp != null) {
            temp.setGrade("Not graded");
        }
    }

    /**
     * The method defines the event when the user clicks the 'Submit' button.
     *
     * @date 2019/04/11
     */
    public void submitOnAction() {
        boolean ifConfirm = Utility.showConfirmation(
                "Confirmation",
                "Are you sure to update the grades?",
                "Be careful, once the grade is archived, it cannot be modified by the faculty anymore!");
        if (!ifConfirm) {
            return;
        }

        // make sure that exact one course is selected
        CourseForGUI selectedCourse = coursesCurrentlyTeachingTable.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            ObservableList<StudentWithGradeForGUI> temp = coursesGraderTable.getItems();
            for (StudentWithGradeForGUI i : temp) {
                if (!"Not graded".equals(i.getGrade())) {

                    // remove the graded student from the course.studentsWhoAreTaking
                    Database.courses.get(selectedCourse.getCourseId()).removeStudentsWhoAreTaking(i.getUuid());

                    // remove the course from student.removeFromCurrentCoursesList
                    Student student = (Student) Database.accounts.get(i.getUuid());
                    student.removeFromCurrentCoursesList(selectedCourse.getCourseId());
                    student.addTakenCourses(new CourseAndGrade(selectedCourse.getCourseId(), i.getGrade()));

                    // update STUDENTS.db
                    Database.updateStudentTakenCourses(i.getUuid(), student.getTakenCoursesListAsString());
                    Database.updateStudentCurrentCourses(i.getUuid(), student.getCurrentCoursesListAsString());

                    // Log
                    Log.gradedAStudent(new Timestamp(System.currentTimeMillis()), MenuGUI.uuid, i.getUuid());
                }

            }
            // update coursesGraderTable
            coursesGraderTable.setItems(getDataForCoursesGraderTable(selectedCourse.getCourseId()));

            // update CoursesCurrentlyTeaching
            coursesCurrentlyTeachingTable.setItems(getDataForCoursesCurrentlyTeaching(MenuGUI.uuid));

            // prompt a successful operation
            Utility.showAlert("Notification", null, "Students graded successfully!");

        } else {
            // prompt an error message
            Utility.showError("Error", null, "No course is selected!");
        }
    }

    /**
     * The method creates an observable list so that the GUI can reflect the students and their grades.
     *
     * @date 2019/04/11
     */
    private ObservableList<StudentWithGradeForGUI> getDataForCoursesGraderTable(String courseId) {
        ObservableList<StudentWithGradeForGUI> result = FXCollections.observableArrayList();
        HashSet<String>                        temp   = Database.courses.get(courseId).getStudentsWhoAreTaking();
        for (String i : temp) {
            result.add(new StudentWithGradeForGUI(i));
        }
        return result;
    }

    /**
     * The method creates an observable list so the GUI can reflect the faculty and the courses they teach.
     *
     * @date 2019/04/11
     */
    private ObservableList<CourseForGUI> getDataForCoursesCurrentlyTeaching(String uuid) {
        ObservableList<CourseForGUI> result  = FXCollections.observableArrayList();
        Faculty                      faculty = (Faculty) Database.accounts.get(uuid);
        ArrayList<String>            tempArr = faculty.getCoursesTeaching();
        for (String i : tempArr) {
            result.add(new CourseForGUI(Database.courses.get(i)));
        }
        return result;
    }

    /**
     * The method initializes the controller class.
     *
     * @param url Uniform Resource Locator
     * @param rb  Resource Bundle
     * @date 2019/04/11
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // set up the columns in coursesCurrentlyTeachingTable
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        countOfStudentsColumn.setCellValueFactory(new PropertyValueFactory<>("studentsWhoAreTakingCount"));

        // set up the columns in coursesGraderTable
        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // import data to coursesCurrentlyTeachingTable
        coursesCurrentlyTeachingTable.setItems(getDataForCoursesCurrentlyTeaching(MenuGUI.uuid));

        // no drag and drop for coursesCurrentlyTeachingTable
        coursesCurrentlyTeachingTable.getColumns().addListener((ListChangeListener<TableColumn<CourseForGUI, ?>>) c -> {
            c.next();
            if (c.wasReplaced()) {
                coursesCurrentlyTeachingTable.getColumns().clear();
                coursesCurrentlyTeachingTable.getColumns().addAll(Arrays.asList(courseIdColumn, countOfStudentsColumn));
            }
        });

        // when an item in coursesCurrentlyTeachingTable is selected, coursesGraderTable will be
        // concurrently updated
        coursesCurrentlyTeachingTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected
            CourseForGUI temp = coursesCurrentlyTeachingTable.getSelectionModel().getSelectedItem();
            if (temp != null) {
                // update coursesGraderTable
                coursesGraderTable.setItems(getDataForCoursesGraderTable(temp.getCourseId()));
            }
        });

        // no drag and drop for coursesGraderTable
        coursesGraderTable.getColumns().addListener((ListChangeListener<TableColumn<StudentWithGradeForGUI, ?>>) c -> {
            c.next();
            if (c.wasReplaced()) {
                coursesGraderTable.getColumns().clear();
                coursesGraderTable.getColumns().addAll(Arrays.asList(uuidColumn, gradeColumn));
            }
        });

        // when an item in coursesGraderTable is selected, fullNameTextArea and emailAddressTextArea will be
        // concurrently updated
        coursesGraderTable.getSelectionModel().selectedItemProperty().addListener((observableValue,
                                                                                   oldValue, newValue) -> {
            //Check whether item is selected
            StudentWithGradeForGUI temp = coursesGraderTable.getSelectionModel().getSelectedItem();
            if (temp != null) {
                // update fullNameTextArea and emailAddressTextArea
                User tempUser = Database.accounts.get(temp.getUuid());
                fullNameTextArea.setText(tempUser.getFullName());
                emailAddressTextArea.setText(tempUser.getEmailAddress());
            }
        });
    }
}
