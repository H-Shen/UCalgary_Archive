import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

/**
 * The {@code UserAddGUI} class defines all the layout and logic of the window which the user can add a user.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class UserAddGUI implements Initializable {

    /**
     * @Fields Properties and components used in GUI
     */
    @FXML
    private Label         uuidLabel;
    @FXML
    private MenuButton    roleMenuButton;
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
    private User   user;
    private int    passwordIntensity;

    /**
     * sets button label to "STUDENT"
     *
     * @date 2019/04/08
     */
    public void studentMenuItemAction() {
        roleMenuButton.setText("STUDENT");
    }

    /**
     * sets button label to "FACULTY"
     *
     * @date 2019/04/08
     */
    public void facultyMenuItemAction() {
        roleMenuButton.setText("FACULTY");
    }

    /**
     * sets button label to "ADMIN"
     *
     * @date 2019/04/08
     */
    public void adminMenuItemAction() {
        roleMenuButton.setText("ADMIN");
    }

    /**
     * sets button label to "Male"
     *
     * @date 2019/04/08
     */
    public void maleMenuItemAction() {
        genderMenuButton.setText("Male");
    }

    /**
     * sets button label to "Female"
     *
     * @date 2019/04/08
     */
    public void femaleMenuItemAction() {
        genderMenuButton.setText("Female");
    }

    /**
     * gets text from full name textfield
     *
     * @date 2019/04/08
     */
    public void fullNameTextFieldKeyReleased() {
        fullName = fullNameTextField.getText();
    }

    /**
     * gets text from username textfield
     *
     * @date 2019/04/08
     */
    public void usernameTextFieldKeyReleased() {
        username = usernameTextField.getText();
    }

    public void addressTextFieldKeyReleased() {
        address = addressTextField.getText();
    }

    /**
     * gets text from date of birth textfield
     *
     * @date 2019/04/08
     */
    public void dateOfBirthKeyReleased() {
        dateOfBirth = dateOfBirthTextField.getText();
    }

    /**
     * gets text from email textfield
     *
     * @date 2019/04/08
     */
    public void emailAddressKeyReleased() {
        emailAddress = emailAddressTextField.getText();
    }

    /**
     * gets text from phone number textfield
     *
     * @date 2019/04/08
     */
    public void phoneNumberTextFieldKeyReleased() {
        phoneNumber = phoneNumberTextField.getText();
    }

    /**
     * gets text from password textfield, identifies the password strength
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
            intensityIndicator.setText("Pretty secure!");
        } else if (passwordIntensity >= 80) {
            intensityIndicator.setText("Secure");
        } else if (passwordIntensity >= 70) {
            intensityIndicator.setText("Pretty strong");
        } else if (passwordIntensity >= 60) {
            intensityIndicator.setText("Strong");
        } else if (passwordIntensity >= 50) {
            intensityIndicator.setText("Average");
        } else if (passwordIntensity >= 25) {
            intensityIndicator.setText("Weak");
        } else if (passwordIntensity > 0) {
            intensityIndicator.setText("Pretty weak");
        } else {
            intensityIndicator.setText("");
        }
    }

    /**
     * gets text from newPasswordAgain textfield
     *
     * @date 2019/04/08
     */
    public void newPasswordAgainTextFieldKeyReleased() {
        newPasswordAgain = newPasswordAgainField.getText();
    }

    /**
     * Creates the new account once a pop up window is shows and confirmed
     * a log is added to history log of the event
     * an alert window pops up to validate teh account creation
     *
     * @date 2019/04/08
     */
    public void createOnAction() {
        if (accountInfoValidation()) {
            boolean ifConfirm = Utility.showConfirmation("Confirmation", null, "Are you sure to create the account?");
            if (!ifConfirm) {
                return;
            }
            createUser();
            createCommit();
            // log
            Log.addAnAccount(new Timestamp(System.currentTimeMillis()), MenuGUI.uuid, user.getUuid());
            Utility.showAlert("Notification", null, "Account created successfully!");
        }
    }

    /**
     * Updates the info of User in Database.accounts Database.usernameToUuid and Database.accountsForGUI
     *
     * @date 2019/04/08
     */
    private void createUser() {

        // instantiate user
        switch (roleMenuButton.getText()) {
            case "ADMIN":
                user = new Admin();
                break;
            case "STUDENT":
                user = new Student();
                break;
            default:
                user = new Faculty();
                break;
        }
        user.setUuid(uuidLabel.getText());
        user.setRole(roleMenuButton.getText());
        user.setFullName(fullName);
        user.setUsername(username);
        user.setAddress(address);
        user.setEmailAddress(emailAddress);
        user.setDateOfBirth(new DateOfBirth(dateOfBirth));
        user.setPhoneNumber(phoneNumber);
        user.setPassword(newPassword);
        if ("Male".equals(genderMenuButton.getText())) {
            user.setGender("M");
        } else {
            user.setGender("F");
        }

        // insert to Database.accountsForGUI and Database.accounts and Database.usernameToUuid
        UserForGUI temp = new UserForGUI(user);
        Database.accountsForGUI.add(temp);
        Database.accounts.put(uuidLabel.getText(), user);
        Database.usernameToUuid.put(user.getUsername(), uuidLabel.getText());

        // update AdminGUI.courseList
        AdminGUI.accountList.add(temp);
    }

    /**
     * reset all text fields and add their default prompts
     *
     * @date 2019/04/08
     */
    public void resetOnAction() {

        // clear all text fields
        fullNameTextField.clear();
        usernameTextField.clear();
        phoneNumberTextField.clear();
        dateOfBirthTextField.clear();
        addressTextField.clear();
        emailAddressTextField.clear();
        newPasswordField.clear();
        newPasswordAgainField.clear();
        slider.setValue(0);
        intensityIndicator.setText("");

        // reset the prompt text for all text fields
        fullNameTextField.setPromptText("eg: 'STEPHEN WILLIAMS'");
        usernameTextField.setPromptText("eg: 'stephen williams'");
        phoneNumberTextField.setPromptText("eg: '123-456-7890'");
        dateOfBirthTextField.setPromptText("eg: '1984-01-05'");
        addressTextField.setPromptText("eg: 'Unit 8137 Box 4460,DPO AA 19600'");
        emailAddressTextField.setPromptText("eg: 'stephen_williams@example.org'");
        newPasswordField.setPromptText("New Password");
        newPasswordAgainField.setPromptText("New Password Again");

        // reset the value of all local fields
        fullName = fullNameTextField.getText();
        username = usernameTextField.getText();
        address = addressTextField.getText();
        dateOfBirth = dateOfBirthTextField.getText();
        emailAddress = emailAddressTextField.getText();
        phoneNumber = phoneNumberTextField.getText();
        newPassword = newPasswordField.getText();
        newPasswordAgain = newPasswordAgainField.getText();

        // reset user
        user = null;
    }

    /**
     * Commit the info of User to STUDENTS.db, FACULTY.db and ACCOUNT_DATA.db
     *
     * @date 2019/04/08
     */
    private void createCommit() {
        Database.createAccountCommit(user);
    }

    /**
     * validates if all text fields contain valid info and if not pop up an error window
     *
     * @return false if the fields are invalid, otherwise true
     * @date 2019/04/08
     */
    private boolean accountInfoValidation() {

        // validate uuid
        if (!Validation.isUuidValid(uuidLabel.getText())) {
            Utility.showError("ERROR", null, "This uuid has been used!");
            return false;
        }

        // validate full name
        if (!Validation.isFullNameValid(fullName)) {
            Utility.showError("ERROR", "Invalid full name!", "Possible reasons:\n\n" +
                    "1. It contains characters other than uppercase letters and spaces\n" +
                    "2. It does not contain an uppercase letter\n" +
                    "3. It contains leading or trailing whitespaces.\n" +
                    "3. Its length is smaller than 3.\n" +
                    "4. Its length is greater than 40.");
            return false;
        }

        // validate username
        if (!Validation.isUsernameValid(username)) {
            Utility.showError("ERROR", "Invalid username!", "Possible reasons:\n\n" +
                    "1. It is empty.\n" +
                    "2. It only contains whitespaces.\n" +
                    "3. It is identical to another username in the database.\n" +
                    "4. Its length is smaller than 4.\n" +
                    "5. Its length is greater than 20.");
            return false;
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
            Utility.showError("ERROR", "Invalid phone number!", "Possible reasons:\n\n" +
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
            Utility.showError("ERROR", "Invalid email address!", "Possible reasons:\n\n" +
                    "1. Its format is not valid.\n" +
                    "2. Its length is greater than 40.\n" +
                    "3. It contains characters other than letters, digits, dashes, underlines, dots, @.");
            return false;
        }

        // validate new password
        if (!Validation.isPasswordValid(newPassword)) {
            Utility.showError("ERROR", "New password is invalid!", "Possible reasons:\n\n" +
                    "1. Its length is smaller than 6.\n" +
                    "2. The password contains a non-legal character.");
            return false;
        }

        // validate new password again
        if (!newPasswordAgain.equals(newPassword)) {
            Utility.showError("ERROR", "New password not matched!", "");
            return false;
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
        uuidLabel.setText(RandomUserGenerator.createRandomUuid());
        resetOnAction();

        // manually changing the slider is not allowed
        slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                -> slider.setValue(passwordIntensity));
    }
}
