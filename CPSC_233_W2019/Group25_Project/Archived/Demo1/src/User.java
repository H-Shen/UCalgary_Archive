/**
 * The definition of the User generic class.
 */

abstract class User {

    /**
     * Once uuid is initialized, it cannot be changed.
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

    public User() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {

        // Make sure the uuid cannot be changed after being initialization
        // Make sure the uuid is valid.
        if (this.uuid == null && Validation.isUuidValid(uuid)) {
            this.uuid = uuid;
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {

        if (Validation.isRoleValid(role)) {
            this.role = role;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        // Make sure the username is valid
        if (Validation.isUsernameValid(username)) {
            this.username = username;
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {

        // Make sure the fullname is valid
        if (Validation.isFullNameValid(fullName)) {
            this.fullName = fullName;
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {

        // Make sure the gender is valid
        if (Validation.isGenderValid(gender)) {
            this.gender = gender;
        }
    }

    public DateOfBirth getDateOfBirth() {
        return new DateOfBirth(dateOfBirth);
    }

    public void setDateOfBirth(DateOfBirth dateOfBirth) {

        // Make sure the DateOfBirth is valid
        if (Validation.isDateOfBirthValid(dateOfBirth)) {
            this.dateOfBirth = new DateOfBirth(dateOfBirth);
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {

        // Make sure the emailAddress is valid
        if (Validation.isEmailAddressValid(emailAddress)) {
            this.emailAddress = emailAddress;
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {

        // Make sure the emailAddress is valid
        if (Validation.isPhoneNumberValid(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        // Make sure the password is valid
        if (Validation.isPasswordValid(password)) {
            this.password = password;
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {

        // Make sure the emailAddress is valid
        if (Validation.isAddressValid(address)) {
            this.address = address;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("         UUID : " + getUuid() + "\n");
        sb.append("     USERNAME : " + getUsername() + "\n");
        sb.append("     FULLNAME : " + getFullName() + "\n");
        sb.append("         ROLE : " + getRole() + "\n");
        sb.append("       GENDER : " + getGender() + "\n");
        sb.append("      ADDRESS : " + getAddress() + "\n");
        sb.append(" PHONE NUMBER : " + getPhoneNumber() + "\n");
        sb.append("DATE OF BIRTH : " + getDateOfBirth() + "\n");
        sb.append("EMAIL ADDRESS : " + getEmailAddress());
        return sb.toString();
    }
}





