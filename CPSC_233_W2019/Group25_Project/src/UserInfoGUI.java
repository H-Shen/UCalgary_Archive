import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The {@code UserInfoGUI} class defines all the layout and logic of the window which the faculty or student can
 * edit his/her account
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class UserInfoGUI implements Initializable {

    /**
     * @Fields Properties and components used in GUI
     */
    @FXML
    private Label         uuidLabel;
    @FXML
    private Label         roleLabel;
    @FXML
    private Label         genderLabel;
    @FXML
    private Label         fullNameLabel;
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
    private PasswordField currentPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField newPasswordAgainField;
    @FXML
    private Slider        slider;
    @FXML
    private Label         intensityIndicator;

    private String username;
    private String address;
    private String dateOfBirth;
    private String phoneNumber;
    private String emailAddress;
    private String currentPassword;
    private String newPassword;
    private String newPasswordAgain;
    private int    passwordIntensity;

    /**
     * gets the text from the username textfield
     *
     * @date 2019/04/08
     */
    public void usernameTextFieldKeyReleased() {
        username = usernameTextField.getText();
    }

    /**
     * gets the text from the address textfield
     *
     * @date 2019/04/08
     */
    public void addressTextFieldKeyReleased() {
        address = addressTextField.getText();
    }

    /**
     * gets the text from the date of birth textfield
     *
     * @date 2019/04/08
     */
    public void dateOfBirthKeyReleased() {
        dateOfBirth = dateOfBirthTextField.getText();
    }

    /**
     * gets the text from the email textfield
     *
     * @date 2019/04/08
     */
    public void emailAddressKeyReleased() {
        emailAddress = emailAddressTextField.getText();
    }

    /**
     * gets the text from the phone number textfield
     *
     * @date 2019/04/08
     */
    public void phoneNumberTextFieldKeyReleased() {
        phoneNumber = phoneNumberTextField.getText();
    }

    /**
     * gets the text from the current password textfield
     *
     * @date 2019/04/08
     */
    public void currentPasswordTextFieldKeyReleased() {
        currentPassword = currentPasswordField.getText();
    }

    /**
     * gets the text from the new password textfield and indicates its strength
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
            intensityIndicator.setText("Very Secure!");
        } else if (passwordIntensity >= 80) {
            intensityIndicator.setText("Secure");
        } else if (passwordIntensity >= 70) {
            intensityIndicator.setText("Very Strong");
        } else if (passwordIntensity >= 60) {
            intensityIndicator.setText("Strong");
        } else if (passwordIntensity >= 50) {
            intensityIndicator.setText("Average");
        } else if (passwordIntensity >= 25) {
            intensityIndicator.setText("Weak");
        } else if (passwordIntensity > 0) {
            intensityIndicator.setText("Very Weak");
        } else {
            intensityIndicator.setText("");
        }
    }

    /**
     * gets the text from the newPasswordAgain textfield
     *
     * @date 2019/04/08
     */
    public void newPasswordAgainTextFieldKeyReleased() {
        newPasswordAgain = newPasswordAgainField.getText();
    }

    /**
     * updates account on update button by calling other methods
     *
     * @date 2019/04/08
     */
    public void updateOnAction() {
        if (accountInfoValidation()) {
            updateUser();
            updateCommit();
            Utility.showAlert("Notification", null, "Account updated successfully!");
        }
    }

    /**
     * Update the info of User in Database.accounts
     *
     * @date 2019/04/08
     */
    private void updateUser() {

        Database.accounts.get(MenuGUI.uuid).setUsername(username);
        Database.accounts.get(MenuGUI.uuid).setAddress(address);
        Database.accounts.get(MenuGUI.uuid).setEmailAddress(emailAddress);
        Database.accounts.get(MenuGUI.uuid).setPhoneNumber(phoneNumber);
        Database.accounts.get(MenuGUI.uuid).setDateOfBirth(new DateOfBirth(dateOfBirth));

        Database.usernameToUuid.remove(Database.accounts.get(MenuGUI.uuid).getUsername());
        Database.usernameToUuid.put(username, MenuGUI.uuid);

        // update the password if the new password is set
        if (!newPassword.isEmpty()) {
            Database.accounts.get(MenuGUI.uuid).setPassword(newPassword);
        }
    }

    /**
     * Commit the info of User to database
     *
     * @date 2019/04/08
     */
    private void updateCommit() {
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.USERNAME);
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.ADDRESS);
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.EMAIL);
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.DATE_OF_BIRTH);
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.PHONE);
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.PASSWORD);
    }

    /**
     * validates if the information in the text fields is valid
     * if invalid, pops up an error window
     *
     * @return false if info is invalid, otherwise true
     * @date 2019/04/08
     */
    private boolean accountInfoValidation() {

        // validate username
        if (!username.equals(Database.accounts.get(MenuGUI.uuid).getUsername())) {
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
                    "2. Its length is greater than 50.");
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
            Utility.showError("ERROR", "Invalid date of birth!", "Possible reasons:\n\n" +
                    "1. Its format is not valid.\n" +
                    "2. Its length is greater than 40.");
            return false;
        }


        // password validation only works when one of these three fields are not empty
        if (!currentPassword.isEmpty() || !newPassword.isEmpty() || !newPasswordAgain.isEmpty()) {

            // validate password
            if (!currentPassword.equals(Database.accounts.get(MenuGUI.uuid).getPassword())) {
                Utility.showError("ERROR", "Current password is incorrect!", "");
                return false;
            }

            // validate new password
            if (!Validation.isPasswordValid(newPassword)) {
                Utility.showError("ERROR", "New password is invalid!", "Possible reasons:\n\n" +
                        "Its length is smaller than 6.");
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
        uuidLabel.setText(MenuGUI.uuid);

        // manually changing the slider is not allowed
        slider.valueProperty().addListener((ObservableValue<? extends Number> ov, Number oldVal, Number newVal)
                -> slider.setValue(passwordIntensity));

        // StudentWindowManager will be changed to MenuGUI.uuid since this class is used for both student and faculty
        roleLabel.setText(Database.accounts.get(MenuGUI.uuid).getRole());

        if ("M".equals(Database.accounts.get(MenuGUI.uuid).getGender())) {
            genderLabel.setText("Male");
        } else {
            genderLabel.setText("Female");
        }
        fullNameLabel.setText(Database.accounts.get(MenuGUI.uuid).getFullName());

        usernameTextField.setText(Database.accounts.get(MenuGUI.uuid).getUsername());
        phoneNumberTextField.setText(Database.accounts.get(MenuGUI.uuid).getPhoneNumber());
        dateOfBirthTextField.setText(Database.accounts.get(MenuGUI.uuid).getDateOfBirth().toString());
        addressTextField.setText(Database.accounts.get(MenuGUI.uuid).getAddress());
        emailAddressTextField.setText(Database.accounts.get(MenuGUI.uuid).getEmailAddress());
        currentPasswordField.setPromptText("Current Password");
        newPasswordField.setPromptText("New Password");
        newPasswordAgainField.setPromptText("New Password Again");

        username = usernameTextField.getText();

        // initialize the local fields
        emailAddress = emailAddressTextField.getText();
        address = addressTextField.getText();
        dateOfBirth = dateOfBirthTextField.getText();
        phoneNumber = phoneNumberTextField.getText();
        currentPassword = currentPasswordField.getText();
        newPassword = newPasswordField.getText();
        newPasswordAgain = newPasswordAgainField.getText();

        // if the intensity of current password is weak, pop out a window to prompt the user to change
        if (Utility.passwordIntensity(Database.accounts.get(MenuGUI.uuid).getPassword()) < 25) {
            Utility.showAlert("Warning", "The password is weak!", "For the purpose of safety, changing the password" +
                    " is strongly recommended!");
        }
    }
}
