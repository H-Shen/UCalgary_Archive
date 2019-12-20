import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * The {@code UserInfoEditGUI} class defines all the layout and logic of the window which the admin can
 * edit anyone's account
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class UserInfoEditGUI implements Initializable {

    /**
     * @Fields Properties and components used in GUI
     */
    @FXML
    private Label         uuidLabel;
    @FXML
    private MenuButton    genderMenuButton;
    @FXML
    private TextField     fullNameTextField;
    @FXML
    private TextField     usernameTextField;
    @FXML
    private TextField     addressTextField;
    @FXML
    private TextField     phoneNumberTextField;
    @FXML
    private TextField     dateOfBirthTextField;
    @FXML
    private TextField     emailAddressTextField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField newPasswordAgainField;
    @FXML
    private Button        courseHistoryButton;
    @FXML
    private Label         roleLabel;
    @FXML
    private Slider        slider;
    @FXML
    private Label         intensityIndicator;

    private String fullName;
    private String username;
    private String address;
    private String dateOfBirth;
    private String phoneNumber;
    private String emailAddress;
    private String newPassword;
    private String newPasswordAgain;
    private int    passwordIntensity;

    /**
     * changes the text to display "Male" when the option in the dropdown menu is selected
     *
     * @date 2019/04/08
     */
    public void maleMenuItemAction() {
        genderMenuButton.setText("Male");
    }

    /**
     * changes the text to display "Female" when the option in the dropdown menu is selected
     *
     * @date 2019/04/08
     */
    public void femaleMenuItemAction() {
        genderMenuButton.setText("Female");
    }

    /**
     * changes the text to display new full name if text within the textfield
     *
     * @date 2019/04/08
     */
    public void fullNameTextFieldKeyReleased() {
        fullName = fullNameTextField.getText();
    }

    /**
     * changes the text to display new username if text within the textfield
     *
     * @date 2019/04/08
     */
    public void usernameTextFieldKeyReleased() {
        username = usernameTextField.getText();
    }

    /**
     * changes the text to display new address if text within textfield
     *
     * @date 2019/04/08
     */
    public void addressTextFieldKeyReleased() {
        address = addressTextField.getText();
    }

    /**
     * changes the text to display new date of birth if text within the textfield
     *
     * @date 2019/04/08
     */
    public void dateOfBirthKeyReleased() {
        dateOfBirth = dateOfBirthTextField.getText();
    }

    /**
     * changes the text to display new email if text within the textfield
     *
     * @date 2019/04/08
     */
    public void emailAddressKeyReleased() {
        emailAddress = emailAddressTextField.getText();
    }

    /**
     * changes the text to display new phone number if text within the textfield
     *
     * @date 2019/04/08
     */
    public void phoneNumberTextFieldKeyReleased() {
        phoneNumber = phoneNumberTextField.getText();
    }

    /**
     * if text in textfield, displays a slider to indicate new passwords strength, then updates text to new password
     *
     * @date 2019/04/08
     */
    public void newPasswordTextFieldKeyReleased() {
        newPassword = newPasswordField.getText();

        // update the slider
        passwordIntensity = Utility.passwordIntensity(newPassword);
        slider.setValue(passwordIntensity);

        // update the indicator
        if (passwordIntensity == 100) {
            intensityIndicator.setText("Very secure!");
        } else if (passwordIntensity >= 80) {
            intensityIndicator.setText("Secure");
        } else if (passwordIntensity >= 70) {
            intensityIndicator.setText("Very strong");
        } else if (passwordIntensity >= 60) {
            intensityIndicator.setText("Strong");
        } else if (passwordIntensity >= 50) {
            intensityIndicator.setText("Average");
        } else if (passwordIntensity >= 25) {
            intensityIndicator.setText("Weak");
        } else if (passwordIntensity > 0) {
            intensityIndicator.setText("Very weak");
        } else {
            intensityIndicator.setText("");
        }
    }

    /**
     * updates the variable newPasswordAgain to the text within the textfield
     *
     * @date 2019/04/08
     */
    public void newPasswordAgainTextFieldKeyReleased() {
        newPasswordAgain = newPasswordAgainField.getText();
    }

    /**
     * calls accountInfoValidation to validate all new info, which returns true if the info all fits the constraints
     * if true, calls updateUser and commits the update to the database
     * if user is an admin, add the update to the log history in log show an alert pop-up window
     *
     * @date 2019/04/08
     */
    public void updateOnAction() {

        if (accountInfoValidation()) {
            updateUser();
            updateCommit();
            // log
            if ("ADMIN".equals(roleLabel.getText())) {
                Log.updateAnAccount(new Timestamp(System.currentTimeMillis()), MenuGUI.uuid, uuidLabel.getText());
            } else {
                Log.updateOwnAccount(new Timestamp(System.currentTimeMillis()), MenuGUI.uuid);
            }
            Utility.showAlert("Notification", null, "Account updated successfully!");
        }
    }

    /**
     * Define the event after the user presses the 'Course History' button.
     * If the user being edited is a student, then an additional popup window can be displayed to show the students
     * history.
     * If the user being edited is a faculty, then the additional popup window shows courses taught history.
     *
     * @date 2019/04/08
     */
    public void courseHistoryOnAction() {

        MenuGUI.uuid = AdminGUI.uuidToEdit;

        if ("Course History".equals(courseHistoryButton.getText())) {
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
        } else {
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
    }

    /**
     * Update the info of User in Database.accounts Database.usernameToUuid and Database.accountsForGUI
     *
     * @date 2019/04/08
     */
    private void updateUser() {

        // update Database.accounts
        String gender;
        if ("Male".equals(genderMenuButton.getText())) {
            gender = "M";
        } else {
            gender = "F";
        }
        Database.accounts.get(AdminGUI.uuidToEdit).setGender(gender);
        Database.accounts.get(AdminGUI.uuidToEdit).setFullName(fullName);
        Database.accounts.get(AdminGUI.uuidToEdit).setUsername(username);
        Database.accounts.get(AdminGUI.uuidToEdit).setAddress(address);
        Database.accounts.get(AdminGUI.uuidToEdit).setEmailAddress(emailAddress);
        Database.accounts.get(AdminGUI.uuidToEdit).setPhoneNumber(phoneNumber);
        Database.accounts.get(AdminGUI.uuidToEdit).setDateOfBirth(new DateOfBirth(dateOfBirth));

        // update Database.accountsForGUI
        Database.usernameToUuid.remove(Database.accounts.get(AdminGUI.uuidToEdit).getUsername());
        Database.usernameToUuid.put(username, AdminGUI.uuidToEdit);

        // update the password if the new password is set
        if (!newPassword.isEmpty()) {
            Database.accounts.get(AdminGUI.uuidToEdit).setPassword(newPassword);
        }

        // update Database.accountsForGUI
        for (UserForGUI i : Database.accountsForGUI) {
            if (i.getUuid().equals(AdminGUI.uuidToEdit)) {
                i.setUsername(username);
                i.setGender(gender);
                i.setFullName(fullName);
                i.setAddress(address);
                i.setEmailAddress(emailAddress);
                i.setPhoneNumber(phoneNumber);
                i.setDateOfBirth(dateOfBirth);
            }
        }
    }

    /**
     * Commit the info of User to database
     *
     * @date 2019/04/08
     */
    private void updateCommit() {
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.GENDER);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.FULL_NAME);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.USERNAME);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.ADDRESS);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.EMAIL);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.DATE_OF_BIRTH);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.PHONE);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.PASSWORD);
    }

    /**
     * shows an error popup if one of the text fields does not contain valid information
     *
     * @return true if all info is valid
     * @date 2019/04/08
     */
    private boolean accountInfoValidation() {

        // validate full name
        if (!Validation.isFullNameValid(fullName)) {
            Utility.showError("ERROR", "Invalid username!", "Possible reasons:\n\n" +
                    "1. It does not contain any uppercase letter\n" +
                    "2. It contains leading or trailing whitespaces.\n" +
                    "3. Its length is smaller than 3.\n" +
                    "4. Its length is greater than 40.");
            return false;
        }

        // validate username
        if (!username.equals(Database.accounts.get(AdminGUI.uuidToEdit).getUsername())) {
            if (!Validation.isUsernameValid(username)) {
                Utility.showError("ERROR", "Invalid username!", "Possible reasons:\n\n" +
                        "1. It is empty.\n" +
                        "2. It only contains whitespaces.\n" +
                        "3. It is identical to another username in the database.\n" +
                        "4. Its length is smaller than 4.\n" +
                        "5. Its length is greater than 20.");
                return false;
            }
        }

        // validate address
        if (!Validation.isAddressValid(address)) {
            Utility.showError("ERROR", "Invalid address!", "Possible reasons:\n\n" +
                    "1. It is empty.\n" +
                    "2. Its length is greater than 50.\n" +
                    "3. It contains characters other than letters, digits, spaces, dashes, dots and commas");
            return false;
        }

        // validate phone number
        if (!Validation.isPhoneNumberValid(phoneNumber)) {
            Utility.showError("ERROR", "Invalid phone number!", "Possible reason:\n\n" +
                    "It does not follow the format ###-###-####.");
            return false;
        }

        // validate date of birth
        if (!Validation.isDateOfBirthValid(new DateOfBirth(dateOfBirth))) {
            Utility.showError("ERROR", "Invalid date of birth!", "Possible reasons:\n\n" +
                    "1. The year is smaller than 1900.\n" +
                    "2. The year is greater than current year.\n" +
                    "3. The month or the day is out of range.\n" +
                    "4. It does not follow the format yyyy-mm-dd.");
            return false;
        }

        // validate email address
        if (!Validation.isEmailAddressValid(emailAddress)) {
            Utility.showError("ERROR", "Invalid date of birth!", "Possible reasons:\n\n" +
                    "1. Its format is not valid.\n" +
                    "2. Its length is greater than 40.");
            return false;
        }

        // password validation only works when one of these three fields are not empty
        if (!newPassword.isEmpty() || !newPasswordAgain.isEmpty()) {

            // validate new password
            if (!Validation.isPasswordValid(newPassword)) {
                Utility.showError("ERROR", "New password is invalid!", "Possible reasons:\n\n" +
                        "1. Its length is smaller than 6.\n" +
                        "2. The password contains a non-legal character");
                return false;
            }

            // validate new password again
            if (!newPasswordAgain.equals(newPassword)) {
                Utility.showError("ERROR", "New password not matched!", "");
                return false;
            }
        }

        return true;
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

        // initialization
        uuidLabel.setText(AdminGUI.uuidToEdit);

        // manually changing the slider is not allowed
        slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                -> slider.setValue(passwordIntensity));

        // StudentWindowManager will be changed to AdminGUI.uuidToEdit since this class is used for both student and faculty
        if ("M".equals(Database.accounts.get(AdminGUI.uuidToEdit).getGender())) {
            genderMenuButton.setText("Male");
        } else {
            genderMenuButton.setText("Female");
        }

        switch (Database.accounts.get(AdminGUI.uuidToEdit).getRole()) {
            case "STUDENT":
                roleLabel.setText("STUDENT");
                courseHistoryButton.setVisible(true);
                courseHistoryButton.setText("Course History");
                break;
            case "FACULTY":
                roleLabel.setText("FACULTY");
                courseHistoryButton.setVisible(true);
                courseHistoryButton.setText("Course Teaching");
                break;
            default:
                roleLabel.setText("ADMINISTRATOR");
                courseHistoryButton.setVisible(false);
                break;
        }

        fullNameTextField.setText(Database.accounts.get(AdminGUI.uuidToEdit).getFullName());
        usernameTextField.setText(Database.accounts.get(AdminGUI.uuidToEdit).getUsername());
        phoneNumberTextField.setText(Database.accounts.get(AdminGUI.uuidToEdit).getPhoneNumber());
        dateOfBirthTextField.setText(Database.accounts.get(AdminGUI.uuidToEdit).getDateOfBirth().toString());
        addressTextField.setText(Database.accounts.get(AdminGUI.uuidToEdit).getAddress());
        emailAddressTextField.setText(Database.accounts.get(AdminGUI.uuidToEdit).getEmailAddress());
        newPasswordField.setPromptText("New Password");
        newPasswordAgainField.setPromptText("New Password Again");

        // initialize the local fields
        fullName = fullNameTextField.getText();
        username = usernameTextField.getText();
        address = addressTextField.getText();
        dateOfBirth = dateOfBirthTextField.getText();
        emailAddress = emailAddressTextField.getText();
        phoneNumber = phoneNumberTextField.getText();
        newPassword = newPasswordField.getText();
        newPasswordAgain = newPasswordAgainField.getText();
    }
}
