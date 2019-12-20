import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * The {@code AdminGUI} class inherits class User and contains methods that initializes a admin user.
 *
 * @author Group25
 * @date 2019/3/23
 */
public class AdminGUI implements Initializable {

    public static String                                          uuidToEdit     = "";
    public static String                                          courseIdToEdit = "";
    public static ObservableList<CourseForGUI>                    courseList     = FXCollections.observableArrayList();
    public static ObservableList<UserForGUI>                      accountList    = FXCollections.observableArrayList();
    private       String                                          keyword        = "";
    @FXML
    private       Label                                           infoLabel;
    @FXML
    private       TableView<CourseForGUI>                         tableViewForCourses;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> courseIdColumn;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> courseNameColumn;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> courseUnitsColumn;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> canBeRepeated;
    @FXML
    private       Pane                                            paneForCourses;
    @FXML
    private       TextArea                                        courseNameTextArea;
    @FXML
    private       TextArea                                        courseDescriptionTextArea;
    @FXML
    private       TextArea                                        coursePrerequisitesTextArea;
    @FXML
    private       TextArea                                        courseAntirequisitesTextArea;
    @FXML
    private       TextField                                       searchByKeywordForCourses;
    @FXML
    private       MenuButton                                      menuButtonForCourses;


    @FXML
    private TableView<UserForGUI>                         tableViewForAccounts;
    @FXML
    private TableColumn<UserForGUI, SimpleStringProperty> uuidColumn;
    @FXML
    private TableColumn<UserForGUI, SimpleStringProperty> usernameColumn;
    @FXML
    private TableColumn<UserForGUI, SimpleStringProperty> fullNameColumn;
    @FXML
    private TableColumn<UserForGUI, SimpleStringProperty> roleColumn;
    @FXML
    private TableColumn<UserForGUI, SimpleStringProperty> genderColumn;
    @FXML
    private TableColumn<UserForGUI, SimpleStringProperty> dateOfBirthColumn;
    @FXML
    private TableColumn<UserForGUI, SimpleStringProperty> emailColumn;
    @FXML
    private TableColumn<UserForGUI, SimpleStringProperty> phoneNumberColumn;
    @FXML
    private Pane                                          paneForAccounts;
    @FXML
    private TextArea                                      uuidTextArea;
    @FXML
    private TextArea                                      usernameTextArea;
    @FXML
    private TextArea                                      fullNameTextArea;
    @FXML
    private TextArea                                      emailAddressTextArea;
    @FXML
    private TextArea                                      addressTextArea;
    @FXML
    private TextField                                     searchByKeywordForAccounts;
    @FXML
    private MenuButton                                    menuButtonForAccounts;

    public void searchCoursesOnAction(ActionEvent e) {
        searchCourses();
    }

    public void courseIdMenuItemAction(ActionEvent e) {
        menuButtonForCourses.setText("Course ID");
    }

    public void courseNameMenuItemAction(ActionEvent e) {
        menuButtonForCourses.setText("Course Name");
    }

    public void courseDescriptionItemAction(ActionEvent e) {
        menuButtonForCourses.setText("Course Description");
    }

    public void coursePrerequisitesItemAction(ActionEvent e) {
        menuButtonForCourses.setText("Course Prerequisites");
    }

    public void courseAntirequisitesItemAction(ActionEvent e) {
        menuButtonForCourses.setText("Course Antirequisites");
    }

    public void searchBarForCoursesKeyReleased(Event e) {
        keyword = searchByKeywordForCourses.getText();
    }

    public void goForCoursesOnAction(ActionEvent e) {

        if (keyword.isEmpty()) {
            return;
        }

        ObservableList<CourseForGUI> temp = FXCollections.observableArrayList();
        switch (menuButtonForCourses.getText()) {
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
            case "Course Antirequisites":
                for (CourseForGUI i : Database.coursesForGUI) {
                    if (i.getAntirequisites().contains(keyword)) {
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
            default:
                return;
        }
        tableViewForCourses.setItems(temp);
    }

    /**
     * searchCourses
     */
    public void searchCourses() {

        // switch the visibility
        paneForAccounts.setVisible(false);
        tableViewForAccounts.setVisible(false);
        paneForCourses.setVisible(true);
        tableViewForCourses.setVisible(true);

        // set up the columns in the table
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseUnitsColumn.setCellValueFactory(new PropertyValueFactory<>("courseUnits"));
        canBeRepeated.setCellValueFactory(new PropertyValueFactory<>("canBeRepeated"));

        // import data
        tableViewForCourses.setItems(courseList);

        // no drag and drop
        tableViewForCourses.getColumns().addListener((ListChangeListener<TableColumn<CourseForGUI, ?>>) c -> {
            c.next();
            if (c.wasReplaced()) {
                tableViewForCourses.getColumns().clear();
                tableViewForCourses.getColumns().addAll(Arrays.asList(
                        courseIdColumn,
                        courseNameColumn,
                        courseUnitsColumn,
                        canBeRepeated));
            }
        });

        // refresh the info in text areas if an item is selected
        tableViewForCourses.getSelectionModel().selectedItemProperty().addListener((ChangeListener) (observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            CourseForGUI temp = tableViewForCourses.getSelectionModel().getSelectedItem();
            if (temp != null) {
                courseNameTextArea.setText(temp.getCourseName());
                courseDescriptionTextArea.setText(temp.getCourseDescription());
                coursePrerequisitesTextArea.setText(temp.getPrerequisites());
                courseAntirequisitesTextArea.setText(temp.getAntirequisites());
            }
        });
    }

    public void uuidMenuItemAction(ActionEvent e) {
        menuButtonForAccounts.setText("UUID");
    }

    public void usernameMenuItemAction(ActionEvent e) {
        menuButtonForAccounts.setText("Username");
    }

    public void fullNameMenuItemAction(ActionEvent e) {
        menuButtonForAccounts.setText("Full name");
    }

    public void roleMenuItemAction(ActionEvent e) {
        menuButtonForAccounts.setText("Role");
    }

    public void genderMenuItemAction(ActionEvent e) {
        menuButtonForAccounts.setText("Gender");
    }

    public void dateOfBirthMenuItemAction(ActionEvent e) {
        menuButtonForAccounts.setText("Date of birth");
    }

    public void phoneNumberMenuItemAction(ActionEvent e) {
        menuButtonForAccounts.setText("Phone number");
    }

    public void emailAddressMenuItemAction(ActionEvent e) {
        menuButtonForAccounts.setText("Email address");
    }

    public void addressMenuItemAction(ActionEvent e) {
        menuButtonForAccounts.setText("Address");
    }

    public void searchBarForAccountsKeyReleased(Event e) {
        keyword = searchByKeywordForAccounts.getText();
    }

    public void goForAccountsOnAction(ActionEvent e) {

        if (keyword.isEmpty()) {
            return;
        }

        ObservableList<UserForGUI> temp = FXCollections.observableArrayList();
        switch (menuButtonForAccounts.getText()) {
            case "UUID":
                for (UserForGUI i : Database.accountsForGUI) {
                    if (i.getUuid().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Username":
                for (UserForGUI i : Database.accountsForGUI) {
                    if (i.getUsername().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Full name":
                for (UserForGUI i : Database.accountsForGUI) {
                    if (i.getFullName().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Role":
                for (UserForGUI i : Database.accountsForGUI) {
                    if (i.getRole().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Gender":
                for (UserForGUI i : Database.accountsForGUI) {
                    if (i.getGender().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Date of birth":
                for (UserForGUI i : Database.accountsForGUI) {
                    if (i.getDateOfBirth().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Phone number":
                for (UserForGUI i : Database.accountsForGUI) {
                    if (i.getPhoneNumber().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Email address":
                for (UserForGUI i : Database.accountsForGUI) {
                    if (i.getEmailAddress().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            case "Address":
                for (UserForGUI i : Database.accountsForGUI) {
                    if (i.getAddress().contains(keyword)) {
                        temp.add(i);
                    }
                }
                break;
            default:
                return;
        }
        tableViewForAccounts.setItems(temp);

    }

    public void searchAccountsOnAction(ActionEvent e) {
        searchAccounts();
    }

    /**
     *
     */
    private void searchAccounts() {

        // switch the visibility
        paneForCourses.setVisible(false);
        tableViewForCourses.setVisible(false);
        paneForAccounts.setVisible(true);
        tableViewForAccounts.setVisible(true);

        // set up the columns in the table
        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));

        // import data
        tableViewForAccounts.setItems(accountList);

        // no drag and drop
        tableViewForAccounts.getColumns().addListener((ListChangeListener<TableColumn<UserForGUI, ?>>) c -> {
            c.next();
            if (c.wasReplaced()) {
                tableViewForAccounts.getColumns().clear();
                tableViewForAccounts.getColumns().addAll(Arrays.asList(
                        uuidColumn,
                        usernameColumn,
                        fullNameColumn,
                        roleColumn,
                        genderColumn,
                        dateOfBirthColumn,
                        phoneNumberColumn,
                        emailColumn)
                );
            }
        });

        // refresh the info in text areas if an item is selected
        tableViewForAccounts.getSelectionModel().selectedItemProperty().addListener((ChangeListener) (observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            UserForGUI temp = tableViewForAccounts.getSelectionModel().getSelectedItem();
            if (temp != null) {
                uuidTextArea.setText(temp.getUuid());
                usernameTextArea.setText(temp.getUsername());
                fullNameTextArea.setText(temp.getFullName());
                emailAddressTextArea.setText(temp.getEmailAddress());
                addressTextArea.setText(temp.getAddress());
            }
        });

    }

    public void addACourseOnClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CourseAddGUI.fxml"));
            Parent     root       = fxmlLoader.load();
            Stage      stage      = new Stage();
            stage.setTitle("Add a course");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(MenuGUI.subStage);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // update info in tableView
        initializeCourses();
        tableViewForCourses.setItems(courseList);
    }

    public void addAnAccountOnClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserAddGUI.fxml"));
            Parent     root       = fxmlLoader.load();
            Stage      stage      = new Stage();
            stage.setTitle("Add an account");
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
     * Initialize the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // initialization of infoLabel
        String s = "Welcome, " + Database.accounts.get(MenuGUI.uuid).getUsername();
        infoLabel.setText(s);

        // initialization of info of all courses
        initializeCourses();

        // initialization of all accounts
        initializeAccounts();

        // default page
        searchCourses();
    }

    /**
     * Convert Database.coursesForGUI to ObservableList and return
     */
    private void initializeCourses() {
        if (courseList == null) {
            courseList = FXCollections.observableArrayList();
        }
        courseList.clear();
        courseList.addAll(Database.coursesForGUI);
    }

    /**
     * Convert Database.accountsForGUI to ObservableList and return
     */
    private void initializeAccounts() {
        if (accountList == null) {
            accountList = FXCollections.observableArrayList();
        }
        accountList.clear();
        accountList.addAll(Database.accountsForGUI);
    }

    /**
     * Define the action of 'Enrollment Settings' button
     */
    public void enrollmentSettingsOnClicked() {
        if (User.getEnrollmentStatus().equals(Constants.ENROLLMENT_CLOSE)) {
            Util.showError("ERROR", "Enrollment not open yet!", "Please refer to the calender for more details.");
        } else {
            // TODO
        }
    }

    /**
     * Define the action of 'Edit' button
     */
    public void editOnClicked() {

        if (tableViewForCourses.isVisible()) {
            CourseForGUI obj = tableViewForCourses.getSelectionModel().getSelectedItem();

            if (obj == null) {
                return;
            }

            try {
                courseIdToEdit = obj.getCourseId();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CourseInfoEditGUI" +
                        ".fxml"));
                Parent root  = fxmlLoader.load();
                Stage  stage = new Stage();
                stage.setTitle("Course Information Edit");
                stage.setScene(new Scene(root));
                stage.sizeToScene();
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(MenuGUI.subStage);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            initializeCourses();
            tableViewForCourses.setItems(courseList);

        } else {
            UserForGUI obj = tableViewForAccounts.getSelectionModel().getSelectedItem();

            if (obj == null) {
                return;
            }

            try {
                uuidToEdit = obj.getUuid();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UserInfoEditGUI" +
                        ".fxml"));
                Parent root  = fxmlLoader.load();
                Stage  stage = new Stage();
                stage.setTitle("Account Information Edit");
                stage.setScene(new Scene(root));
                stage.sizeToScene();
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(MenuGUI.subStage);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // update the tableView of accounts
            initializeAccounts();
            tableViewForAccounts.setItems(accountList);
        }

    }

    /**
     * Define the action of 'Remove' button
     */
    public void removeButtonOnClicked() {

        if (tableViewForCourses.isVisible()) {
            CourseForGUI obj = tableViewForCourses.getSelectionModel().getSelectedItem();

            if (obj == null) {
                return;
            }

            // pop up a window to make the user confirm
            boolean ifConfirm = Util.showConfirmation(
                    "Confirmation",
                    "Are you sure to delete the course?",
                    "Course ID: " + obj.getCourseId());

            if (ifConfirm) {

                // delete the item in all containers in Database class
                Database.coursesForGUI.removeIf(x -> x.getCourseId().equals(obj.getCourseId()));
                Database.courses.remove(obj.getCourseId());
                Database.optionalCourses.remove(obj.getCourseId());
                Database.mandatoryCourses.remove(obj.getCourseId());
                Database.optionalCoursesForInternship.remove(obj.getCourseId());
                Database.mandatoryCoursesForInternship.remove(obj.getCourseId());


                // commit to database file, including INTERNSHIP_REQUIREMENTS.db, ACADEMIC_REQUIREMENTS.db
                // regardless it's an optional or mandatory course for graduation or internship
                Database.courseRemovalCommitToRequirements(obj.getCourseId(), true, true);
                Database.courseRemovalCommitToRequirements(obj.getCourseId(), true, false);
                Database.courseRemovalCommitToRequirements(obj.getCourseId(), false, true);
                Database.courseRemovalCommitToRequirements(obj.getCourseId(), false, false);
                Database.courseRemovalCommit(obj.getCourseId());

                // delete the item in courseList
                courseList.remove(obj);
                // refresh the tableView
                tableViewForCourses.setItems(courseList);

                // pop up a window to indicate the successful deletion
                Util.showAlert("Notification", null, "Course successfully removed!");
            }

        } else {

            UserForGUI obj = tableViewForAccounts.getSelectionModel().getSelectedItem();

            if (obj == null) {
                return;
            }

            // pop up a window to make the user confirm
            boolean ifConfirm = Util.showConfirmation(
                    "Confirmation",
                    "Are you sure to delete the account?",
                    "Account UUID: " + obj.getUuid());

            if (ifConfirm) {

                // delete the item in all containers in Database class
                Database.accountsForGUI.removeIf(x -> x.getUuid().equals(obj.getUuid()));
                Database.accounts.remove(obj.getUuid());
                Database.usernameToUuid.remove(obj.getUsername());

                // delete the item in courseList
                accountList.remove(obj);

                // commit to database file, including ACCOUNT_DATA.db, FACULTY.db, STUDENTS.db
                Database.accountRemovalCommit(obj.getUuid());

                // pop up a window to indicate the successful deletion
                Util.showAlert("Notification", null, "Account successfully removed!");

                // refresh the tableView
                tableViewForAccounts.setItems(accountList);
            }
        }

    }
}
