import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The {@code User} class defines the User generic.
 */
public abstract class User implements Comparable<User> {

    /**
     * Filepath configuration
     */
    private static String      enrollmentStatus = "CLOSE";
    /**
     * Once uuid is initialized, it cannot be changed.
     */
    private        String      uuid;
    private        String      role;
    private        String      username;
    private        String      fullName;
    private        String      gender;
    private        DateOfBirth dateOfBirth;
    private        String      emailAddress;
    private        String      phoneNumber;
    private        String      address;
    private        String      password;

    /**
     * Default Constructor
     */
    public User() {
    }

    /**
     * Gets the enrollment status.
     *
     * @return a String containing the enrollment status.
     */
    public static String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    /**
     * Sets the enrollment status.
     * Either open or close.
     *
     * @param status a String containing the enrollment status.
     */
    public static void setEnrollmentStatus(String status) {
        switch (status) {
            case "OPEN":
                enrollmentStatus = "OPEN";
                updateEnrollmentStatusToFile(enrollmentStatus);
                break;
            case "CLOSE":
                enrollmentStatus = "CLOSE";
                updateEnrollmentStatusToFile(enrollmentStatus);
                break;
            default:
                break;
        }
    }

    /**
     * Updates the enrollment status.
     *
     * @param status a String containing the status.
     */
    public static void updateEnrollmentStatusToFile(String status) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(Constants.ENROLLMENT_CONF))) {
            out.write(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the uuid of the user.
     *
     * @return a String containing the uuid.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the user's uuid.
     * The uuid cannot be empty and it must be valid.
     * The uuid cannot be changed after being initialized.
     *
     * @param uuid a String containing the uuid of the user.
     */
    public void setUuid(String uuid) {

        if (this.uuid == null && Validation.isUuidValid(uuid)) {
            this.uuid = uuid;
        }
    }

    /**
     * Gets the role of the user.
     *
     * @return a String containing the role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     * The role must be valid.
     *
     * @param role a String containing the role of the user.
     */
    public void setRole(String role) {

        if (Validation.isRoleValid(role)) {
            this.role = role;
        }
    }

    /**
     * Gets the username of the user.
     *
     * @return a String containing the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     * The username must be valid.
     * A duplicated username will be ignored.
     *
     * @param username a String containing the username.
     */
    public void setUsername(String username) {

        if (this.username != null && this.username.equals(username)) {
            return;
        }

        if (Validation.isUsernameValid(username)) {
            this.username = username;
        }
    }

    /**
     * Gets the full name of the user.
     *
     * @return a String containing the full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the user.
     * The full name must be valid.
     *
     * @param fullName a String containing the full name.
     */
    public void setFullName(String fullName) {

        // Make sure the fullname is valid
        if (Validation.isFullNameValid(fullName)) {
            this.fullName = fullName;
        }
    }

    /**
     * Gets the gender of the user.
     *
     * @return a String containing the gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user.
     * The gender must be valid.
     *
     * @param gender a String containing the gender.
     */
    public void setGender(String gender) {

        if (Validation.isGenderValid(gender)) {
            this.gender = gender;
        }
    }

    /**
     * Gets the date of birth.
     *
     * @return a String containing the date of birth.
     */
    public DateOfBirth getDateOfBirth() {
        return new DateOfBirth(dateOfBirth);
    }

    /**
     * Sets the date of birth.
     * The date of birth must be valid.
     *
     * @param dateOfBirth a String containing the date of birth.
     */
    public void setDateOfBirth(DateOfBirth dateOfBirth) {

        if (Validation.isDateOfBirthValid(dateOfBirth)) {
            this.dateOfBirth = new DateOfBirth(dateOfBirth);
        }
    }

    /**
     * Gets the email address of the user.
     *
     * @return a String containing the email address.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the user.
     * The email address must be valid.
     *
     * @param emailAddress a String containing the email address.
     */
    public void setEmailAddress(String emailAddress) {

        // Make sure the emailAddress is valid
        if (Validation.isEmailAddressValid(emailAddress)) {
            this.emailAddress = emailAddress;
        }
    }

    /**
     * Gets the phone number of the user.
     *
     * @return a String containing the phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user.
     * The phone number must be valid.
     *
     * @param phoneNumber a String containing the phone number.
     */
    public void setPhoneNumber(String phoneNumber) {

        // Make sure the emailAddress is valid
        if (Validation.isPhoneNumberValid(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }
    }

    /**
     * Gets the password of the user.
     *
     * @return a String containing the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * The password must be valid.
     *
     * @param password a String containing the password.
     */
    public void setPassword(String password) {

        if (Validation.isPasswordValid(password)) {
            this.password = password;
        }
    }

    /**
     * Gets the address of the user.
     *
     * @return a String containing the address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the user.
     * The address must be valid.
     *
     * @param address a String containing the address.
     */
    public void setAddress(String address) {

        // Make sure the emailAddress is valid
        if (Validation.isAddressValid(address)) {
            this.address = address;
        }
    }

    /**
     * Hightlights that keyword that the user searched.
     *
     * @param highlightField the field that is highlighted.
     * @param keyword        a String containing the keyword that was searched.
     * @return a String containing that keyword that is hightlighted.
     */
    public String toStringWithHighLight(Constants.USER_FIELD highlightField, String keyword) {

        StringBuilder sb = new StringBuilder();

        String username;
        if (highlightField == Constants.USER_FIELD.USERNAME) {
            username = TextUI.colorHighlightedSubstring(getUsername(), keyword);
        } else {
            username = getUsername();
        }

        String role;
        if (highlightField == Constants.USER_FIELD.ROLE) {
            role = TextUI.colorHighlightedSubstring(getRole(), keyword);
        } else {
            role = getRole();
        }

        String gender;
        if (highlightField == Constants.USER_FIELD.GENDER) {
            gender = TextUI.colorHighlightedSubstring(getGender(), keyword);
        } else {
            gender = getGender();
        }

        String uuid;
        if (highlightField == Constants.USER_FIELD.UUID) {
            uuid = TextUI.colorHighlightedSubstring(getUuid(), keyword);
        } else {
            uuid = getUuid();
        }

        String fullName;
        if (highlightField == Constants.USER_FIELD.FULLNAME) {
            fullName = TextUI.colorHighlightedSubstring(getFullName(), keyword);
        } else {
            fullName = getFullName();
        }

        String address;
        if (highlightField == Constants.USER_FIELD.ADDRESS) {
            address = TextUI.colorHighlightedSubstring(getAddress(), keyword);
        } else {
            address = getAddress();
        }

        String phoneNumber;
        if (highlightField == Constants.USER_FIELD.PHONE) {
            phoneNumber = TextUI.colorHighlightedSubstring(getPhoneNumber(), keyword);
        } else {
            phoneNumber = getPhoneNumber();
        }

        String emailAddress;
        if (highlightField == Constants.USER_FIELD.EMAIL) {
            emailAddress = TextUI.colorHighlightedSubstring(getEmailAddress(), keyword);
        } else {
            emailAddress = getEmailAddress();
        }

        String dateOfBirth;
        if (highlightField == Constants.USER_FIELD.DATE_OF_BIRTH) {
            dateOfBirth = TextUI.colorHighlightedSubstring(getDateOfBirth().toString(), keyword);
        } else {
            dateOfBirth = getDateOfBirth().toString();
        }

        sb.append(Constants.COLOR_WRAPPER("         UUID : ", Constants.WHITE_BOLD) + uuid + "\n");
        sb.append(Constants.COLOR_WRAPPER("     USERNAME : ", Constants.WHITE_BOLD) + username + "\n");
        sb.append(Constants.COLOR_WRAPPER("     FULLNAME : ", Constants.WHITE_BOLD) + fullName + "\n");
        sb.append(Constants.COLOR_WRAPPER("         ROLE : ", Constants.WHITE_BOLD) + role + "\n");
        sb.append(Constants.COLOR_WRAPPER("       GENDER : ", Constants.WHITE_BOLD) + gender + "\n");
        sb.append(Constants.COLOR_WRAPPER("      ADDRESS : ", Constants.WHITE_BOLD) + address + "\n");
        sb.append(Constants.COLOR_WRAPPER(" PHONE NUMBER : ", Constants.WHITE_BOLD) + phoneNumber + "\n");
        sb.append(Constants.COLOR_WRAPPER("DATE OF BIRTH : ", Constants.WHITE_BOLD) + dateOfBirth + "\n");
        sb.append(Constants.COLOR_WRAPPER("EMAIL ADDRESS : ", Constants.WHITE_BOLD) + emailAddress);

        return sb.toString();

    }

    /**
     * 3 way-comparison by the username's lexicographical order.
     *
     * @param o
     * @return -1 if o cannot be cast to User or its username is smaller, 0 if the usernames are the same, 1 otherwise.
     */
    @Override
    public int compareTo(User o) {
        if (o != null) {
            return getUsername().compareTo(o.getUsername());
        }
        return -1;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(Constants.COLOR_WRAPPER("         UUID : ", Constants.WHITE_BOLD) + getUuid() + "\n");
        sb.append(Constants.COLOR_WRAPPER("     USERNAME : ", Constants.WHITE_BOLD) + getUsername() + "\n");
        sb.append(Constants.COLOR_WRAPPER("     FULLNAME : ", Constants.WHITE_BOLD) + getFullName() + "\n");
        sb.append(Constants.COLOR_WRAPPER("         ROLE : ", Constants.WHITE_BOLD) + getRole() + "\n");
        sb.append(Constants.COLOR_WRAPPER("       GENDER : ", Constants.WHITE_BOLD) + getGender() + "\n");
        sb.append(Constants.COLOR_WRAPPER("      ADDRESS : ", Constants.WHITE_BOLD) + getAddress() + "\n");
        sb.append(Constants.COLOR_WRAPPER(" PHONE NUMBER : ", Constants.WHITE_BOLD) + getPhoneNumber() + "\n");
        sb.append(Constants.COLOR_WRAPPER("DATE OF BIRTH : ", Constants.WHITE_BOLD) + getDateOfBirth() + "\n");
        sb.append(Constants.COLOR_WRAPPER("EMAIL ADDRESS : ", Constants.WHITE_BOLD) + getEmailAddress());

        return sb.toString();
    }
}