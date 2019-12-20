/**
 * The {@code User} class defines the User generic.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public abstract class User implements Comparable<User> {

    /**
     * @Fields the properties of the object. Once uuid is initialized, it cannot be changed.
     */
    private String      uuid;
    private String      role;
    private String      username;
    private String      fullName;
    private String      gender;
    private DateOfBirth dateOfBirth;
    private String      emailAddress;
    private String      phoneNumber;
    private String      address;
    private String      password;

    /**
     * Default Constructor
     *
     * @date 2019/04/08
     */
    public User() {
    }

    /**
     * Gets the uuid of the user.
     *
     * @return a string contains the uuid.
     * @date 2019/04/08
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the user's uuid.
     * The uuid cannot be empty and it must be valid.
     * The uuid cannot be changed after being initialized.
     *
     * @param uuid a string contains the uuid of the user
     * @date 2019/04/08
     */
    public void setUuid(String uuid) {

        if (this.uuid == null && Validation.isUuidValid(uuid)) {
            this.uuid = uuid;
        }
    }

    /**
     * Gets the role of the user.
     *
     * @return a string contains the role
     * @date 2019/04/08
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     * The role must be valid.
     *
     * @param role a string contains the role of the user
     * @date 2019/04/08
     */
    public void setRole(String role) {
        if (Validation.isRoleValid(role)) {
            this.role = role;
        }
    }

    /**
     * Gets the username of the user.
     *
     * @return a string contains the username
     * @date 2019/04/08
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     * The username must be valid.
     * A duplicated username will be ignored.
     *
     * @param username a string contains the username
     * @date 2019/04/08
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
     * @return a string containing the full name
     * @date 2019/04/08
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the user.
     * The full name must be valid.
     *
     * @param fullName a String containing the full name
     * @date 2019/04/08
     */
    public void setFullName(String fullName) {
        if (Validation.isFullNameValid(fullName)) {
            this.fullName = fullName;
        }
    }

    /**
     * Gets the gender of the user.
     *
     * @return a string contains the gender
     * @date 2019/04/08
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user.
     * The gender must be valid.
     *
     * @param gender a string contains the gender
     * @date 2019/04/08
     */
    public void setGender(String gender) {
        if (Validation.isGenderValid(gender)) {
            this.gender = gender;
        }
    }

    /**
     * Gets the date of birth.
     *
     * @return a string contains the date of birth
     * @date 2019/04/08
     */
    public DateOfBirth getDateOfBirth() {
        return new DateOfBirth(dateOfBirth);
    }

    /**
     * Sets the date of birth.
     * The date of birth must be valid.
     *
     * @param dateOfBirth a string contains the date of birth
     * @date 2019/04/08
     */
    public void setDateOfBirth(DateOfBirth dateOfBirth) {
        if (Validation.isDateOfBirthValid(dateOfBirth)) {
            this.dateOfBirth = new DateOfBirth(dateOfBirth);
        }
    }

    /**
     * Gets the email address of the user.
     *
     * @return a string contains the email address
     * @date 2019/04/08
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of the user.
     * The email address must be valid.
     *
     * @param emailAddress a string contains the email address
     * @date 2019/04/08
     */
    public void setEmailAddress(String emailAddress) {
        if (Validation.isEmailAddressValid(emailAddress)) {
            this.emailAddress = emailAddress;
        }
    }

    /**
     * Gets the phone number of the user.
     *
     * @return a string contains the phone number
     * @date 2019/04/08
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of the user.
     * The phone number must be valid.
     *
     * @param phoneNumber a string contains the phone number
     * @date 2019/04/08
     */
    public void setPhoneNumber(String phoneNumber) {
        if (Validation.isPhoneNumberValid(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }
    }

    /**
     * Gets the password of the user.
     *
     * @return a string contains the password
     * @date 2019/04/08
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     * The password must be valid.
     *
     * @param password a string contains the password
     * @date 2019/04/08
     */
    public void setPassword(String password) {
        if (Validation.isPasswordValid(password)) {
            this.password = password;
        }
    }

    /**
     * Gets the address of the user.
     *
     * @return a string contains the address
     * @date 2019/04/08
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the user.
     * The address must be valid.
     *
     * @param address a string contains the address
     * @date 2019/04/08
     */
    public void setAddress(String address) {
        if (Validation.isAddressValid(address)) {
            this.address = address;
        }
    }

    /**
     * Highlights that keyword that the user searched.
     *
     * @param highlightField the field that is highlighted.
     * @param keyword        a String containing the keyword that was searched.
     * @return a string contains that keyword that is highlighted.
     * @date 2019/04/08
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
        if (highlightField == Constants.USER_FIELD.FULL_NAME) {
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

        sb.append(Constants.colorWrapper("         UUID : ", Constants.WHITE_BOLD)).append(uuid).append("\n");
        sb.append(Constants.colorWrapper("     USERNAME : ", Constants.WHITE_BOLD)).append(username).append("\n");
        sb.append(Constants.colorWrapper("    FULL NAME : ", Constants.WHITE_BOLD)).append(fullName).append("\n");
        sb.append(Constants.colorWrapper("         ROLE : ", Constants.WHITE_BOLD)).append(role).append("\n");
        sb.append(Constants.colorWrapper("       GENDER : ", Constants.WHITE_BOLD)).append(gender).append("\n");
        sb.append(Constants.colorWrapper("      ADDRESS : ", Constants.WHITE_BOLD)).append(address).append("\n");
        sb.append(Constants.colorWrapper("DATE OF BIRTH : ", Constants.WHITE_BOLD)).append(dateOfBirth).append("\n");
        sb.append(Constants.colorWrapper(" PHONE NUMBER : ", Constants.WHITE_BOLD)).append(phoneNumber).append("\n");
        sb.append(Constants.colorWrapper("EMAIL ADDRESS : ", Constants.WHITE_BOLD)).append(emailAddress);

        return sb.toString();
    }

    /**
     * 3 way-comparison by the username's lexicographical order.
     *
     * @param o another User object
     * @return -1 if o cannot be cast to User or its username is smaller, 0 if they are the same, 1 otherwise.
     * @date 2019/04/08
     */
    @Override
    public int compareTo(User o) {
        if (o != null) {
            return getUsername().compareTo(o.getUsername());
        }
        return -1;
    }

    /**
     * The method defines the result after a User object is being printed.
     *
     * @return a string represents the User object
     * @date 2019/04/08
     */
    @Override
    public String toString() {

        return Constants.colorWrapper("         UUID : ", Constants.WHITE_BOLD) + getUuid() + "\n" +
                Constants.colorWrapper("     USERNAME : ", Constants.WHITE_BOLD) + getUsername() + "\n" +
                Constants.colorWrapper("    FULL NAME : ", Constants.WHITE_BOLD) + getFullName() + "\n" +
                Constants.colorWrapper("         ROLE : ", Constants.WHITE_BOLD) + getRole() + "\n" +
                Constants.colorWrapper("       GENDER : ", Constants.WHITE_BOLD) + getGender() + "\n" +
                Constants.colorWrapper("      ADDRESS : ", Constants.WHITE_BOLD) + getAddress() + "\n" +
                Constants.colorWrapper("DATE OF BIRTH : ", Constants.WHITE_BOLD) + getDateOfBirth() + "\n" +
                Constants.colorWrapper(" PHONE NUMBER : ", Constants.WHITE_BOLD) + getPhoneNumber() + "\n" +
                Constants.colorWrapper("EMAIL ADDRESS : ", Constants.WHITE_BOLD) + getEmailAddress();
    }
}