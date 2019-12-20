import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class UserAddGUI implements Initializable {

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

    private String fullName;
    private String username;
    private String address;
    private String dateOfBirth;
    private String phoneNumber;
    private String emailAddress;
    private String newPassword;
    private String newPasswordAgain;
    private User   user;

    public void studentMenuItemAction() {
        roleMenuButton.setText("STUDENT");
    }

    public void facultyMenuItemAction() {
        roleMenuButton.setText("FACULTY");
    }

    public void adminMenuItemAction() {
        roleMenuButton.setText("ADMIN");
    }

    public void maleMenuItemAction() {
        genderMenuButton.setText("Male");
    }

    public void femaleMenuItemAction() {
        genderMenuButton.setText("Female");
    }


    public void fullNameTextFieldKeyReleased(Event e) {
        fullName = fullNameTextField.getText();
    }

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

    public void newPasswordTextFieldKeyReleased(Event e) {
        newPassword = newPasswordField.getText();
    }

    public void newPasswordAgainTextFieldKeyReleased(Event e) {
        newPasswordAgain = newPasswordAgainField.getText();
    }

    public void createOnAction() {

        if (accountInfoValidation()) {
            boolean ifConfirm = Util.showConfirmation("Confirmation", null, "Are you sure to create the account?");
            if (!ifConfirm) {
                return;
            }
            createUser();
            createCommit();
            Util.showAlert("Notification", null, "Account created successfully!");
        }
    }

    /**
     * Update the info of User in Database.accounts Database.usernameToUuid and Database.accountsForGUI
     */
    public void createUser() {

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
        if (genderMenuButton.getText().equals("Male")) {
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
     *
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
     * Commit the info of User to database
     */
    public void createCommit() {
        // update STUDENTS.db, FACULTY.db and ACCOUNT_DATA.db
        Database.createAccountCommit(user);
    }

    /**
     *
     */
    public boolean accountInfoValidation() {

        // validate full name
        if (!Validation.isFullNameValid(fullName)) {
            Util.showError("ERROR", "Invalid full name!", "Possible reasons:\n\n" +
                    "1. It contains characters other than uppercase letters and spaces\n" +
                    "2. It does not contain an uppercase letter\n" +
                    "3. It contains leading or trailing whitespaces.\n" +
                    "3. Its length is smaller than 2.\n" +
                    "4. Its length is greater than 40.");
            return false;
        }

        // validate username
        if (!Validation.isUsernameValid(username)) {
            Util.showError("ERROR", "Invalid username!", "Possible reasons:\n\n" +
                    "1. It is empty.\n" +
                    "2. It only contains whitespaces.\n" +
                    "3. It is identical to another username in the database.\n" +
                    "4. Its length is smaller than 4.\n" +
                    "5. Its length is greater than 20.");
            return false;
        }


        // validate address
        if (!Validation.isAddressValid(address)) {
            Util.showError("ERROR", "Invalid address!", "Possible reasons:\n\n" +
                    "1. It is empty.\n" +
                    "2. Its length is greater than 50.\n" +
                    "3. It contains characters other than letters, digits, spaces, dashes, dots and commas");
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
            Util.showError("ERROR", "Invalid email address!", "Possible reasons:\n\n" +
                    "1. Its format is not valid.\n" +
                    "2. Its length is greater than 40.\n" +
                    "3. It contains characters other than letters, digits, dashes, underlines, dots, @.");
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
        uuidLabel.setText(RandomUserGenerator.createRandomUuid());
        resetOnAction();
    }

}
