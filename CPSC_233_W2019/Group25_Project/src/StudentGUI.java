import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;

/**
 * The {@code StudentGUI} class defines the layout and logic of Student's Panel in GUI.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class StudentGUI implements Initializable {

    /**
     * @Fields Properties and components used in GUI
     */
    @FXML
    private Label                                           infoLabel;
    @FXML
    private TableView<CourseForGUI>                         tableView;
    @FXML
    private TableColumn<CourseForGUI, SimpleStringProperty> courseIdColumn;
    @FXML
    private TableColumn<CourseForGUI, SimpleStringProperty> courseNameColumn;
    @FXML
    private TableColumn<CourseForGUI, SimpleStringProperty> courseUnitsColumn;
    @FXML
    private TableColumn<CourseForGUI, SimpleStringProperty> canBeRepeated;
    @FXML
    private TextArea                                        courseNameTextArea;
    @FXML
    private TextArea                                        courseDescriptionTextArea;
    @FXML
    private TextArea                                        coursePrerequisitesTextArea;
    @FXML
    private TextArea                                        courseAntirequisitesTextArea;
    @FXML
    private Label                                           searchPanel;
    @FXML
    private Label                                           keywordLabel;
    @FXML
    private TextField                                       searchByKeyword;
    @FXML
    private Button                                          go;
    @FXML
    private MenuButton                                      menuButton;
    @FXML
    private Label                                           total;

    private String                       keyword = "";
    private ObservableList<CourseForGUI> courseList;
    private ObservableList<CourseForGUI> coursesRemaining;

    /**
     * The method calls the method searchCourses() when button is pressed.
     *
     * @date 2019/04/08
     */
    public void searchCoursesOnAction() {
        searchCourses();
    }

    /**
     * The method sets the search fields to be invisible then sets a label that indicates whether each course is
     * optional or mandatory updates the table view to have courses remaining for the students degree.
     *
     * @date 2019/04/08
     */
    public void academicRequirementsOnAction() {

        // set the visibility and the contents
        searchPanel.setText("COURSE PROPERTY");
        keywordLabel.setText("Mandatory / Optional:" + "\n" + "Mandatory / Optional for internship:");
        searchByKeyword.setVisible(false);
        menuButton.setVisible(false);
        go.setVisible(false);

        // update the courses list of academic requirements to table view
        tableView.setItems(coursesRemaining);

        // update stats info
        total.setText("TOTAL: " + coursesRemaining.size());
    }

    /**
     * The method loads the window for a student's course history when the student presses 'Course History'.
     *
     * @date 2019/04/08
     */
    public void coursesHistoryOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CourseHistoryGUI.fxml"));
            Parent     root       = fxmlLoader.load();
            Stage      stage      = new Stage();
            stage.setTitle("Course History");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(MenuGUI.subStage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The method loads a window with account information when accountSetting button is clicked
     *
     * @date 2019/04/08
     */
    public void accountSettingsOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserInfoGUI.fxml"));
            Parent     root       = fxmlLoader.load();
            Stage      stage      = new Stage();
            stage.setTitle("Account Information");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(MenuGUI.subStage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The method loads a window with information of enrollment for students when 'Enrollment'button is clicked
     *
     * @date 2019/04/08
     */
    public void enrollmentOnAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StudentEnrollment.fxml"));
            Parent     root       = fxmlLoader.load();
            Stage      stage      = new Stage();
            stage.setTitle("Courses Enrollment");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(MenuGUI.subStage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The method sets menuButton to say "Course ID".
     *
     * @date 2019/04/08
     */
    public void courseIdMenuItemAction() {
        menuButton.setText("Course ID");
    }

    /**
     * The method sets menuButton to say "Course Name".
     *
     * @date 2019/04/08
     */
    public void courseNameMenuItemAction() {
        menuButton.setText("Course Name");
    }

    /**
     * The method sets menuButton to say "Course Description".
     *
     * @date 2019/04/08
     */
    public void courseDescriptionItemAction() {
        menuButton.setText("Course Description");
    }

    /**
     * The method sets menuButton to say "Course Prerequisites".
     *
     * @date 2019/04/08
     */
    public void coursePrerequisitesItemAction() {
        menuButton.setText("Course Prerequisites");
    }

    /**
     * The method sets menuButton to say "Course Antirequisites".
     *
     * @date 2019/04/08
     */
    public void courseAntirequisitesItemAction() {
        menuButton.setText("Course Antirequisites");
    }

    /**
     * The method gets texts in the SearchByKeyword textfield.
     *
     * @date 2019/04/08
     */
    public void searchBarKeyReleased() {
        keyword = searchByKeyword.getText();
    }

    /**
     * The method sorts through the course's array and finds any the keyword depends on the search criteria selected
     * sets Table View to match the temp array.
     *
     * @date 2019/04/08
     */
    public void goOnAction() {

        if (keyword.isEmpty()) {
            return;
        }

        ObservableList<CourseForGUI> temp = FXCollections.observableArrayList();
        switch (menuButton.getText()) {
            case "Course ID":
                for (CourseForGUI i : Database.coursesForGUI) {
                    if (i.getCourseId().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Course Name":
                for (CourseForGUI i : Database.coursesForGUI) {
                    if (i.getCourseName().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Course Description":
                for (CourseForGUI i : Database.coursesForGUI) {
                    if (i.getCourseDescription().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Course Prerequisites":
                for (CourseForGUI i : Database.coursesForGUI) {
                    if (i.getPrerequisites().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Course Antirequisites":
                for (CourseForGUI i : Database.coursesForGUI) {
                    if (i.getAntirequisites().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            default:
                return;
        }
        tableView.setItems(temp);

        // update stats info
        total.setText("TOTAL: " + temp.size());
    }

    /**
     * The method allows any user to search for courses through any keyword and updates the table view to reflect
     * that search.
     *
     * @date 2019/04/08
     */
    public void searchCourses() {

        // set the visibility and the contents
        searchPanel.setText("SEARCH PANEL");
        keywordLabel.setText("KEYWORD");
        searchByKeyword.setVisible(true);
        menuButton.setVisible(true);
        go.setVisible(true);

        // import data
        tableView.setItems(courseList);

        // update stats info
        total.setText("TOTAL: " + courseList.size());
    }

    /**
     * Initialize the controller class.
     *
     * @param url Uniform Resource Locator
     * @param rb  Resource Bundle
     * @date 2019/04/08
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // initialization of infoLabel
        String s = "Welcome, " + Database.accounts.get(MenuGUI.uuid).getUsername();
        infoLabel.setText(s);

        // set up the columns in the table
        courseUnitsColumn.setCellValueFactory(new PropertyValueFactory<>("courseUnits"));
        canBeRepeated.setCellValueFactory(new PropertyValueFactory<>("canBeRepeated"));
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        // no drag and drop
        tableView.getColumns().addListener((ListChangeListener<TableColumn<CourseForGUI, ?>>) c -> {
            c.next();
            if (c.wasReplaced()) {
                tableView.getColumns().clear();
                tableView.getColumns().add(courseIdColumn);
                tableView.getColumns().add(courseNameColumn);
                tableView.getColumns().add(courseUnitsColumn);
                tableView.getColumns().add(canBeRepeated);
            }
        });

        // refresh the info in text areas if an item is selected
        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            CourseForGUI temp = tableView.getSelectionModel().getSelectedItem();
            if (temp != null) {
                courseNameTextArea.setText(temp.getCourseName());
                courseDescriptionTextArea.setText(temp.getCourseDescription());
                coursePrerequisitesTextArea.setText(temp.getPrerequisites());
                courseAntirequisitesTextArea.setText(temp.getAntirequisites());

                // if the page is 'Academic Requirements', show the course property in keywordLabel
                if ("COURSE PROPERTY".equals(searchPanel.getText())) {
                    String tempStr = "";
                    if (Database.optionalCourses.contains(temp.getCourseId())) {
                        tempStr += "Optional for graduation\n";
                    } else {
                        tempStr += "Mandatory for graduation\n";
                    }

                    if (Database.optionalCoursesForInternship.contains(temp.getCourseId())) {
                        tempStr += "Optional for internship";
                    } else if (Database.mandatoryCoursesForInternship.contains(temp.getCourseId())) {
                        tempStr += "Mandatory for internship";
                    }
                    keywordLabel.setText(tempStr);
                }
            }
        });

        // initialization of info of all courses and courses in academic requirements
        initializeCourses();
        initializeCoursesOfAcademicRequirements(MenuGUI.uuid);

        // default page
        searchCourses();
    }

    /**
     * Convert Database.coursesForGUI to ObservableList and return
     *
     * @date 2019/04/08
     */
    private void initializeCourses() {
        courseList = FXCollections.observableArrayList();
        courseList.addAll(Database.coursesForGUI);
    }

    /**
     * Initialize all remaining courses that the student still needs to take before graduation and import to
     * coursesRemaining.
     *
     * @param uuid the uuid of the student
     * @date 2019/04/08
     */
    private void initializeCoursesOfAcademicRequirements(String uuid) {

        // initialize a student object in order to obtain the information
        Student student = (Student) Database.accounts.get(uuid);

        // obtain the courses the student has taken
        HashSet<String>           coursesTakenSet = new HashSet<>();
        ArrayList<CourseAndGrade> tempArr0        = student.getTakenCoursesList();
        for (CourseAndGrade i : tempArr0) {
            coursesTakenSet.add(i.getCourseId());
        }
        ArrayList<String> tempArr1 = student.getCurrentCoursesList();
        coursesTakenSet.addAll(tempArr1);

        // obtain all the remaining courses that the student has not taken yet
        // wrap them as CourseForGUI objects and import to coursesRemaining
        ArrayList<CourseForGUI> temp = new ArrayList<>();
        for (String i : Database.mandatoryCourses) {
            if (!coursesTakenSet.contains(i)) {
                temp.add(new CourseForGUI(Database.courses.get(i)));
            }
        }

        for (String i : Database.optionalCourses) {
            if (!coursesTakenSet.contains(i)) {
                temp.add(new CourseForGUI(Database.courses.get(i)));
            }
        }

        // sort
        Collections.sort(temp);

        // convert to ObservableList<CourseForGUI>
        coursesRemaining = FXCollections.observableArrayList();
        coursesRemaining.addAll(temp);
    }
}
