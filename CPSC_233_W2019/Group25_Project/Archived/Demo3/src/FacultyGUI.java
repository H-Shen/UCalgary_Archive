import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.util.ResourceBundle;


public class FacultyGUI implements Initializable {

    static {
        Database.initializeFacultyInformation(MenuGUI.uuid);
    }

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

    public void searchCoursesOnAction(ActionEvent e) {
        searchCourses();
    }

    public void coursesManagmentButtonOnAction(ActionEvent e) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CourseManagementGUI.fxml"));
            Parent     root       = fxmlLoader.load();
            Stage      stage      = new Stage();
            stage.setTitle("Courses Management");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(MenuGUI.subStage);
            stage.show();
        } catch (Exception f) {
            f.printStackTrace();
        }
    }


    public void accountSettingsOnAction(ActionEvent e) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void courseIdMenuItemAction(ActionEvent e) {
        menuButton.setText("Course ID");
    }

    public void courseNameMenuItemAction(ActionEvent e) {
        menuButton.setText("Course Name");
    }

    public void courseDescriptionItemAction(ActionEvent e) {
        menuButton.setText("Course Description");
    }

    public void coursePrerequisitesItemAction(ActionEvent e) {
        menuButton.setText("Course Prerequisites");
    }

    public void courseAntirequisitesItemAction(ActionEvent e) {
        menuButton.setText("Course Antirequisites");
    }

    public void searchBarKeyReleased(Event e) {
        keyword = searchByKeyword.getText();
    }

    public void goOnAction(ActionEvent e) {

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
            case "Course Description":
                for (CourseForGUI i : Database.coursesForGUI) {
                    if (i.getCourseDescription().contains(keyword)) {
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
        searchByKeyword.setVisible(true);
        menuButton.setVisible(true);
        go.setVisible(true);
        searchPanel.setText("SEARCH PANEL");
        keywordLabel.setText("KEYWORD");

        // set up the columns in the table
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseUnitsColumn.setCellValueFactory(new PropertyValueFactory<>("courseUnits"));
        canBeRepeated.setCellValueFactory(new PropertyValueFactory<>("canBeRepeated"));

        // import data
        tableView.setItems(courseList);

        // no drag and drop
        tableView.getColumns().addListener(new ListChangeListener<TableColumn<CourseForGUI, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<CourseForGUI, ?>> c) {
                c.next();
                if (c.wasReplaced()) {
                    tableView.getColumns().clear();
                    tableView.getColumns().addAll(courseIdColumn, courseNameColumn, courseUnitsColumn, canBeRepeated);
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

                    coursePrerequisitesTextArea.setText(temp.getPrerequisites());
                    courseAntirequisitesTextArea.setText(temp.getAntirequisites());
                    courseNameTextArea.setText(temp.getCourseName());
                    courseDescriptionTextArea.setText(temp.getCourseDescription());

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

    public void initializeCoursesTeaching(String uuid) {

        // initialize a student object in order to obtain the information
        Faculty faculty = (Faculty) Database.accounts.get(uuid);

        // obtain the courses the student has taken
        ArrayList<String> tempArr0 = faculty.getCoursesTeaching();


    }

}
