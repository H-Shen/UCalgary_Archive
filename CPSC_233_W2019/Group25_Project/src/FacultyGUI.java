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
import java.util.ResourceBundle;

/**
 * The {@code FacultyGUI} class defines the layout and logic of Faculty's Panel in GUI.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class FacultyGUI implements Initializable {

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

    /**
     * The method gets text from the searchCourses() textfield in search Courses section.
     *
     * @date 2019/04/08
     */
    public void searchCoursesOnAction() {
        searchCourses();
    }

    /**
     * The method pops up a courses Management window
     *
     * @date 2019/04/08
     */
    public void coursesManagementButtonOnAction() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * The method loads a popup window to edit account settings.
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * The method sets text on the courseMenuButton to "Course ID".
     *
     * @date 2019/04/08
     */
    public void courseIdMenuItemAction() {
        menuButton.setText("Course ID");
    }

    /**
     * The method sets text on the Button to "Course Name".
     *
     * @date 2019/04/08
     */
    public void courseNameMenuItemAction() {
        menuButton.setText("Course Name");
    }

    /**
     * The method sets text from the Button to "Course Description".
     *
     * @date 2019/04/08
     */
    public void courseDescriptionItemAction() {
        menuButton.setText("Course Description");
    }

    /**
     * The method sets text from teh button to "Course Prerequisites".
     *
     * @date 2019/04/08
     */
    public void coursePrerequisitesItemAction() {
        menuButton.setText("Course Prerequisites");
    }

    /**
     * The method sets text from teh button to "Course Antirequisites".
     *
     * @date 2019/04/08
     */
    public void courseAntirequisitesItemAction() {
        menuButton.setText("Course Antirequisites");
    }

    /**
     * The method gets text from the searchBar textfield.
     *
     * @date 2019/04/08
     */
    public void searchBarKeyReleased() {
        keyword = searchByKeyword.getText();
    }

    /**
     * The method filters the items in the table view of courses and only shows those contains the keyword in the
     * corresponding property.
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
        searchByKeyword.setVisible(true);
        menuButton.setVisible(true);
        go.setVisible(true);
        searchPanel.setText("SEARCH PANEL");
        keywordLabel.setText("KEYWORD");

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
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        canBeRepeated.setCellValueFactory(new PropertyValueFactory<>("canBeRepeated"));
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
        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue,
                                                                          oldValue
                , newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            CourseForGUI temp = tableView.getSelectionModel().getSelectedItem();
            if (temp != null) {

                coursePrerequisitesTextArea.setText(temp.getPrerequisites());
                courseAntirequisitesTextArea.setText(temp.getAntirequisites());
                courseNameTextArea.setText(temp.getCourseName());
                courseDescriptionTextArea.setText(temp.getCourseDescription());

                // if the page is 'Academic Requirements', show the course property in keywordLabel
                if ("COURSE PROPERTY".equals(searchPanel.getText())) {
                    String tempStr = "";
                    if (Database.optionalCourses.contains(temp.getCourseId())) {
                        tempStr += "Optional for graduation\n";
                    } else {
                        tempStr += "Mandatory for graduation\n";
                    }

                    if (Database.mandatoryCoursesForInternship.contains(temp.getCourseId())) {
                        tempStr += "Mandatory for internship";
                    } else if (Database.optionalCoursesForInternship.contains(temp.getCourseId())) {
                        tempStr += "Optional for internship";
                    }
                    keywordLabel.setText(tempStr);
                }
            }
        });

        // initialization of info of all courses and courses in academic requirements
        initializeCourses();

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
}
