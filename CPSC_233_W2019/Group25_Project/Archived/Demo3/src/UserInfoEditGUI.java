import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class UserInfoEditGUI implements Initializable {

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

    private String fullName;
    private String username;
    private String address;
    private String dateOfBirth;
    private String phoneNumber;
    private String emailAddress;
    private String currentPassword;
    private String newPassword;
    private String newPasswordAgain;


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

    public void updateOnAction() {

        if (accountInfoValidation()) {
            updateUser();
            updateCommit();
            Util.showAlert("Notification", null, "Account updated successfully!");
        }
    }

    public void courseHistoryOnAction() {

        // if the user being edited is a student, then the admin can access its course history
        if (courseHistoryButton.getText().equals("Course History")) {

            MenuGUI.uuid = AdminGUI.uuidToEdit;

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

        // if the user being edited is a faculty, then the admin can access courses that the faculty is teaching
        else {
            System.out.println(123);
            // TODO
        }
    }

    /**
     * Update the info of User in Database.accounts Database.usernameToUuid and Database.accountsForGUI
     */
    public void updateUser() {

        // update Database.accountsForGUI
        Database.usernameToUuid.remove(Database.accounts.get(AdminGUI.uuidToEdit).getUsername());
        Database.usernameToUuid.put(username, AdminGUI.uuidToEdit);

        // update Database.accounts
        String gender;
        if (genderMenuButton.getText().equals("Male")) {
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
     */
    public void updateCommit() {

        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.GENDER);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.FULLNAME);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.USERNAME);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.ADDRESS);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.EMAIL);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.DATE_OF_BIRTH);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.PHONE);
        Database.accountUpdateCommit(AdminGUI.uuidToEdit, Constants.USER_FIELD.PASSWORD);

    }

    /**
     *
     */
    public boolean accountInfoValidation() {

        // validate full name
        if (!Validation.isFullNameValid(fullName)) {
            Util.showError("ERROR", "Invalid username!", "Possible reasons:\n\n" +
                    "1. It does not contain any uppercase letter\n" +
                    "2. It contains leading or trailing whitespaces.\n" +
                    "3. Its length is smaller than 2.\n" +
                    "4. Its length is greater than 40.");
            return false;
        }

        // validate username
        if (!username.equals(Database.accounts.get(AdminGUI.uuidToEdit).getUsername())) {
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
            Util.showError("ERROR", "Invalid date of birth!", "Possible reasons:\n\n" +
                    "1. Its format is not valid.\n" +
                    "2. Its length is greater than 40.");
            return false;
        }

        // password validation only works when one of these three fields are not empty
        if (!newPassword.isEmpty() || !newPasswordAgain.isEmpty()) {

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
        uuidLabel.setText(AdminGUI.uuidToEdit);

        // StudentWindowManager will be changed to AdminGUI.uuidToEdit since this class is used for both student and faculty
        if (Database.accounts.get(AdminGUI.uuidToEdit).getGender().equals("M")) {
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
