import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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


public class StudentGUI implements Initializable {

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

    private String                       keyword = "";
    private ObservableList<CourseForGUI> courseList;
    private ObservableList<CourseForGUI> coursesRemaining;

    public void searchCoursesOnAction(ActionEvent e) {
        searchCourses();
    }

    public void academicRequirementsOnAction(ActionEvent e) {

        // set the visibility and the contents
        searchPanel.setText("COURSE PROPERTY");
        keywordLabel.setText("Mandatory / Optional:" + "\n" + "Mandatory / Optional for internship:");
        searchByKeyword.setVisible(false);
        menuButton.setVisible(false);
        go.setVisible(false);

        // update the courses list of academic requirements to table view
        tableView.setItems(coursesRemaining);
    }

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

    public void enrollmentOnAction() {
        if (User.getEnrollmentStatus().equals("CLOSE")) {
            Util.showError("ERROR", "Enrollment not open yet!", "Please refer to the calender for more details.");
        } else {
            // TODO
        }
    }

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

    public void courseIdMenuItemAction() {
        menuButton.setText("Course ID");
    }

    public void courseNameMenuItemAction() {
        menuButton.setText("Course Name");
    }

    public void courseDescriptionItemAction() {
        menuButton.setText("Course Description");
    }

    public void coursePrerequisitesItemAction() {
        menuButton.setText("Course Prerequisites");
    }

    public void courseAntirequisitesItemAction() {
        menuButton.setText("Course Antirequisites");
    }

    public void searchBarKeyReleased() {
        keyword = searchByKeyword.getText();
    }

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
    }

    /**
     * searchCourses
     */
    public void searchCourses() {

        // set the visibility and the contents
        searchPanel.setText("SEARCH PANEL");
        keywordLabel.setText("KEYWORD");
        searchByKeyword.setVisible(true);
        menuButton.setVisible(true);
        go.setVisible(true);

        // set up the columns in the table
        courseUnitsColumn.setCellValueFactory(new PropertyValueFactory<>("courseUnits"));
        canBeRepeated.setCellValueFactory(new PropertyValueFactory<>("canBeRepeated"));
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));

        // import data
        tableView.setItems(courseList);

        // no drag and drop
        tableView.getColumns().addListener(new ListChangeListener<TableColumn<CourseForGUI, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<CourseForGUI, ?>> c) {
                c.next();
                if (c.wasReplaced()) {
                    tableView.getColumns().clear();
                    tableView.getColumns().add(courseIdColumn);
                    tableView.getColumns().add(courseNameColumn);
                    tableView.getColumns().add(courseUnitsColumn);
                    tableView.getColumns().add(canBeRepeated);
                }
            }
        });

        // refresh the info in text areas if an item is selected
        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                CourseForGUI temp = tableView.getSelectionModel().getSelectedItem();
                if (temp != null) {
                    courseNameTextArea.setText(temp.getCourseName());
                    courseDescriptionTextArea.setText(temp.getCourseDescription());
                    coursePrerequisitesTextArea.setText(temp.getPrerequisites());
                    courseAntirequisitesTextArea.setText(temp.getAntirequisites());

                    // if the page is 'Academic Requirements', show the course property in keywordLabel
                    if (searchPanel.getText().equals("COURSE PROPERTY")) {
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
            }
        });
    }

    /**
     * Initialize the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // initializtion of infoLabel
        String s = "Welcome, " + Database.accounts.get(MenuGUI.uuid).getUsername();
        infoLabel.setText(s);

        // initialization of info of all courses and courses in academic requirements
        initializeCourses();
        initializeCoursesOfAcademicRequirements(MenuGUI.uuid);

        // default page
        searchCourses();
    }

    /**
     * Convert Database.coursesForGUI to ObservableList and return
     */
    public void initializeCourses() {
        courseList = FXCollections.observableArrayList();
        for (CourseForGUI i : Database.coursesForGUI) {
            courseList.add(i);
        }
    }

    /**
     * @param uuid
     * @return
     */
    public void initializeCoursesOfAcademicRequirements(String uuid) {

        // initialize a student object in order to obtain the information
        Student student = (Student) Database.accounts.get(uuid);

        // obtain the courses the student has taken
        HashSet<String>           coursesTakenSet = new HashSet<>();
        ArrayList<CourseAndGrade> tempArr0        = student.getTakenCoursesList();
        for (CourseAndGrade i : tempArr0) {
            coursesTakenSet.add(i.getCourseId());
        }
        ArrayList<String> tempArr1 = student.getCurrentCoursesList();
        for (String i : tempArr1) {
            coursesTakenSet.add(i);
        }

        // obtain all the remaining courses that the student has not taken yet
        // wrap them as CourseForGUI objects and import to coursesRemaining
        ArrayList<CourseForGUI> temp = new ArrayList<>();
        for (String i : Database.mandatoryCourses) {
            if (!coursesTakenSet.contains(i)) {
                temp.add(new CourseForGUI(Database.courses.get(i)));
            }
        }

        ArrayList<CourseForGUI> optionalCourses = new ArrayList<>();
        for (String i : Database.optionalCourses) {
            if (!coursesTakenSet.contains(i)) {
                temp.add(new CourseForGUI(Database.courses.get(i)));
            }
        }

        // sort
        Collections.sort(temp);

        // convert to ObservableList<CourseForGUI>
        coursesRemaining = FXCollections.observableArrayList();
        for (CourseForGUI i : temp) {
            coursesRemaining.add(i);
        }
    }
}