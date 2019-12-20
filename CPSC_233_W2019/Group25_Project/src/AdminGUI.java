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
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * The {@code AdminGUI} class defines the layout and logic of Administrator's Panel in GUI.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class AdminGUI implements Initializable {

    /**
     * @Fields Properties and components used in GUI
     */
    public static String                                          uuidToEdit     = "";
    public static String                                          courseIdToEdit = "";
    public static ObservableList<CourseForGUI>                    courseList     = FXCollections.observableArrayList();
    public static ObservableList<UserForGUI>                      accountList    = FXCollections.observableArrayList();
    private       String                                          keyword        = "";
    @FXML
    private       Label                                           infoLabel;
    @FXML
    private       TableView<CourseForGUI>                         coursesTable;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> courseIdColumn;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> courseNameColumn;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> courseUnitsColumn;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> canBeRepeated;
    @FXML
    private       TableColumn<CourseForGUI, SimpleStringProperty> courseDescription;
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
    private       TableView<UserForGUI>                           accountsTable;
    @FXML
    private       TableColumn<UserForGUI, SimpleStringProperty>   uuidColumn;
    @FXML
    private       TableColumn<UserForGUI, SimpleStringProperty>   usernameColumn;
    @FXML
    private       TableColumn<UserForGUI, SimpleStringProperty>   fullNameColumn;
    @FXML
    private       TableColumn<UserForGUI, SimpleStringProperty>   roleColumn;
    @FXML
    private       TableColumn<UserForGUI, SimpleStringProperty>   genderColumn;
    @FXML
    private       TableColumn<UserForGUI, SimpleStringProperty>   dateOfBirthColumn;
    @FXML
    private       TableColumn<UserForGUI, SimpleStringProperty>   emailColumn;
    @FXML
    private       TableColumn<UserForGUI, SimpleStringProperty>   phoneNumberColumn;
    @FXML
    private       Pane                                            paneForAccounts;
    @FXML
    private       TextArea                                        uuidTextArea;
    @FXML
    private       TextArea                                        usernameTextArea;
    @FXML
    private       TextArea                                        fullNameTextArea;
    @FXML
    private       TextArea                                        emailAddressTextArea;
    @FXML
    private       TextArea                                        addressTextArea;
    @FXML
    private       TextField                                       searchByKeywordForAccounts;
    @FXML
    private       MenuButton                                      menuButtonForAccounts;
    @FXML
    private       Label                                           total;

    /**
     * The method searches courses when the user press the 'Search Courses' button by calling searchCourses().
     *
     * @date 2019/04/08
     */
    public void searchCoursesOnAction() {
        searchCourses();
    }

    /**
     * The method sets the text on menu button to "Course ID" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void courseIdMenuItemAction() {
        menuButtonForCourses.setText("Course ID");
    }

    /**
     * The method sets the text on menu button to "Course Name" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void courseNameMenuItemAction() {
        menuButtonForCourses.setText("Course Name");
    }

    /**
     * The method sets the text on menu button to "Course Description" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void courseDescriptionItemAction() {
        menuButtonForCourses.setText("Course Description");
    }

    /**
     * The method sets the text on menu button to "Course Prerequisites" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void coursePrerequisitesItemAction() {
        menuButtonForCourses.setText("Course Prerequisites");
    }

    /**
     * The method sets the text on menu button to "Course Antirequisites" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void courseAntirequisitesItemAction() {
        menuButtonForCourses.setText("Course Antirequisites");
    }

    /**
     * The method gets the text from the keyword textfield when the user searches courses.
     *
     * @date 2019/04/08
     */
    public void searchBarForCoursesKeyReleased() {
        keyword = searchByKeywordForCourses.getText();
    }

    /**
     * The method filters the items in the table view of courses and only shows those contains the keyword in the
     * corresponding property.
     *
     * @date 2019/04/08
     */
    public void goForCoursesOnAction() {

        // if the keyword is empty, do nothing
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
        coursesTable.setItems(temp);

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

        // switch the visibility
        paneForAccounts.setVisible(false);
        accountsTable.setVisible(false);
        paneForCourses.setVisible(true);
        coursesTable.setVisible(true);

        // import data
        coursesTable.setItems(courseList);

        // update stats info
        total.setText("TOTAL: " + courseList.size());
    }

    /**
     * The method sets the text on menu button to "UUID" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void uuidMenuItemAction() {
        menuButtonForAccounts.setText("UUID");
    }

    /**
     * The method sets the text on menu button to "Username" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void usernameMenuItemAction() {
        menuButtonForAccounts.setText("Username");
    }

    /**
     * The method sets the text on menu button to "Full Name" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void fullNameMenuItemAction() {
        menuButtonForAccounts.setText("Full name");
    }

    /**
     * The method sets the text on menu button to "Role" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void roleMenuItemAction() {
        menuButtonForAccounts.setText("Role");
    }

    /**
     * The method sets the text on menu button to "Gender" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void genderMenuItemAction() {
        menuButtonForAccounts.setText("Gender");
    }

    /**
     * The method sets the text on menu button to "Date of birth" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void dateOfBirthMenuItemAction() {
        menuButtonForAccounts.setText("Date of birth");
    }

    /**
     * The method sets the text on menu button to "Phone number" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void phoneNumberMenuItemAction() {
        menuButtonForAccounts.setText("Phone number");
    }

    /**
     * The method sets the text on menu button to "Email address" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void emailAddressMenuItemAction() {
        menuButtonForAccounts.setText("Email address");
    }

    /**
     * The method sets the text on menu button to "Address" when the user selects this menu item.
     *
     * @date 2019/04/08
     */
    public void addressMenuItemAction() {
        menuButtonForAccounts.setText("Address");
    }

    /**
     * The method gets the contents from the searchBar textfield.
     *
     * @date 2019/04/08
     */
    public void searchBarForAccountsKeyReleased() {
        keyword = searchByKeywordForAccounts.getText();
    }

    /**
     * The method searches accounts database based on the keyword textfield and the field selected adds all that
     * match to a temp array displays temp array in the table view.
     *
     * @date 2019/04/08
     */
    public void goForAccountsOnAction() {

        // if the keyword is empty, do nothing.
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
        accountsTable.setItems(temp);

        // update stats info
        total.setText("TOTAL: " + temp.size());
    }

    /**
     * The method calls the method for searching accounts when button clicked.
     *
     * @date 2019/04/08
     */
    public void searchAccountsOnAction() {
        searchAccounts();
    }


    /**
     * The method allows any user to search for accounts through any keyword and updates the Table
     * view to reflect that search.
     *
     * @date 2019/04/08
     */
    private void searchAccounts() {

        // switch the visibility
        paneForCourses.setVisible(false);
        coursesTable.setVisible(false);
        paneForAccounts.setVisible(true);
        accountsTable.setVisible(true);

        // import data
        accountsTable.setItems(accountList);

        // update stats info
        total.setText("TOTAL: " + accountList.size());
    }

    /**
     * When the button "Add a course" is clicked, a new window pops up that provides the proper text fields
     * for an administrator to create a new course.
     *
     * @date 2019/04/08
     */
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
        coursesTable.setItems(courseList);
    }

    /**
     * When the button "Add an account" is clicked, a new window pops up that gives the proper text
     * fields to add enough information to create a new account.
     *
     * @date 2019/04/08
     */
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
     * @param url Uniform Resource Locator
     * @param rb  Resource Bundle
     * @date 2019/04/08
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // initialization of infoLabel
        infoLabel.setText("Welcome, " + Database.accounts.get(MenuGUI.uuid).getUsername());

        // set up the columns in the course table
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseUnitsColumn.setCellValueFactory(new PropertyValueFactory<>("courseUnits"));
        canBeRepeated.setCellValueFactory(new PropertyValueFactory<>("canBeRepeated"));
        courseDescription.setCellValueFactory(new PropertyValueFactory<>("courseDescription"));

        // no drag and drop in the course table
        coursesTable.getColumns().addListener((ListChangeListener<TableColumn<CourseForGUI, ?>>) c -> {
            c.next();
            if (c.wasReplaced()) {
                coursesTable.getColumns().clear();
                coursesTable.getColumns().addAll(Arrays.asList(courseIdColumn, courseNameColumn, courseUnitsColumn,
                        canBeRepeated, courseDescription));
            }
        });

        // refresh the info the course in text areas if an item is selected
        coursesTable.getSelectionModel().selectedItemProperty().addListener((observableValue
                , oldValue, newValue) -> {
            // check whether item is selected and set value of selected item to Label
            CourseForGUI temp = coursesTable.getSelectionModel().getSelectedItem();
            if (temp != null) {
                courseNameTextArea.setText(temp.getCourseName());
                courseDescriptionTextArea.setText(temp.getCourseDescription());
                coursePrerequisitesTextArea.setText(temp.getPrerequisites());
                courseAntirequisitesTextArea.setText(temp.getAntirequisites());
            }
        });

        // set up the columns in the account table
        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        dateOfBirthColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailAddress"));

        // no drag and drop in the account table
        accountsTable.getColumns().addListener((ListChangeListener<TableColumn<UserForGUI, ?>>) c -> {
            c.next();
            if (c.wasReplaced()) {
                accountsTable.getColumns().clear();
                accountsTable.getColumns().addAll(Arrays.asList(uuidColumn, usernameColumn, fullNameColumn, roleColumn,
                        genderColumn, dateOfBirthColumn, phoneNumberColumn, emailColumn)
                );
            }
        });

        // refresh the info of the account in text areas if an item is selected
        accountsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            // check whether item is selected and set value of selected item to Label
            UserForGUI temp = accountsTable.getSelectionModel().getSelectedItem();
            if (temp != null) {
                uuidTextArea.setText(temp.getUuid());
                usernameTextArea.setText(temp.getUsername());
                fullNameTextArea.setText(temp.getFullName());
                emailAddressTextArea.setText(temp.getEmailAddress());
                addressTextArea.setText(temp.getAddress());
            }
        });

        // initialization of info of all courses
        initializeCourses();
        // initialization of all accounts
        initializeAccounts();
        // set to the default page
        searchCourses();
    }

    /**
     * Convert Database.coursesForGUI to ObservableList and return
     *
     * @date 2019/04/08
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
     *
     * @date 2019/04/08
     */
    private void initializeAccounts() {
        if (accountList == null) {
            accountList = FXCollections.observableArrayList();
        }
        accountList.clear();
        accountList.addAll(Database.accountsForGUI);
    }

    /**
     * Define the action of 'History Log' button
     *
     * @date 2019/04/08
     */
    public void historyLogOnClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LogGUI.fxml"));
            Parent     root       = fxmlLoader.load();
            Stage      stage      = new Stage();
            stage.setTitle("History Log");
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
     * This method allows an admin user to edit courses that are selected through the table view and updates the
     * database and table to reflect these changes.
     *
     * @date 2019/04/08
     */
    public void editOnClicked() {
        if (coursesTable.isVisible()) {
            CourseForGUI obj = coursesTable.getSelectionModel().getSelectedItem();
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
            coursesTable.setItems(courseList);

        } else {
            UserForGUI obj = accountsTable.getSelectionModel().getSelectedItem();
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
            accountsTable.setItems(accountList);

            // update stats info
            total.setText("TOTAL: " + accountList.size());
        }
    }

    /**
     * The method defines a listener to detect the course within the table view that has been selected by an admin
     * user, and removes the course from the database if the conformation window has been clicked if the course is not
     * currently being taken or taught by another user, (or if a user, is not the same user as the one currently
     * logged in). The table is then updated to reflect this change.
     *
     * @date 2019/04/08
     */
    public void removeButtonOnClicked() {
        if (coursesTable.isVisible()) {
            CourseForGUI obj = coursesTable.getSelectionModel().getSelectedItem();
            if (obj == null) {
                return;
            }
            // pop up a window to make the user confirm
            boolean ifConfirm = Utility.showConfirmation("Confirmation", null,
                    "Are you sure to delete the course?");

            if (ifConfirm) {

                // the admin can only remove a course which is not taken by student or taught by any faculty
                if (Database.courseCurrentlyTaught.contains(obj.getCourseId())) {
                    Utility.showError("ERROR", null, "Cannot remove a course which is currently being taught!");
                    return;
                }

                // the admin cannot remove a course which is a prerequisite or anti-requisite of other courses
                for (Course i : Database.courses.values()) {

                    // pass itself in the iteration
                    if (i.getCourseId().equals(obj.getCourseId())) {
                        continue;
                    }
                    if (i.getPrerequisites().contains(obj.getCourseId())) {
                        Utility.showError("ERROR", null,
                                "Cannot remove a course since it is a prerequisite of " + i.getCourseId() + "!");
                        return;
                    }
                    if (i.getAntirequisites().contains(obj.getCourseId())) {
                        Utility.showError("ERROR", null,
                                "Cannot remove a course since it is a anti-requisite of " + i.getCourseId() + "!");
                        return;
                    }
                }

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
                coursesTable.setItems(courseList);

                // log
                Log.removeACourse(new Timestamp(System.currentTimeMillis()), MenuGUI.uuid, obj.getCourseId());

                // pop up a window to indicate the successful deletion
                Utility.showAlert("Notification", null, "Course successfully removed!");
            }
        } else {
            UserForGUI obj = accountsTable.getSelectionModel().getSelectedItem();
            if (obj == null) {
                return;
            }

            // the administrator cannot delete itself
            if (obj.getUuid().equals(MenuGUI.uuid)) {
                Utility.showError("ERROR", null, "The user cannot delete itself!");
                return;
            }

            // pop up a window to make the user confirm
            boolean ifConfirm = Utility.showConfirmation(
                    "Confirmation", null, "Are you sure to delete the account?");

            if (ifConfirm) {

                // delete the item in all containers in Database class
                Database.accountsForGUI.removeIf(x -> x.getUuid().equals(obj.getUuid()));
                Database.accounts.remove(obj.getUuid());
                Database.usernameToUuid.remove(obj.getUsername());

                // delete the item in courseList
                accountList.remove(obj);

                // commit to database file, including ACCOUNT_DATA.db, FACULTY.db, STUDENTS.db
                Database.accountRemovalCommit(obj.getUuid());

                // log
                Log.removeAnAccount(new Timestamp(System.currentTimeMillis()), MenuGUI.uuid, obj.getUuid());

                // pop up a window to indicate the successful deletion
                Utility.showAlert("Notification", null, "Account successfully removed!");

                // refresh the tableView
                accountsTable.setItems(accountList);

                // update stats info
                total.setText("TOTAL: " + accountList.size());
            }
        }
    }
}
