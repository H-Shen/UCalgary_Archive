import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoGUI implements Initializable {

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

    private String username;
    private String address;
    private String dateOfBirth;
    private String phoneNumber;
    private String emailAddress;
    private String currentPassword;
    private String newPassword;
    private String newPasswordAgain;

    // define actions
    public void usernameTextFieldKeyReleased(Event e) {
        username = usernameTextField.getText();
    }

    public void addressTextFieldKeyReleased(Event e) {
        address = addressTextField.getText();
    }

    public void dateOfBirthKeyReleased(Event e) {
        dateOfBirth = dateOfBirthTextField.getText();
    }

    public void emailAddressKeyReleased(Event e) {
        emailAddress = emailAddressTextField.getText();
    }

    public void phoneNumberTextFieldKeyReleased(Event e) {
        phoneNumber = phoneNumberTextField.getText();
    }

    public void currentPasswordTextFieldKeyReleased(Event e) {
        currentPassword = currentPasswordField.getText();
    }

    public void newPasswordTextFieldKeyReleased(Event e) {
        newPassword = newPasswordField.getText();
    }

    public void newPasswordAgainTextFieldKeyReleased(Event e) {
        newPasswordAgain = newPasswordAgainField.getText();
    }

    public void updateOnAction(ActionEvent e) {

        if (accountInfoValidation()) {
            updateUser();
            updateCommit();
            Util.showAlert("Notification", null, "Account updated successfully!");
        }
    }

    /**
     * Update the info of User in Database.accounts
     */
    public void updateUser() {

        Database.usernameToUuid.remove(Database.accounts.get(MenuGUI.uuid).getUsername());
        Database.usernameToUuid.put(username, MenuGUI.uuid);
        Database.accounts.get(MenuGUI.uuid).setUsername(username);
        Database.accounts.get(MenuGUI.uuid).setAddress(address);
        Database.accounts.get(MenuGUI.uuid).setEmailAddress(emailAddress);
        Database.accounts.get(MenuGUI.uuid).setPhoneNumber(phoneNumber);
        Database.accounts.get(MenuGUI.uuid).setDateOfBirth(new DateOfBirth(dateOfBirth));

        // update the password if the new password is set
        if (!newPassword.isEmpty()) {
            Database.accounts.get(MenuGUI.uuid).setPassword(newPassword);
        }
    }

    /**
     * Commit the info of User to database
     */
    public void updateCommit() {

        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.USERNAME);
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.ADDRESS);
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.EMAIL);
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.DATE_OF_BIRTH);
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.PHONE);
        Database.accountUpdateCommit(MenuGUI.uuid, Constants.USER_FIELD.PASSWORD);

    }

    /**
     *
     */
    public boolean accountInfoValidation() {

        // validate username
        if (!username.equals(Database.accounts.get(MenuGUI.uuid).getUsername())) {
            if (!Validation.isUsernameValid(username)) {
                Util.showError("ERROR", "Invalid username!", "Possible reasons:\n\n" +
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
            Util.showError("ERROR", "Invalid address!", "Possible reasons:\n\n" +
                    "1. It is empty.\n" +
                    "2. Its length is greater than 50.");
            return false;
        }

        // validate phone number
        if (!Validation.isPhoneNumberValid(phoneNumber)) {
            Util.showError("ERROR", "Invalid phone number!", "Possible reasons:\n\n" +
                    "It does not follow the format ###-###-####.");
            return false;
        }

        // validate date of birth
        if (!Validation.isDateOfBirthValid(new DateOfBirth(dateOfBirth))) {
            Util.showError("ERROR", "Invalid date of birth!", "Possible reasons:\n\n" +
                    "1. The year is smaller than 1900.\n" +
                    "2. The year is greater than current year.\n" +
                    "3. The month or the day is out of range.\n" +
                    "4. It does not follow the format yyyy-mm-dd.");
            return false;
        }

        // validate email address
        if (!Validation.isEmailAddressValid(emailAddress)) {
            Util.showError("ERROR", "Invalid date of birth!", "Possible reasons:\n\n" +
                    "1. Its format is not valid.\n" +
                    "2. Its length is greater than 40.");
            return false;
        }


        // password validation only works when one of these three fields are not empty
        if (!currentPassword.isEmpty() || !newPassword.isEmpty() || !newPasswordAgain.isEmpty()) {

            // validate password
            if (!currentPassword.equals(Database.accounts.get(MenuGUI.uuid).getPassword())) {
                Util.showError("ERROR", "Current password is incorrect!", "");
                return false;
            }

            // validate new password
            if (!Validation.isPasswordValid(newPassword)) {
                Util.showError("ERROR", "New password is invalid!", "Possible reasons:\n\n" +
                        "Its length is smaller than 6.");
                return false;
            }

            // validate new password again
            if (!newPasswordAgain.equals(newPassword)) {
                Util.showError("ERROR", "New password not matched!", "");
                return false;
            }
        }

        return true;
    }

    /**
     * Initialize the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // initialization
        uuidLabel.setText(MenuGUI.uuid);

        // StudentWindowManager will be changed to MenuGUI.uuid since this class is used for both student and faculty
        roleLabel.setText(Database.accounts.get(MenuGUI.uuid).getRole());

        if (Database.accounts.get(MenuGUI.uuid).getGender().equals("M")) {
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
        address = addressTextField.getText();
        dateOfBirth = dateOfBirthTextField.getText();
        emailAddress = emailAddressTextField.getText();
        phoneNumber = phoneNumberTextField.getText();
        currentPassword = currentPasswordField.getText();
        newPassword = newPasswordField.getText();
        newPasswordAgain = newPasswordAgainField.getText();
    }

}
