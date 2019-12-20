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
 * The {@code CourseAddGUI} class defines all the layout and logic of the window which the user can add a course.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class CourseAddGUI implements Initializable {

    /**
     * @Fields Properties and components used in GUI
     */
    private final HashSet<String>                                 prerequisitesSet  = new HashSet<>();
    private final HashSet<String>                                 antirequisitesSet = new HashSet<>();
    @FXML
    private       TextArea                                        courseNameTextArea;
    @FXML
    private       TextArea                                        courseDescriptionTextArea;
    @FXML
    private       TextField                                       courseUnitsTextField;
    @FXML
    private       TextField                                       courseIdTextField;
    @FXML
    private       MenuButton                                      canBeRepeatedMenuButton;
    @FXML
    private       TableView<CourseForGUI>                         prerequisitesTable;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> prerequisitesColumn;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> antirequisitesColumn;
    @FXML
    private       TableView<CourseForGUI>                         antirequisitesTable;
    @FXML
    private       TextField                                       courseIdAddToListTextField;
    @FXML
    private       MenuButton                                      internshipReqMenuButton;
    @FXML
    private       MenuButton                                      graduationReqMenuButton;
    private       String                                          courseId;
    private       String                                          courseName;
    private       String                                          courseDescription;
    private       String                                          courseUnits;
    private       String                                          addCourseId;
    private       Course                                          course;

    /**
     * The method changes the button to display "Yes" if course can be repeated.
     *
     * @date 2019/04/08
     */
    public void yesMenuItemOnAction() {
        canBeRepeatedMenuButton.setText("YES");
    }

    /**
     * The method changes the button to display "No" if course can be repeated.
     *
     * @date 2019/04/08
     */
    public void noMenuItemOnAction() {
        canBeRepeatedMenuButton.setText("NO");
    }

    /**
     * The method changes the button to display "Optional" if course is an optional degree requirement.
     *
     * @date 2019/04/08
     */
    public void optionalMenuItemForGraduationOnAction() {
        graduationReqMenuButton.setText("Optional");
    }

    /**
     * The method changes the button to display "Mandatory" if course is an mandatory degree requirement.
     *
     * @date 2019/04/08
     */
    public void mandatoryMenuItemForGraduationOnAction() {
        graduationReqMenuButton.setText("Mandatory");
    }

    /**
     * The method changes the button to display "None" if course is not optional or mandatory for an internship.
     *
     * @date 2019/04/08
     */
    public void noneMenuItemForInternshipOnAction() {
        internshipReqMenuButton.setText("None");
    }

    /**
     * The method changes the button to display "Optional" if course is optional for an internship.
     *
     * @date 2019/04/08
     */
    public void optionalMenuItemForInternshipOnAction() {
        internshipReqMenuButton.setText("Optional");
    }

    /**
     * The method changes the button to display "Mandatory" if course is mandatory for an internship.
     *
     * @date 2019/04/08
     */
    public void mandatoryMenuItemForInternshipOnAction() {
        internshipReqMenuButton.setText("Mandatory");
    }

    /**
     * The method shows a pop-up window if any of the course text fields are invalid,
     *
     * @return false if any of hte fields are invalid, otherwise true
     * @date 2019/04/08
     */
    private boolean courseInfoValidation() {

        // validate course id
        if (!Validation.isCourseIdValid(courseId)) {
            Utility.showError("ERROR", "Invalid course id!", "Possible reasons:\n\n" +
                    "1. It is empty.\n" +
                    "2. A duplicated course id is in the database already.\n" +
                    "3. It contains characters other than uppercase letters and digits.\n" +
                    "4. Its length is greater than 10.\n" +
                    "5. It does not satisfy the format.");
            return false;
        }

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

        // validate course units
        if (!Validation.isCourseUnitsValid(courseUnits)) {
            Utility.showError("ERROR", "Invalid course units!", "Possible reasons:\n\n" +
                    "1. It is not a number.\n" +
                    "2. It contains redundant leading zeroes or trailing zeroes.\n" +
                    "3. It is not greater than 0.\n" +
                    "4. It is greater than 15.");
            return false;
        }

        return true;
    }

    /**
     * The method creates a new course, adds the course and its fields to the database then updates the course table
     * view to reflect this change.
     *
     * @date 2019/04/08
     */
    private void createCourse() {

        course = new Course();
        course.setCourseId(courseId);
        course.setCourseName(courseName);
        course.setCourseUnits(courseUnits);
        course.setCourseDescription(courseDescription);
        course.setCanBeRepeated(canBeRepeatedMenuButton.getText());
        course.setPrerequisites(prerequisitesSet);
        course.setAntirequisites(antirequisitesSet);

        // update Database.courses
        Database.courses.put(courseId, course);

        // update Database.coursesForGUI
        CourseForGUI temp = new CourseForGUI(course);
        Database.coursesForGUI.add(temp);

        // update Database.mandatoryCourses or Database.optionalCourses
        if ("Mandatory".equals(graduationReqMenuButton.getText())) {
            Database.mandatoryCourses.add(courseId);
        } else {
            Database.optionalCourses.add(courseId);
        }

        // update Database.mandatoryCoursesForInternship or Database.optionalCoursesForInternship
        switch (internshipReqMenuButton.getText()) {
            case "Mandatory":
                Database.mandatoryCoursesForInternship.add(courseId);
                break;
            case "Optional":
                Database.optionalCoursesForInternship.add(courseId);
                break;
            default:
                break;
        }

        // update AdminGUI.courseList
        AdminGUI.courseList.add(temp);
    }

    /**
     * The method updates all databases to correctly reflect the new course update.
     *
     * @date 2019/04/08
     */
    private void createCommit() {

        // update COURSES.db
        Database.createCourseCommit(course);

        // update ACADEMIC_REQUIREMENTS.db
        if ("Mandatory".equals(graduationReqMenuButton.getText())) {
            Database.createCourseCommitToRequirements(courseId, true, true);
        } else {
            Database.createCourseCommitToRequirements(courseId, true, false);
        }

        // update INTERNSHIP_REQUIREMENTS.db
        switch (internshipReqMenuButton.getText()) {
            case "Mandatory":
                Database.createCourseCommitToRequirements(courseId, false, true);
                break;
            case "Optional":
                Database.createCourseCommitToRequirements(courseId, false, false);
                break;
            default:
                break;
        }
    }

    /**
     * The method defines an event, when button to create course is click, a confirmation popup will appear, and if
     * clicked yes, then courses will be committed to database, and then the action will be added to a log.
     *
     * @date 2019/04/08
     */
    public void createOnAction() {

        if (courseInfoValidation()) {
            boolean ifConfirm = Utility.showConfirmation("Confirmation", null, "Are you sure to create the course?");
            if (!ifConfirm) {
                return;
            }
            createCourse();
            createCommit();
            // log
            Log.addACourse(new Timestamp(System.currentTimeMillis()), MenuGUI.uuid, courseId);
            Utility.showAlert("Notification", null, "Course created successfully!");
        }
    }

    /**
     * The method defines an event if the user press the 'Remove' button when a selected item is selected
     *
     * @apiNote a bug is that cannot be de-selected an item from another table thus selected items in both tables
     * will be
     * removed
     * @date 2019/3/20
     */
    public void removeSelectedOnAction() {

        CourseForGUI temp;
        temp = antirequisitesTable.getSelectionModel().getSelectedItem();
        if (temp != null) {
            antirequisitesTable.getItems().remove(temp);
            antirequisitesSet.remove(temp.getCourseId());
            updateAntirequisitesTable();
        }
        temp = prerequisitesTable.getSelectionModel().getSelectedItem();
        if (temp != null) {
            prerequisitesTable.getItems().remove(temp);
            prerequisitesSet.remove(temp.getCourseId());
            updatePrerequisitesTable();
        }
    }

    /**
     * The method validates if the courseID is valid to add to the databases and shows an error popup if it is not
     *
     * @param addCourseId the id of the added course
     * @return false if the courseID is invalid, otherwise true
     * @date 2019/04/08
     */
    private boolean validateAddCourseId(String addCourseId) {

        if (!Database.courses.containsKey(addCourseId)) {
            Utility.showError("ERROR", "Invalid course id to add!", "Possible reasons:\n\n" +
                    "It does not exist in the database!");
            return false;
        }

        if (prerequisitesSet.contains(addCourseId)) {
            Utility.showError("ERROR", "Invalid course id to add!", "Possible reasons:\n\n" +
                    "It has been added to the prerequisites already!");
            return false;
        }

        if (antirequisitesSet.contains(addCourseId)) {
            Utility.showError("ERROR", "Invalid course id to add!", "Possible reasons:\n\n" +
                    "It has been added to the antirequisites already!");
            return false;
        }

        return true;
    }

    /**
     * The method updates the prerequisites array to reflect the addition of a course.
     *
     * @date 2019/04/08
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
     * The method updates the antirequisites array to reflect the addition of a course.
     *
     * @date 2019/04/08
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
     * The method updates the prerequisites Table View to reflect the addition of a course.
     *
     * @date 2019/04/08
     */
    public void addToPrerequisitesOnAction() {

        if (validateAddCourseId(addCourseId)) {
            prerequisitesSet.add(addCourseId);
        }
        updatePrerequisitesTable();
    }

    /**
     * The method updates the antirequisites Table View to reflect the addition of a course.
     *
     * @date 2019/04/08
     */
    public void addToAntirerequisitesOnAction() {

        if (validateAddCourseId(addCourseId)) {
            antirequisitesSet.add(addCourseId);
        }
        updateAntirequisitesTable();
    }

    /**
     * The method gets new course's courseId from textfield and assigns to courseId.
     *
     * @date 2019/04/08
     */
    public void courseIdKeyReleased() {
        courseId = courseIdTextField.getText();
    }

    /**
     * The method gets new course's name from textfield and assigns to courseName.
     *
     * @date 2019/04/08
     */
    public void courseNameKeyReleased() {
        courseName = courseNameTextArea.getText();
    }

    /**
     * The method gets new course's description from textfield and assigns to courseDescription.
     *
     * @date 2019/04/08
     */
    public void courseDescriptionKeyReleased() {
        courseDescription = courseDescriptionTextArea.getText();
    }

    /**
     * The method gets new course's courseUnits from textfield and assigns to courseUnits.
     *
     * @date 2019/04/08
     */
    public void courseUnitsKeyReleased() {
        courseUnits = courseUnitsTextField.getText();
    }

    /**
     * The method gets new course id and from the courseIdAddToListTextField and assigns to addCourseId.
     *
     * @date 2019/04/08
     */
    public void courseIdAddToListKeyReleased() {
        addCourseId = courseIdAddToListTextField.getText();
    }

    /**
     * The method resets all the text fields in the add course window and updates the tables to reflect this reset.
     *
     * @date 2019/04/08
     */
    public void resetOnAction() {
        courseIdTextField.clear();
        courseIdTextField.setPromptText("eg: \"CPSC217\"");
        courseId = "";
        courseNameTextArea.clear();
        courseName = "";
        courseDescriptionTextArea.clear();
        courseDescription = "";
        courseUnitsTextField.clear();
        courseUnits = "";
        courseUnitsTextField.setPromptText("eg: \"1.5\", \"3\"");
        canBeRepeatedMenuButton.setText("YES");
        courseIdAddToListTextField.clear();
        addCourseId = "";
        prerequisitesSet.clear();
        antirequisitesSet.clear();
        graduationReqMenuButton.setText("Optional");
        internshipReqMenuButton.setText("None");
        updatePrerequisitesTable();
        updateAntirequisitesTable();
    }

    /**
     * The method initialize the controller class.
     *
     * @param url Uniform Resource Locator
     * @param rb  Resource Bundle
     * @date 2019/04/08
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // reset all fields in the layout
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
