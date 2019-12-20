import javafx.beans.property.SimpleStringProperty;

/**
 * The {@code UserForGUI} class wraps the class User for GUI purpose
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class UserForGUI {

    /**
     * @Fields Properties of the object
     */
    private final SimpleStringProperty uuid         = new SimpleStringProperty();
    private final SimpleStringProperty role         = new SimpleStringProperty();
    private final SimpleStringProperty username     = new SimpleStringProperty();
    private final SimpleStringProperty fullName     = new SimpleStringProperty();
    private final SimpleStringProperty gender       = new SimpleStringProperty();
    private final SimpleStringProperty dateOfBirth  = new SimpleStringProperty();
    private final SimpleStringProperty emailAddress = new SimpleStringProperty();
    private final SimpleStringProperty phoneNumber  = new SimpleStringProperty();
    private final SimpleStringProperty address      = new SimpleStringProperty();

    /**
     * Constructor that gets a User object and the attributes for their GUI, and sets them all to simple strings using
     * other
     * methods so it can be displayed in the GUI.
     *
     * @param user the User object needs to be wrapped
     */
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

    /**
     * getter for uuid
     *
     * @return uuid
     * @date 2019/04/08
     */
    public String getUuid() {
        return uuid.get();
    }

    /**
     * setter for uuid
     *
     * @param uuid uuid to set
     * @date 2019/04/08
     */
    private void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    /**
     * turns uuid into a simple string
     *
     * @return uuid
     * @date 2019/04/08
     */
    public SimpleStringProperty uuidProperty() {
        return uuid;
    }

    /**
     * getter for role
     *
     * @return role
     * @date 2019/04/08
     */
    public String getRole() {
        return role.get();
    }

    /**
     * setter for role
     *
     * @param role role to set
     * @date 2019/04/08
     */
    private void setRole(String role) {
        this.role.set(role);
    }

    /**
     * turns role into a simple string
     *
     * @return role
     * @date 2019/04/08
     */
    public SimpleStringProperty roleProperty() {
        return role;
    }

    /**
     * getter for username
     *
     * @return username
     * @date 2019/04/08
     */
    public String getUsername() {
        return username.get();
    }

    /**
     * setter for username
     *
     * @param username username to set
     * @date 2019/04/08
     */
    public void setUsername(String username) {
        this.username.set(username);
    }

    /**
     * turns username into a simple string
     *
     * @return username
     * @date 2019/04/08
     */
    public SimpleStringProperty usernameProperty() {
        return username;
    }

    /**
     * getter for full name
     *
     * @return fullName
     * @date 2019/04/08
     */
    public String getFullName() {
        return fullName.get();
    }

    /**
     * setter for full name
     *
     * @param fullName full name to set
     * @date 2019/04/08
     */
    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    /**
     * turns full name into a simple string
     *
     * @return fullName
     * @date 2019/04/08
     */
    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    /**
     * getter for gender
     *
     * @return gender
     * @date 2019/04/08
     */
    public String getGender() {
        return gender.get();
    }

    /**
     * setter for gender
     *
     * @param gender gender to set
     * @date 2019/04/08
     */
    public void setGender(String gender) {
        this.gender.set(gender);
    }

    /**
     * turns gender into a simple string
     *
     * @return gender
     * @date 2019/04/08
     */
    public SimpleStringProperty genderProperty() {
        return gender;
    }

    /**
     * getter for datOfBirth
     *
     * @return dateOfBirth
     * @date 2019/04/08
     */
    public String getDateOfBirth() {
        return dateOfBirth.get();
    }

    /**
     * setter for datOfBirth
     *
     * @param dateOfBirth date of birth to set
     * @date 2019/04/08
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth.set(dateOfBirth);
    }

    /**
     * turns dateOfBirth into a simple string
     *
     * @return dateOfBirth
     * @date 2019/04/08
     */
    public SimpleStringProperty dateOfBirthProperty() {
        return dateOfBirth;
    }

    /**
     * getter for email
     *
     * @return emailAddress
     * @date 2019/04/08
     */
    public String getEmailAddress() {
        return emailAddress.get();
    }

    /**
     * setter for email
     *
     * @param emailAddress email address to set
     * @date 2019/04/08
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress.set(emailAddress);
    }

    /**
     * turns email into a simple string
     *
     * @return emailAddress
     * @date 2019/04/08
     */
    public SimpleStringProperty emailAddressProperty() {
        return emailAddress;
    }

    /**
     * getter for phone number
     *
     * @return phoneNumber
     * @date 2019/04/08
     */
    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    /**
     * setter for phone number
     *
     * @param phoneNumber phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    /**
     * turns phone number into a simple string
     *
     * @return phoneNumber
     * @date 2019/04/08
     */
    public SimpleStringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    /**
     * getter for address
     *
     * @return address
     * @date 2019/04/08
     */
    public String getAddress() {
        return address.get();
    }

    /**
     * setter for address
     *
     * @param address address to set
     * @date 2019/04/08
     */
    public void setAddress(String address) {
        this.address.set(address);
    }

    /**
     * turns address into a simple string
     *
     * @return address
     * @date 2019/04/08
     */
    public SimpleStringProperty addressProperty() {
        return address;
    }
}
