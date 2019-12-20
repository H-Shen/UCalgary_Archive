import javafx.beans.property.SimpleStringProperty;

/**
 * a class simliar to class User but for GUI purpose
 *
 * @author hshen
 * @version 2019-03-25
 */

public class UserForGUI {

    private SimpleStringProperty uuid         = new SimpleStringProperty();
    private SimpleStringProperty role         = new SimpleStringProperty();
    private SimpleStringProperty username     = new SimpleStringProperty();
    private SimpleStringProperty fullName     = new SimpleStringProperty();
    private SimpleStringProperty gender       = new SimpleStringProperty();
    private SimpleStringProperty dateOfBirth  = new SimpleStringProperty();
    private SimpleStringProperty emailAddress = new SimpleStringProperty();
    private SimpleStringProperty phoneNumber  = new SimpleStringProperty();
    private SimpleStringProperty address      = new SimpleStringProperty();

    public UserForGUI(User user) {
        setUuid(user.getUuid());
        setRole(user.getRole());
        setUsername(user.getUsername());
        setFullName(user.getFullName());
        setGender(user.getGender());
        setDateOfBirth(user.getDateOfBirth().toString());
        setEmailAddress(user.getEmailAddress());
        setPhoneNumber(user.getPhoneNumber());
        setAddress(user.getAddress());
    }

    public String getUuid() {
        return uuid.get();
    }

    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    public SimpleStringProperty uuidProperty() {
        return uuid;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public SimpleStringProperty roleProperty() {
        return role;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public String getFullName() {
        return fullName.get();
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    public SimpleStringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    public String getEmailAddress() {
        return emailAddress.get();
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress.set(emailAddress);
    }

    public SimpleStringProperty emailAddressProperty() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }
}
