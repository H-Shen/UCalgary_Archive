import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * The {@code CourseInfoEditGUI} class defines all the layout and logic of the window which the admin can edit a course
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class CourseInfoEditGUI implements Initializable {

    /**
     * @Fields Properties and components used in GUI
     */
    @FXML
    private Label                                           courseIdLabel;
    @FXML
    private TextArea                                        courseNameTextArea;
    @FXML
    private TextArea                                        courseDescriptionTextArea;
    @FXML
    private Label                                           courseUnitsLabel;
    @FXML
    private MenuButton                                      canBeRepeatedMenuButton;
    @FXML
    private TableView<CourseForGUI>                         prerequisitesTable;
    @FXML
    private TableColumn<CourseForGUI, SimpleStringProperty> prerequisitesColumn;
    @FXML
    private TableColumn<CourseForGUI, SimpleStringProperty> antirequisitesColumn;
    @FXML
    private TableView<CourseForGUI>                         antirequisitesTable;
    @FXML
    private TextField                                       courseIdAddToListTextField;
    @FXML
    private Label                                           reqForGradLabel;
    @FXML
    private Label                                           reqForInternLabel;

    private String          courseName;
    private String          courseDescription;
    private String          addCourseId;
    private HashSet<String> prerequisitesSet  = new HashSet<>();
    private HashSet<String> antirequisitesSet = new HashSet<>();
    private Course          course;

    /**
     * The method changes the text to display "Yes" when the course is changed to being repeated.
     *
     * @date 2019/04/11
     */
    public void yesMenuItemOnAction() {
        canBeRepeatedMenuButton.setText("YES");
    }

    /**
     * The method changes the text to display "No" when the course is changed to being repeated.
     *
     * @date 2019/04/11
     */
    public void noMenuItemOnAction() {
        canBeRepeatedMenuButton.setText("NO");
    }

    /**
     * The method displays a popup error window if the courses new information is not valid.
     *
     * @return false if info is invalid, otherwise true
     * @date 2019/04/11
     */
    private boolean courseInfoValidation() {
        // validate course name
        if (!Validation.isCourseNameValid(courseName)) {
            Utility.showError("ERROR", "Invalid course name!", "Possible reasons:\n\n" +
                    "The course name is empty.");
            return false;
        }
        // validate course description
        if (!Validation.isCourseDescriptionValid(courseDescription)) {
            Utility.showError("ERROR", "Invalid course description!", "Possible reasons:\n\n" +
                    "The course description is empty.");
            return false;
        }
        return true;
    }

    /**
     * The method sets the course's field to reflect teh changes within the textfield
     * updates the databases to reflect this change
     * then updates Table View's in the GUI to reflect this change
     *
     * @date 2019/04/11
     */
    private void updateCourse() {

        course.setCourseName(courseName);
        course.setCourseDescription(courseDescription);
        course.setCanBeRepeated(canBeRepeatedMenuButton.getText());
        course.setPrerequisites(prerequisitesSet);
        course.setAntirequisites(antirequisitesSet);

        // update Database.courses
        Database.courses.put(course.getCourseId(), course);

        // update Database.coursesForGUI
        for (CourseForGUI i : Database.coursesForGUI) {
            if (i.getCourseId().equals(course.getCourseId())) {
                i.setCourseName(courseName);
                i.setCourseDescription(courseDescription);
                i.setCanBeRepeated(canBeRepeatedMenuButton.getText());
                i.setPrerequisites(course.getPrerequisitesAsString());
                i.setAntirequisites(course.getAntirequisitesAsString());
                // update AdminGUI.courseList concurrently
                for (int j = 0; j < AdminGUI.courseList.size(); ++j) {
                    if (AdminGUI.courseList.get(j).getCourseId().equals(i.getCourseId())) {
                        AdminGUI.courseList.set(j, i);
                        break;
                    }
                }
                break;
            }
        }
    }

    /**
     * The method commits the change of courses to COURSES.db
     *
     * @date 2019/04/11
     */
    private void updateCommit() {
        Database.courseUpdateCommit(course.getCourseId(), Constants.COURSE_FIELD.COURSE_NAME);
        Database.courseUpdateCommit(course.getCourseId(), Constants.COURSE_FIELD.COURSE_DESCRIPTION);
        Database.courseUpdateCommit(course.getCourseId(), Constants.COURSE_FIELD.COURSE_UNITS);
        Database.courseUpdateCommit(course.getCourseId(), Constants.COURSE_FIELD.CAN_BE_REPEATED);
        Database.courseUpdateCommit(course.getCourseId(), Constants.COURSE_FIELD.PREREQUISITES);
        Database.courseUpdateCommit(course.getCourseId(), Constants.COURSE_FIELD.ANTIREQUISITES);
    }

    /**
     * The method pops up a confirmation window, and if the user clicks yes the course updates are committed, logged
     * into log history and an alert window is shown to verify the update to the user.
     *
     * @date 2019/04/11
     */
    public void updateOnAction() {
        if (courseInfoValidation()) {
            boolean ifConfirm = Utility.showConfirmation("Confirmation", null, "Are you sure to create the course?");
            if (!ifConfirm) {
                return;
            }
            updateCourse();
            updateCommit();
            // log
            Log.updateACourse(new Timestamp(System.currentTimeMillis()), MenuGUI.uuid, course.getCourseId());
            // prompt a message to show the course is updated successfully
            Utility.showAlert("Notification", null, "Course updated successfully!");
        }
    }

    /**
     * The method defines an action which removes the selected item in the table view.
     *
     * @apiNote a bug is that I cannot deselect an item from another table thus selected items in both tables will be
     * removed
     * @date 2019/3/20
     */
    public void removeSelectedOnAction() {
        CourseForGUI temp;
        temp = prerequisitesTable.getSelectionModel().getSelectedItem();
        if (temp != null) {
            prerequisitesTable.getItems().remove(temp);
            prerequisitesSet.remove(temp.getCourseId());
            updatePrerequisitesTable();
        }
        temp = antirequisitesTable.getSelectionModel().getSelectedItem();
        if (temp != null) {
            antirequisitesTable.getItems().remove(temp);
            antirequisitesSet.remove(temp.getCourseId());
            updateAntirequisitesTable();
        }
    }

    /**
     * The method validates new courseID and shows error popup if invalid
     * error popup indicates why courseID is invalid
     *
     * @return false if the courseID is invalid, otherwise true
     * @date 2019/04/11
     */
    private boolean validateAddCourseId(String addCourseId) {

        if (!Database.courses.containsKey(addCourseId)) {
            Utility.showError("ERROR", "Invalid course id to add!", "Possible reason:\n\n" +
                    "It does not exist in the database!");
            return false;
        }

        if (prerequisitesSet.contains(addCourseId)) {
            Utility.showError("ERROR", "Invalid course id to add!", "Possible reason:\n\n" +
                    "It has been added to the prerequisites already!");
            return false;
        }

        if (antirequisitesSet.contains(addCourseId)) {
            Utility.showError("ERROR", "Invalid course id to add!", "Possible reason:\n\n" +
                    "It has been added to the antirequisites already!");
            return false;
        }

        return true;
    }

    /**
     * The method adds prerequisite updates to a course to an observable list which is used to display the GUI table
     * view.
     *
     * @date 2019/04/11
     */
    private void updatePrerequisitesTable() {
        ObservableList<CourseForGUI> temp = FXCollections.observableArrayList();
        for (String i : prerequisitesSet) {
            for (CourseForGUI j : Database.coursesForGUI) {
                if (j.getCourseId().equals(i)) {
                    temp.add(j);
                    break;
                }
            }
        }
        prerequisitesTable.setItems(temp);
    }

    /**
     * The method adds anti-requisites updates to a course to an observable list which is used to display the GUI table
     * view.
     *
     * @date 2019/04/11
     */
    private void updateAntirequisitesTable() {
        ObservableList<CourseForGUI> temp = FXCollections.observableArrayList();
        for (String i : antirequisitesSet) {
            for (CourseForGUI j : Database.coursesForGUI) {
                if (j.getCourseId().equals(i)) {
                    temp.add(j);
                    break;
                }
            }
        }
        antirequisitesTable.setItems(temp);
    }

    /**
     * The method updates the Table View in gui to reflect any changes to courses.
     *
     * @date 2019/04/11
     */
    public void addToPrerequisitesOnAction() {

        if (validateAddCourseId(addCourseId)) {
            prerequisitesSet.add(addCourseId);
        }
        updatePrerequisitesTable();
    }

    /**
     * The method updates the Table View in gui to reflect any changes to courses.
     *
     * @date 2019/04/11
     */
    public void addToAntirerequisitesOnAction() {

        if (validateAddCourseId(addCourseId)) {
            antirequisitesSet.add(addCourseId);
        }
        updateAntirequisitesTable();
    }

    /**
     * The method gets text from the courseName textfield in update Course
     *
     * @date 2019/04/11
     */
    public void courseNameKeyReleased() {
        courseName = courseNameTextArea.getText();
    }

    /**
     * The method gets text from the courseDescription textfield in update Course.
     *
     * @date 2019/04/11
     */
    public void courseDescriptionKeyReleased() {
        courseDescription = courseDescriptionTextArea.getText();
    }

    /**
     * The method gets text from the courseID textfield in update Course.
     *
     * @date 2019/04/11
     */
    public void courseIdAddToListKeyReleased() {
        addCourseId = courseIdAddToListTextField.getText();
    }

    /**
     * The method resets all text fields to empty.
     *
     * @date 2019/04/11
     */
    public void resetOnAction() {

        // reset course units
        courseUnitsLabel.setText(course.getCourseUnits());

        // reset course name
        courseNameTextArea.setText(course.getCourseName());
        courseName = course.getCourseName();

        // reset course description
        courseDescriptionTextArea.setText(course.getCourseDescription());
        courseDescription = course.getCourseDescription();

        // reset prerequisites and anti-requisites
        prerequisitesSet = course.getPrerequisites();
        updatePrerequisitesTable();
        antirequisitesSet = course.getAntirequisites();
        updateAntirequisitesTable();

        courseIdAddToListTextField.clear();
        addCourseId = "";
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

        // initialize course as a deep copy from Database.courses
        course = new Course(Database.courses.get(AdminGUI.courseIdToEdit));

        // initialize course id
        courseIdLabel.setText(course.getCourseId());

        // initialize reqForGradLabel
        if (Database.optionalCourses.contains(course.getCourseId())) {
            reqForGradLabel.setText("Optional");
        } else {
            reqForGradLabel.setText("Mandatory");
        }

        // initialize reqForInternLabel
        if (Database.optionalCoursesForInternship.contains(course.getCourseId())) {
            reqForInternLabel.setText("Optional");
        } else if (Database.mandatoryCoursesForInternship.contains(course.getCourseId())) {
            reqForInternLabel.setText("Mandatory");
        } else {
            reqForInternLabel.setText("None");
        }

        // initialize all other information
        resetOnAction();

        // set up the columns in the table
        prerequisitesColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        antirequisitesColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));

        // no drag and drop in prerequisitesTable
        prerequisitesTable.getColumns().addListener((ListChangeListener<TableColumn<CourseForGUI, ?>>) c -> {
            c.next();
            if (c.wasReplaced()) {
                prerequisitesTable.getColumns().clear();
                prerequisitesTable.getColumns().add(prerequisitesColumn);
            }
        });

        // no drag and drop in antirequisitesTable
        antirequisitesTable.getColumns().addListener((ListChangeListener<TableColumn<CourseForGUI, ?>>) c -> {
            c.next();
            if (c.wasReplaced()) {
                antirequisitesTable.getColumns().clear();
                antirequisitesTable.getColumns().add(antirequisitesColumn);
            }
        });
    }
}
